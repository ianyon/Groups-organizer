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

    public void addPeopleToList(List<Person> newOnes){
        data.addAll(newOnes);
    }

    }

