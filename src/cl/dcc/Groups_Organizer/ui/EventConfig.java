package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.Event;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.parceler.Parcels;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.event_config)
public class EventConfig extends CustomFragmentActivity{

    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getIntent().getExtras();

        if(extras != null && extras.containsKey("Event")){
            mEvent = Parcels.unwrap(extras.getParcelable("Event"));
        }
    }

    @AfterViews
    protected void init() {
        if (mEvent != null)
            Toast.makeText(this, "Llegó un evento", Toast.LENGTH_SHORT).show();
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
