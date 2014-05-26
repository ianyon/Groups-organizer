package cl.dcc.Groups_Organizer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.*;
import cl.dcc.Groups_Organizer.controller.PersonAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import cl.dcc.Groups_Organizer.data.Event;
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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.event_config)
public class EventConfig extends CustomFragmentActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	
    private AdminPreferences preferences;
    private Event mEvent;
    private PersonAdapter mAdapter;
    private LoadingThing mLoadingMsg;
    private Person mUser;
    private boolean mNewEvent = true;

    @ViewById(R.id.eventConfigEventName)
    EditText mEventName;

    @ViewById(R.id.eventConfigEventDescription)
    EditText mEventDescription;

    @ViewById(R.id.eventConfigEventWhen)
    EditText mEventWhen;

    @ViewById(R.id.eventConfigEventWhere)
    EditText mEventWhere;

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
            if (extras.containsKey("Event")) {
                mEvent = Parcels.unwrap(extras.getParcelable("Event"));
                mNewEvent = false;

            }
            if(PagerViewHost_.mUser != null) {
                mUser = PagerViewHost_.mUser;//Parcels.unwrap(extras.getParcelable("User"));
            }

        }

        mLoadingMsg = new LoadingThing(EventConfig.this);
        mAdapter = new PersonAdapter(this, new ArrayList<Person>());
    }

    @AfterViews
    protected void loadEventInfo(){

        if (!mNewEvent){
            mEventName.setText(mEvent.getName());
            mEventDescription.setText(mEvent.getDescription());
            if(mEvent.getDatetime() != null) 
            	mEventWhen.setText( mEvent.getDatetime());
            mEventWhere.setText(mEvent.getLocation());
            List<Person> guestList = mAdapter.getList();
            //guestList.clear();
            guestList.addAll(mEvent.getGuestList());
            mAdapter.notifyDataSetChanged();
            canEditEvent();
        }
    }

    private void canEditEvent(){
        if(mUser != null) {
            if(!mEvent.isAdmin(mUser)) {
                mEventName.setFocusable(false);
                mEventDescription.setFocusable(false);
                mEventWhen.setFocusable(false);
                mEventWhere.setFocusable(false);
                mAddPeopleButton.setText("I am Going");
                mCreateButton.setText("Return");
            } else {
                mEventName.setFocusableInTouchMode(true);
                mEventDescription.setFocusableInTouchMode(true);
                mEventWhen.setFocusableInTouchMode(true);
                mEventWhere.setFocusableInTouchMode(true);
                mCreateButton.setText("Save Changes");
                mAddPeopleButton.setText("Add People");
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
        if (mEvent == null) {
            mLoadingMsg.stopPopUp();
            return;
        }

        if (!ConnectionStatus.isOnline(this)) {
            Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
            mLoadingMsg.stopPopUp();
            return;
        }

        // Connection for public event list
        GetEventInfoConn eventsConn = new GetEventInfoConn(getHttpClient());
        RequestParams params = eventsConn.generateParams(mEvent.getId());
        eventsConn.go(params, new JsonHttpResponseHandler() {
        	
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            	String message = "Error when connecting to the server";
            	if("EVENT NOT FOUND".equals(responseString)) {
            		message = "Event not found";
            	} else if("ERROR".equals(responseString)) {
            		message = "Error in server";
            	}

                Toast.makeText(EventConfig.this, message, Toast.LENGTH_LONG).show();
                mLoadingMsg.stopPopUp();
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            	if (statusCode != 200 ) {
            		Toast.makeText(EventConfig.this, "Error al traer la información del evento", Toast.LENGTH_SHORT).show();
                    mLoadingMsg.stopPopUp();
            		return;
            	}

            	try {
                    mEvent = new Event(response);
					preferences.saveEvent(mEvent);
				} catch (JSONException e) {
					Toast.makeText(EventConfig.this, "Error al traer la información del evento", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
                    mLoadingMsg.stopPopUp();
					return;
				}
                mLoadingMsg.stopPopUp();
            	loadEventInfo();
            	super.onSuccess(statusCode, headers, response);
            }
        });

    }

    public void onCreateEvent(View v){

        mLoadingMsg.startPopUp();
        if(!mNewEvent && !mEvent.isAdmin(mUser)){
            finish();
        }

        if(mNewEvent) {
            CreateEventConn createEventConn = new CreateEventConn(getHttpClient());
            RequestParams params = createEventConn.generateParams(mEventName.getText(), mEventDescription.getText(), mEventWhere.getText(),
                    mEventWhen.getText(), mAdapter.getList());

            createEventConn.go(params, new MyHttpResponseHandler("Event Created"));
        } else {
            UpdateEventConn updateEvent = new UpdateEventConn(getHttpClient());
            RequestParams params = updateEvent.generateParams(mEvent.getId(),mEventName.getText(),mEventDescription.getText(),mEventWhere.getText(),mEventWhen.getText(),mAdapter.getList());
            updateEvent.go(params, new MyHttpResponseHandler("Event Updated"));
        }

        mLoadingMsg.stopPopUp();
        finish();
    }

    @Click
    void buttonAddPeople() {
        if(!mNewEvent && !mEvent.isAdmin(mUser)) {
            mLoadingMsg.startPopUp();
            ConfirmConn confirmConn = new ConfirmConn(getHttpClient());
            RequestParams params = confirmConn.generateParams(mEvent.getId(),mEvent.isGoing(mUser)?0:1);
            confirmConn.go(params,new MyHttpResponseHandler("Change attendance status"));
            mLoadingMsg.stopPopUp();
            return;
        }
        startActivityForResult(new Intent(this, AddPeople_.class), 42);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 42) {
            if(resultCode == RESULT_OK){

                    if(data.getParcelableExtra("People") != null) {
                        List<Person> newOnes = Parcels.unwrap(data.getParcelableExtra("People"));
                        for(Person aPerson: newOnes){
                            Toast.makeText(EventConfig.this, aPerson.getName(), Toast.LENGTH_LONG).show();
                        }
	                    mEvent.addGuests(newOnes);
						mAttendees.setAdapter(new PersonAdapter(this, mEvent.getGuestList()));

                    }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(EventConfig.this, "Fail to recive list", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences.getPreferencias(AdminPreferences.PREFERENCIAS_EVENTOS).registerOnSharedPreferenceChangeListener(this);
        onDataChanged();
        refresh();

    }

    @Override
    public void onStop() {
        super.onStop();
        preferences.getPreferencias(AdminPreferences.PREFERENCIAS_EVENTOS).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void onDataChanged() {
        if (mEvent == null)
            return;

        /* Cargamos la info */
        Event event = preferences.getEvent(mEvent.getId());

        if (event == null)
            return;
        mEvent = event;
        loadEventInfo();
//        mAdapter.getList().clear();
//        mAdapter.getList().addAll(event.getGuestList());
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(mEvent.getId()+""))
            onDataChanged();
    }


    private class MyHttpResponseHandler extends TextHttpResponseHandler {

        private String mMsg;


        public MyHttpResponseHandler(String message){
            super();
            mMsg = message;

        }
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            if (statusCode == 200 && responseString.trim().equals("OK")) {
                Toast.makeText(getApplicationContext(), mMsg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
