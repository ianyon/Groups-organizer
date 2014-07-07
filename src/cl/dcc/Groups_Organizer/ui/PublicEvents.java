package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.controller.EventAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import cl.dcc.Groups_Organizer.data.Event;
import cl.dcc.Groups_Organizer.data.EventListData;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 13-04-2014.
 */
public class PublicEvents extends ListFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AdminPreferences preferences;
    private EventAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            preferences = new AdminPreferences(getActivity());
        }
        catch(Exception e){
            Log.e("Error PublicEvents","Error al crear las prefercincias de administracion. " + e.getMessage());
        }
        List<Event> data = new ArrayList<Event>();

        try {
            mAdapter = new EventAdapter(getActivity(), R.layout.event_row, data);
            setListAdapter(mAdapter);
        }
        catch (Exception e){
            Log.e("Error PublicEvents","Error al crear los adaptadores para eventos. " + e.getMessage());
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(this.getActivity(), EventConfig_.class);
        Bundle extras = new Bundle();
        extras.putParcelable("Event", Parcels.wrap((Event) l.getItemAtPosition(position)));
        i.putExtras(extras);
        try {
            startActivity(i);
        }
        catch(Exception e){
            Log.e("Error PublicEvents","Error al iniciar la configuracion de eventos. " + e.getMessage());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        preferences.getPreferencias().registerOnSharedPreferenceChangeListener(this);
        onDataChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.getPreferencias().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void onDataChanged() {
        /* Cargamos la info */
        EventListData listData = preferences.getEventsSimpleList(AdminPreferences.PUBLIC_EVENTS);

        if (listData == null)
            return;

        mAdapter.getList().clear();
        mAdapter.getList().addAll(listData.getEventList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(AdminPreferences.PUBLIC_EVENTS))
            onDataChanged();
    }
}
