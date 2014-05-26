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
import cl.dcc.Groups_Organizer.data.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 23-05-2014.
 */
public class PersonAdapter extends ArrayAdapter<Person> implements Filterable {
    Context context;
	private final static int layoutResourceId = R.layout.person_row;
    List<Person> data = null;
	List<Person> originalData = null;
	private PersonFilter personFilter;

    public PersonAdapter(Context context, List<Person> data) {
        super(context, layoutResourceId, data);
        this.context = context;
	    this.originalData = new ArrayList<Person>();
	    this.originalData.addAll(data);
	    this.data = new ArrayList<Person>();
	    this.data.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PersonHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PersonHolder();
            holder.name = (TextView) row.findViewById(R.id.personRowTextPersonName);

            row.setTag(holder);
        } else {
            holder = (PersonHolder) row.getTag();
        }

        Person person = data.get(position);

        holder.name.setText(person.getUsername());

        return row;
    }

    static class PersonHolder {
        TextView name;
    }

    public List<Person> getList(){
        return data;
    }

	public Filter getFilter() {
		if(personFilter == null) {
			personFilter = new PersonFilter();
		}
		return personFilter;
	}

	public class PersonFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if(constraint == null || constraint.length() == 0) {
				synchronized (this) {
					filterResults.values = originalData;
					filterResults.count = originalData.size();
				}
			} else {
				List<Person> filtered = new ArrayList<Person>();
				for(Person p : originalData) {
					if(p.getUsername().contains(constraint)) {
						filtered.add(p);
					}
				}
				filterResults.values = filtered;
				filterResults.count = filtered.size();
			}
			return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			data = (ArrayList<Person>)results.values;
			notifyDataSetChanged();
			clear();
			addAll(data);
			notifyDataSetInvalidated();
		}
	}
}

