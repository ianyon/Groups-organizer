package cl.dcc.Groups_Organizer.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.Group;

import java.util.List;

/**
 * Created by Ian on 23-05-2014.
 */
public class GroupAdapter extends ArrayAdapter<Group> {
        // TODO: Hacer clase. Está copiada de EventAdapter
        Context context;
        List<Group> data = null;
		private final static int layoutResourceId = R.layout.group_row;

        public GroupAdapter(Context context, List<Group> data) {
	        super(context, layoutResourceId, data);
	        this.data = data;
	        this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            EventHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new EventHolder();
                holder.name = (TextView) row.findViewById(R.id.groupRowTextGroupName);
                holder.membersNumber = (TextView) row.findViewById(R.id.groupRowTextMembers);

                row.setTag(holder);
            } else {
                holder = (EventHolder) row.getTag();
            }

            Group group = data.get(position);

            holder.name.setText(group.getName());
            holder.membersNumber.setText(""+group.getMembersCount());

            return row;
        }

        static class EventHolder {
            TextView name, membersNumber;
        }

        public List<Group> getList(){
            return data;
        }

    }
