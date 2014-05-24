package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.parceler.Parcels;

@EActivity(R.layout.pager_view)
public class PagerViewHost extends CustomFragmentActivity {
    private static final boolean DEBUG = false;
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
    protected void onResume() {
        super.onResume();
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

        // Connection for public event list
        GetEventListConn eventsConn = new GetEventListConn(getHttpClient());
        eventsConn.go(null, new EventListHttpResponseHandler());

        // Connection for personal event list
        eventsConn = new GetEventListConn(getHttpClient());
        RequestParams reqParams = eventsConn.generateParams(mUser.getEmail());
        eventsConn.go(reqParams, new EventListHttpResponseHandler());
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

    public void onRegisterClick(View v){ startActivity(new Intent(this, Register_.class));  }

    public void onAddFriendsClick(View v) {
        startActivity(new Intent(this, AddPeople.class));
    }

    public void onAddEventClick(View v) {
        startActivity(new Intent(this, EventConfig_.class));
    }

    // Object for Handling the http response
    private class EventListHttpResponseHandler extends TextHttpResponseHandler {
        private String category, data;

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString,
                              Throwable throwable) {
            Toast.makeText(PagerViewHost.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            // TODO: hacer mas verboso el control de errores
            if (statusCode == 200 && parseResponse(responseBody)) {
                preferences.setValores(category, data);
                Toast.makeText(PagerViewHost.this, "Datos recibidos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PagerViewHost.this, "Error en la recepción de datos", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean parseResponse(String body) {
            body = body.trim();
            int i;
            if ((i = body.indexOf("\n")) == -1)
                return false;

            category = body.substring(0, i);
            data = body.substring(i);
            return true;
        }
    }

}
