package cl.dcc.Groups_Organizer.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 23-05-2014.
 */
public class GroupAdapter extends ArrayAdapter<Group> implements Filterable {
    // TODO: Hacer clase. Está copiada de EventAdapter
    Context context;
    List<Group> data = null;
	List<Group> originalData = null;
	private final static int layoutResourceId = R.layout.group_row;
	private GroupFilter groupFilter;

    public GroupAdapter(Context context, List<Group> data) {
        super(context, layoutResourceId, data);
	    this.originalData = new ArrayList<Group>();
	    this.originalData.addAll(data);
	    this.data = new ArrayList<Group>();
	    this.data.addAll(data);
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

	@Override
	public Filter getFilter() {
		if(groupFilter == null) {
			groupFilter = new GroupFilter();
		}
		return groupFilter;
	}

	public class GroupFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if(constraint == null || constraint.length() == 0) {
				synchronized (this) {
					filterResults.values = new ArrayList<Group>(originalData);
					filterResults.count = originalData.size();
				}
			} else {
				List<Group> filtered = new ArrayList<Group>();
				for(Group g : originalData) {
					if(g.getName().contains(constraint)) {
						filtered.add(g);
					}
				}
				filterResults.values = filtered;
				filterResults.count = filtered.size();
			}
			return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			data = (ArrayList<Group>)results.values;
			notifyDataSetChanged();
			clear();
			addAll(data);
			notifyDataSetInvalidated();
		}
	}
}
