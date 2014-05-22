package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.Event;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.parceler.Parcels;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.event_config)
public class EventConfig extends CustomFragmentActivity{

    private Event mEvent;
    EditText mEventName,
             mEventDescription,
             mEventWhen,
             mEventWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getIntent().getExtras();

        mEventName = (EditText) findViewById(R.id.eventConfigEventName);
        mEventDescription = (EditText) findViewById(R.id.eventConfigEventDescription);
        mEventWhen = (EditText) findViewById(R.id.eventConfigEventWhen);
        mEventWhere = (EditText) findViewById(R.id.eventConfigEventWhere);

        if(extras != null && extras.containsKey("Event")){
            mEvent = Parcels.unwrap(extras.getParcelable("Event"));
            mEventName.setText(mEvent.getName());
            mEventDescription.setText(mEvent.getDescription());
            mEventWhen.setText(mEvent.getTimeDare());
            mEventWhere.setText(mEvent.getLocation());
        }
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
