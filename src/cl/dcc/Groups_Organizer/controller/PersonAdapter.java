package cl.dcc.Groups_Organizer.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.Person;

import java.util.List;

/**
 * Created by Ian on 23-05-2014.
 */
public class PersonAdapter extends ArrayAdapter<Person> {
        // TODO: Hacer clase. Está copiada de EventAdapter
        Context context;
		private final static int layoutResourceId = R.layout.person_row;
        List<Person> data = null;

        public PersonAdapter(Context context, List<Person> data) {
            super(context, layoutResourceId, data);
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            EventHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new EventHolder();
                holder.name = (TextView) row.findViewById(R.id.personRowTextPersonName);

                row.setTag(holder);
            } else {
                holder = (EventHolder) row.getTag();
            }

            Person person = data.get(position);

            holder.name.setText(person.getUsername());

            return row;
        }

        static class EventHolder {
            TextView name;
        }

        public List<Person> getList(){
            return data;
        }

    }
