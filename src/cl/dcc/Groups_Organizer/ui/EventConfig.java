package cl.dcc.Groups_Organizer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.ConnectionStatus;
import cl.dcc.Groups_Organizer.connection.CreateEventConn;
import cl.dcc.Groups_Organizer.connection.GetEventInfoConn;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new AdminPreferences(this);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null && extras.containsKey("Event")) {
            mEvent = Parcels.unwrap(extras.getParcelable("Event"));
        }

        mLoadingMsg = new LoadingThing(EventConfig.this);
        mAdapter = new PersonAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<Person>());
    }

    @AfterViews
    protected void loadEventInfo(){

        if (mEvent != null){
            mEventName.setText(mEvent.getName());
            mEventDescription.setText(mEvent.getDescription());
            if(mEvent.getDatetime() != null) 
            	mEventWhen.setText(dateFormat.format(mEvent.getDatetime()));
            mEventWhere.setText(mEvent.getLocation());
            List<Person> guestList = mAdapter.getList();
            guestList.clear();
            guestList.addAll(mEvent.getGuestList());
            mAdapter.notifyDataSetChanged();

            buttonCreate.setText("Editar");
        }
    }

    private void canEditEvent(){
        if(true) {
            mEventName.setKeyListener(null);
            mEventDescription.setKeyListener(null);
            mEventWhen.setKeyListener(null);
            mEventWhere.setKeyListener(null);
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
        if (mEvent == null)
            return;

        if (!ConnectionStatus.isOnline(this)) {
            Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
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
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            	if (statusCode != 200 ) {
            		Toast.makeText(EventConfig.this, "Error al traer la información del evento", Toast.LENGTH_SHORT).show();
            		return;
            	}

            	try {
                    mEvent = new Event(response);
					preferences.saveEvent(mEvent);
				} catch (JSONException e) {
					Toast.makeText(EventConfig.this, "Error al traer la información del evento", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					return;
				}
            	
            	loadEventInfo();
            	super.onSuccess(statusCode, headers, response);
            }
        });
    }

    public void onCreateEvent(View v){
        mLoadingMsg.startPopUp();
        CreateEventConn createEventConn = new CreateEventConn(getHttpClient());
        RequestParams params = createEventConn.generateParams(mEventName.getText(), mEventDescription.getText(), mEventWhere.getText(),
                mEventWhen.getText(), mAdapter.getList());

        createEventConn.go(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mLoadingMsg.stopPopUp();
                Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200 && responseString.trim().equals("OK")) {
                    mLoadingMsg.stopPopUp();
                    Toast.makeText(getApplicationContext(), "Evento creado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    mLoadingMsg.stopPopUp();
                    Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void buttonAddPeople() {
        startActivity(new Intent(this, AddPeople_.class));
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


}
