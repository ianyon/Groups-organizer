package cl.dcc.Groups_Organizer.ui;

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
import cl.dcc.Groups_Organizer.connection.GetGuestListConn;
import cl.dcc.Groups_Organizer.controller.PersonAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import cl.dcc.Groups_Organizer.data.Event;
import cl.dcc.Groups_Organizer.data.Person;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

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

    }

    @AfterViews
    protected void loadEventInfo(){

        if (mEvent != null){
            mEventName.setText(mEvent.getName());
            mEventDescription.setText(mEvent.getDescription());
            mEventWhen.setText(mEvent.getTimeDare());
            mEventWhere.setText(mEvent.getLocation());
            ArrayList<String> myStringArray = getUserList(mEvent);
            ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myStringArray);
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
        if (!ConnectionStatus.isOnline(this)) {
            Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Connection for public event list
        GetGuestListConn eventsConn = new GetGuestListConn(getHttpClient());
        RequestParams params = eventsConn.generateParams(preferences.getUser().getName(), mEvent.getName());
        eventsConn.go(params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(EventConfig.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200 ) {
                    Toast.makeText(EventConfig.this, "Petición de datos correcta", Toast.LENGTH_SHORT).show();

                    try {
                        JSONArray jsonArray = new JSONArray(responseString.trim());

                        // Check if we got the guestList
                        if(jsonArray.length()==0) {
                            Toast.makeText(EventConfig.this, "No llegaron invitados", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Initialize event eventList
                        List<Person> guestList = mAdapter.getList();
                        guestList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            guestList.add(new Person(jsonArray.getJSONObject(i)));
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(EventConfig.this, "JSON EXCEPTION", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(EventConfig.this, "Error al recibir los datos del evento", Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();
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
        /* Cargamos la info */
        Event event = preferences.getEvent(mEvent.name);

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
