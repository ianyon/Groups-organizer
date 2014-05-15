package cl.dcc.Groups_Organizer.ui;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.controller.EventAdapter;
import cl.dcc.Groups_Organizer.data.Event;

/**
 * Created by Ian on 13-04-2014.
 */
public class MyEvents extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Event[] data = new Event[3];
        //TODO: Fake data: delete
        for (int i = 0; i < 3; i++)
            data[i] = new Event("Evento " + i, "Santa Rosa", "30", "5");

        EventAdapter adapter = new EventAdapter(getActivity(), R.layout.event_row, data);
        setListAdapter(adapter);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_list, container, false);

        return v;
    }*/
}