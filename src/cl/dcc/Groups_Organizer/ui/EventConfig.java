package cl.dcc.Groups_Organizer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.Event;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.event_config)
public class EventConfig extends CustomFragmentActivity{

    private Event mEvent;
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

        Bundle extras = this.getIntent().getExtras();


        if(extras != null && extras.containsKey("Event")){
            mEvent = Parcels.unwrap(extras.getParcelable("Event"));
        }

    }

    @AfterViews
    public void loadEventInfo(){

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

    @Click
    void buttonAddPeople() {
        startActivity(new Intent(this, AddPeople.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}
