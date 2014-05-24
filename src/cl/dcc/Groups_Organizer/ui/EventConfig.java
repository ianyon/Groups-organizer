package cl.dcc.Groups_Organizer.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import cl.dcc.Groups_Organizer.connection.CreateEventConn;
import com.loopj.android.http.TextHttpResponseHandler;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.ConnectionStatus;
import cl.dcc.Groups_Organizer.connection.GetEventInfoConn;
import cl.dcc.Groups_Organizer.controller.PersonAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import cl.dcc.Groups_Organizer.data.Event;
import cl.dcc.Groups_Organizer.data.Person;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.event_config)
public class EventConfig extends CustomFragmentActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AdminPreferences preferences;
    private Event mEvent;
    private PersonAdapter mAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new AdminPreferences(this);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null && extras.containsKey("Event")) {
            mEvent = Parcels.unwrap(extras.getParcelable("Event"));
        }
        
        mAdapter = new PersonAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<Person>());
    }

    @AfterViews
    public void loadEventInfo(){

        if (mEvent != null){
            mEventName.setText(mEvent.getName());
            mEventDescription.setText(mEvent.getDescription());
            mEventWhen.setText(mEvent.getTimeDare());
            mEventWhere.setText(mEvent.getLocation());
            List<Person> guestList = mAdapter.getList();
            guestList.clear();
            guestList.addAll(mEvent.getGuestList());
            mAdapter.notifyDataSetChanged();
            //ArrayList<String> myStringArray = getUserList(mEvent);
        }
        //TODO hay que cambiar el evento al que corresponde e implemetar bien getUserList
    }

    private void showRegisterWarning(CharSequence text){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        assert context != null;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private ArrayList<String> getUserList(Event aEvent) {

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        return list;
    }

    @AfterViews
    protected void init() {
        if (mEvent != null) {
            Toast.makeText(this, "Llegó un evento", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillInfotmation(Event aEvent) {


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
                Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            	if (statusCode != 200 ) {
            		Toast.makeText(EventConfig.this, "Error al traer la información del evento", Toast.LENGTH_SHORT).show();
            		return;
            	}
            	
            	try {
					preferences.saveEvent(new Event(response));
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
        CreateEventConn createEventConn = new CreateEventConn(getHttpClient());
        RequestParams params = createEventConn.generateParams(mEventName.getText(), mEventDescription.getText(), mEventWhere.getText(),
                mEventWhen.getText(), mAdapter.getList());

        createEventConn.go(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200 && responseString.trim().equals("OK")) {
                    Toast.makeText(getApplicationContext(), "Evento creado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Click
    void buttonAddPeople() {
        startActivity(new Intent(this, AddPeople.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();
        preferences.getPreferencias().registerOnSharedPreferenceChangeListener(this);
        onDataChanged();
        refresh();

    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.getPreferencias().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void onDataChanged() {
        if (mEvent == null)
            return;

        /* Cargamos la info */
        Event event = preferences.getEvent(mEvent.getId());

        if (event == null)
            return;

        mAdapter.getList().clear();
        mAdapter.getList().addAll(event.getGuestList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(AdminPreferences.PUBLIC_EVENTS))
            onDataChanged();
    }


}
