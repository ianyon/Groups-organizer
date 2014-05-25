package cl.dcc.Groups_Organizer.ui;

import android.os.Bundle;
import cl.dcc.Groups_Organizer.R;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Ian on 15-05-2014.
 */

@EActivity(R.layout.add_people)
public class AddPeople extends CustomFragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people);
    }


}