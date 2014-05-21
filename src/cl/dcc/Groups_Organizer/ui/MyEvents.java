package cl.dcc.Groups_Organizer.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.controller.EventAdapter;
import cl.dcc.Groups_Organizer.data.Event;
import org.parceler.Parcels;

import java.util.Date;

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
            data[i] = new Event("Evento " + i,"Un evento entretenido",  "Santa Rosa 950",new Date());

        EventAdapter adapter = new EventAdapter(getActivity(), R.layout.event_row, data);
        setListAdapter(adapter);
    }

    public void onListItemClick (ListView l, View v, int position, long id){
        Intent i = new Intent(this.getActivity(), EventConfig_.class);
        Bundle extras = new Bundle();
        extras.putParcelable("Event", Parcels.wrap(l.getItemAtPosition(position)));
        i.putExtras(extras);
        startActivity(i);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_list, container, false);

        return v;
    }*/
}