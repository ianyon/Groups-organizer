package cl.dcc.Groups_Organizer.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import cl.dcc.Groups_Organizer.controller.GroupAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import cl.dcc.Groups_Organizer.data.Group;
import cl.dcc.Groups_Organizer.data.GroupListData;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 13-04-2014.
 */
public class MyGroups extends ListFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AdminPreferences preferences;
    private GroupAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            preferences = new AdminPreferences(getActivity());
        }
        catch (Exception e){
            Log.e("Error MyGroups","Fail to create admin preference. " + e.getMessage());
        }
        List<Group> data = new ArrayList<Group>();

        try {
            mAdapter = new GroupAdapter(getActivity(), data);
            setListAdapter(mAdapter);
        }
        catch(Exception e){
            Log.e("Error MyGroup","Fail to create admin preference. " + e.getMessage());
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(this.getActivity(), GroupConfig_.class);
        Bundle extras = new Bundle();
        extras.putParcelable("Group", Parcels.wrap((Group) l.getItemAtPosition(position)));
        i.putExtras(extras);
        try {
            startActivity(i);
        }
        catch(Exception e){
            Log.e("Error MyGroups","Falla al momento de iniciar la cofiguracion del grupo. " + e.getMessage());
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
        GroupListData listData = preferences.getGroupsSimpleList(AdminPreferences.PRIVATE_GROUPS);

        if (listData == null)
            return;
		setListAdapter(new GroupAdapter(this.getActivity(), listData.getGroupList()));
//        mAdapter.getList().clear();
//        mAdapter.getList().addAll(listData.getGroupList());
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(AdminPreferences.PRIVATE_GROUPS))
            onDataChanged();
    }
}
