package cl.dcc.Groups_Organizer.ui;

import cl.dcc.Groups_Organizer.utilities.LoadingThing;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.ConnectionStatus;
import cl.dcc.Groups_Organizer.connection.GetEventListConn;
import cl.dcc.Groups_Organizer.controller.TabsAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import cl.dcc.Groups_Organizer.data.Person;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONArray;
import org.parceler.Parcels;

@EActivity(R.layout.pager_view)
public class PagerViewHost extends CustomFragmentActivity {
    // Sección pager
    @ViewById(android.R.id.tabhost)
    TabHost mTabHost;

    @ViewById(R.id.pager)
    ViewPager mViewPager;

    @ViewById(R.id.pagerViewTextName)
    TextView mTextUserName;

    @ViewById(R.id.pagerViewButtonGroups)
    ImageButton mButtonGroups;

    @ViewById(R.id.pagerViewButtonProfile)
    ImageButton mButtonProfile;

    @ViewById(R.id.pagerViewButtonCreateEvent)
    View mCreateEvent;

    Person mUser;

    TabsAdapter mTabsAdapter;

    private AdminPreferences preferences;
    private LoadingThing mLoadingMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new AdminPreferences(this);

        Bundle extras = this.getIntent().getExtras();

        if(extras != null && extras.containsKey("User")){
            mUser = Parcels.unwrap(extras.getParcelable("User"));
            preferences.setUser(mUser);
        }else{
            Toast.makeText(this,"Error fatal, no llego un usuario", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    @AfterViews
    void initVars() {
        mTextUserName.setText(mUser.getName());

        // TabHost configuration
        mTabHost.setup();

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mViewPager.setOffscreenPageLimit(2);

        // Configure TabSpecs
        TabHost.TabSpec tabSpec1 = mTabHost.newTabSpec("tab1");
        tabSpec1.setIndicator("Public events", null);
        mTabsAdapter.addTab(tabSpec1, PublicEvents.class, null);

        TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("tab2");
        tabSpec2.setIndicator("My events", null);
        mTabsAdapter.addTab(tabSpec2, MyEvents.class, null);

        mLoadingMsg = new LoadingThing(PagerViewHost.this);
    }

    public void onRefreshTriggered(View v) {
        // TODO: call this method when the user swipes from up to bottom
        refresh();
    }

    public void refresh() {
        if (!ConnectionStatus.isOnline(this)) {
            Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
            return;
        }

        mLoadingMsg.startPopUp();
        // Connection for public event list
        GetEventListConn eventsConn = new GetEventListConn(getHttpClient());
        RequestParams reqParams = eventsConn.generateParams(false);
        eventsConn.go(reqParams, new EventListHttpResponseHandler(AdminPreferences.PUBLIC_EVENTS));


        // Connection for personal event list
        eventsConn = new GetEventListConn(getHttpClient());
        reqParams = eventsConn.generateParams(true);
        eventsConn.go(reqParams, new EventListHttpResponseHandler(AdminPreferences.PRIVATE_EVENTS));

        mLoadingMsg.stopPopUp();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null) {
            mTabHost.setCurrentTabByTag(state.getString("tab"));
        }
    }

    public void onRegisterClick(View v){
        Toast.makeText(PagerViewHost.this, mUser.getUsername(), Toast.LENGTH_LONG).show();
        Intent aIntent = new Intent(this,Register_.class);
        Bundle extras = new Bundle();
        extras.putParcelable("User", Parcels.wrap(mUser));
        aIntent.putExtras(extras);
        startActivity(aIntent);
        }

    public void onAddFriendsClick(View v) {
        startActivity(new Intent(this, AddPeople_.class));
    }

    public void onAddEventClick(View v) {
        startActivity(new Intent(this, EventConfig_.class));
    }

    // Object for Handling the http response
    private class EventListHttpResponseHandler extends JsonHttpResponseHandler {
        private String category, data;
        private String dataType;
        
        public EventListHttpResponseHandler(String dataType) {
			this.dataType = dataType; 
		}

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString,
                              Throwable throwable) {
           // mLoadingMsg.stopPopUp();
            Toast.makeText(PagerViewHost.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
        }
        
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        	if(statusCode != 200) {
        		Toast.makeText(PagerViewHost.this, "Error en la recepción de datos", Toast.LENGTH_SHORT).show();
        	}
            //mLoadingMsg.stopPopUp();
        	preferences.setValores(dataType, response);
        }
    }

}
