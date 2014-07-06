package cl.dcc.Groups_Organizer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.ConnectionStatus;
import cl.dcc.Groups_Organizer.connection.CreateGroupConn;
import cl.dcc.Groups_Organizer.connection.EditGroupConn;
import cl.dcc.Groups_Organizer.connection.GetGroupInfoConn;
import cl.dcc.Groups_Organizer.controller.PersonAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import cl.dcc.Groups_Organizer.data.Group;
import cl.dcc.Groups_Organizer.data.Person;
import cl.dcc.Groups_Organizer.utilities.LoadingThing;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.group_config)
public class GroupConfig extends CustomFragmentActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	
    private AdminPreferences preferences;
    private Group mGroup;
    private PersonAdapter mAdapter;
    private LoadingThing mLoadingMsg;
    private Person mUser;
    private boolean mNewGroup = true;
	private boolean mUserIsMember;

    @ViewById(R.id.eventConfigEventName)
    EditText mEventName;

    @ViewById(R.id.eventConfigEventDescription)
    EditText mEventDescription;

    @ViewById(R.id.eventConfigAsistend)
    ListView mAttendees;

    @ViewById
    Button buttonCreate;

    @ViewById(R.id.buttonAddPeople)
    Button mAddPeopleButton;

    @ViewById(R.id.buttonCreate)
    Button mCreateButton;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new AdminPreferences(this);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("Group")) {
                mGroup = Parcels.unwrap(extras.getParcelable("Group"));
                mNewGroup = false;
            }
            if(PagerViewHost_.mUser != null) {
                mUser = PagerViewHost_.mUser;//Parcels.unwrap(extras.getParcelable("User"));
            }

        }

        mLoadingMsg = new LoadingThing(GroupConfig.this);
        mAdapter = new PersonAdapter(this, new ArrayList<Person>());
    }

    @AfterViews
    protected void loadGroupInfo() {
	    if (!mNewGroup) {
		    mEventName.setText(mGroup.getName());
		    mEventDescription.setText(mGroup.getDescription());
		    mAttendees.setAdapter(new PersonAdapter(this, mGroup.getMemberList()));
		    canEditEvent();
	    }
    }

	private void canEditEvent(){
        if(mUser != null) {
            if(!mNewGroup) {
	            mEventName.setKeyListener(null);
	            if(mUserIsMember) {
		            mCreateButton.setText("Leave group");
	            }
            } else {
	            mCreateButton.setText("Save Changes");
	        }
	    }
    }
    private void showRegisterWarning(CharSequence text){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        assert context != null;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void refresh() {
        mLoadingMsg.startPopUp();
        if (mGroup == null) {
            mLoadingMsg.stopPopUp();
            return;
        }

        if (!ConnectionStatus.isOnline(this)) {
            Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
            mLoadingMsg.stopPopUp();
            return;
        }

        // Connection for groups retrieval
        GetGroupInfoConn groupConn = new GetGroupInfoConn(getHttpClient());
        RequestParams params = groupConn.generateParams(mGroup.getName());
        groupConn.go(params, new JsonHttpResponseHandler() {

	        @Override
	        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		        String message = "Error when connecting to the server";
		        if ("EVENT NOT FOUND".equals(responseString)) {
			        message = "Event not found";
		        } else if ("ERROR".equals(responseString)) {
			        message = "Error in server";
		        }

		        Toast.makeText(GroupConfig.this, message, Toast.LENGTH_LONG).show();
		        mLoadingMsg.stopPopUp();
	        }

	        @Override
	        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		        if (statusCode != 200) {
			        Toast.makeText(GroupConfig.this, "Error al traer la información del grupo", Toast.LENGTH_SHORT).show();
			        mLoadingMsg.stopPopUp();
			        return;
		        }

		        try {
			        mGroup = new Group(response);
			        for(Person p : mGroup.getMemberList()) {
				        if(p.getUsername().equals(mUser.getUsername())) {
					        mUserIsMember = true;
				        }
			        }
			        preferences.saveGroup(mGroup);
		        } catch (JSONException e) {
			        Toast.makeText(GroupConfig.this, "Error al traer la información del grupo", Toast.LENGTH_SHORT).show();
			        Log.e("JSON", "Error al traer la información del grupo");
			        mLoadingMsg.stopPopUp();
			        return;
		        }
		        mLoadingMsg.stopPopUp();
		        loadGroupInfo();
		        super.onSuccess(statusCode, headers, response);
	        }
        });

    }

    public void onCreateGroup(View v){

        mLoadingMsg.startPopUp();
        if(!mNewGroup /*&& !mGroup.isAdmin(mUser)*/){
	        EditGroupConn editGroupConn = new EditGroupConn(getHttpClient());
	        List<Person> members = new CopyOnWriteArrayList<Person>(mGroup.getMemberList());
	        for(Person p : members) {
		        if(p.getUsername().equals(mUser.getUsername())) {
			        members.remove(p);
		        }
	        }
			RequestParams params = editGroupConn.generateParams(mGroup.getName(), mGroup.getDescription(), members);
	        editGroupConn.go(params, new TextHttpResponseHandler() {

		        @Override
		        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			        Toast.makeText(GroupConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
		        }

		        @Override
		        public void onSuccess(int statusCode, Header[] headers, String responseString) {
			        if(statusCode != 200) {
				        Toast.makeText(GroupConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
			        }
			        Toast.makeText(GroupConfig.this, "Successfully left group", Toast.LENGTH_LONG).show();
			        finish();
		        }
	        });
	        return;
        }
        CreateGroupConn createGroupConn = new CreateGroupConn(getHttpClient());
        RequestParams params = createGroupConn.generateParams(mEventName.getText(), mEventDescription.getText(), mAdapter.getList());

        createGroupConn.go(params, new MyHttpResponseHandler("Event Created",true));

        mLoadingMsg.stopPopUp();
    }

    @Click
    void buttonAddPeople() {
        try {
            startActivity(new Intent(this, AddPeople_.class));
        }
        catch (Exception e){
            Log.e("Error Configuracion de Grupo"," Add people fail to start");
        }
    }

	@AfterViews
	public void afterViews() {
		onDataChanged();
		refresh();
	}

    @Override
    public void onStart() {
        super.onStart();
        preferences.getPreferencias(AdminPreferences.PREFERENCIAS_GRUPOS).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        preferences.getPreferencias(AdminPreferences.PREFERENCIAS_GRUPOS).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void onDataChanged() {
        if (mGroup == null)
            return;

        /* Cargamos la info */
        Group group = preferences.getGroup(mGroup.getName());

        if (group == null)
            return;
        mGroup = group;
        loadGroupInfo();
//        mAdapter.getList().clear();
//        mAdapter.getList().addAll(event.getGuestList());
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Todo check mGroup is not null
        if (key.equals(mGroup.getName()))
            onDataChanged();
    }


    private class MyHttpResponseHandler extends TextHttpResponseHandler {

        private String mMsg;
        private boolean mFinish;

        public MyHttpResponseHandler(String message,boolean end){
            super();
            mMsg = message;
            mFinish = end;
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            Toast.makeText(GroupConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            if (statusCode == 200 && responseString.trim().equals("OK")) {
                Toast.makeText(getApplicationContext(), mMsg, Toast.LENGTH_SHORT).show();
                if(mFinish)
                    finish();
            } else {
                Toast.makeText(GroupConfig.this, "Error when connecting to the server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
