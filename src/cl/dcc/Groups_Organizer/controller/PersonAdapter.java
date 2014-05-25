package cl.dcc.Groups_Organizer.controller;

import android.app.Activity;
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
        int layoutResourceId;
        List<Person> data = null;

        public PersonAdapter(Context context, int layoutResourceId, List<Person> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            PersonHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new PersonHolder();
                holder.name = (TextView) row.findViewById(android.R.id.text1);

                row.setTag(holder);
            } else {
                holder = (PersonHolder) row.getTag();
            }

            Person person = data.get(position);

            holder.name.setText(person.name);

            return row;
        }

        static class PersonHolder {
            TextView name;
        }

        public List<Person> getList(){
            return data;
        }

    }
