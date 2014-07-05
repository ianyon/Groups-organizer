package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.ConnectionStatus;
import cl.dcc.Groups_Organizer.connection.GetGroupListConn;
import cl.dcc.Groups_Organizer.connection.GetPersonListConn;
import cl.dcc.Groups_Organizer.controller.GroupAdapter;
import cl.dcc.Groups_Organizer.controller.PersonAdapter;
import cl.dcc.Groups_Organizer.data.Group;
import cl.dcc.Groups_Organizer.data.Person;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.androidannotations.annotations.*;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.add_people)
public class AddPeople extends CustomFragmentActivity {

	@ViewById(R.id.addPeopleEditTextSearch)
	EditText searchEditText;

	@ViewById(R.id.radioGroupType)
	RadioGroup radioGroup;

	@ViewById(R.id.radioPeople)
    RadioButton radioPeople;

	@ViewById(R.id.listView)
	ListView listView;

	private boolean showingPeople = true;

	ArrayList<Person> peopleList, peopleSelected;
	ArrayList<Group> groupsList, groupSelected;
    GroupAdapter groupAdapter;
    PersonAdapter personAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		peopleList = new ArrayList<Person>();
		groupsList = new ArrayList<Group>();
        peopleSelected = new ArrayList<Person>();
        groupSelected = new ArrayList<Group>();
		groupAdapter = new GroupAdapter(this, groupsList);
		personAdapter = new PersonAdapter(this, peopleList);

	}

	@AfterViews
	public void init() {
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                showingPeople = radioPeople.isChecked();
                onDataChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (showingPeople) {
                    //Toast.makeText(AddPeople.this, peopleList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                    peopleSelected.add(peopleList.get(position));
                } else {
                    //Toast.makeText(AddPeople.this, groupsList.get(position).getName(), Toast.LENGTH_SHORT).show();
                    groupSelected.add(groupsList.get(position));
                }
            }
        });
	}

	@Override
	public void onStart() {//Todo refresh en on resume
		super.onStart();
		refresh();

	}


    public void onClickFinish(View v){

        Intent returnIntent = new Intent();

        if(!peopleSelected.isEmpty())
            returnIntent.putExtra("People" , Parcels.wrap(peopleSelected));
		Object o = Parcels.unwrap(returnIntent.getExtras().getParcelable("People"));
	    System.out.println(o.toString());
        if(!groupSelected.isEmpty())
            returnIntent.putExtra("Group", Parcels.wrap(groupSelected));

        setResult(RESULT_OK,returnIntent);
        Toast.makeText(this, "Returning selected list.", Toast.LENGTH_SHORT).show();
        finish();
    }

	@TextChange(R.id.addPeopleEditTextSearch)
	public void onTextChange(TextView tv, CharSequence text) {
		groupAdapter.getFilter().filter(text.toString());
		personAdapter.getFilter().filter(text.toString());
	}

	private void onDataChanged() {
		if(showingPeople) {
			personAdapter = new PersonAdapter(this, new ArrayList<Person>(peopleList));
            /* Cargamos la info */
			listView.setAdapter(
					personAdapter);

		} else {
			groupAdapter = new GroupAdapter(this, new ArrayList<Group>(groupsList));
			listView.setAdapter(
					groupAdapter);
		}
		onTextChange(null, searchEditText.getText().toString());
	}

	private void refresh() {

		if (!ConnectionStatus.isOnline(this)) {
			Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
			return;
		}

		// Connection for people list
		GetPersonListConn peopleListConn = new GetPersonListConn(getHttpClient());
		RequestParams params = peopleListConn.generateParams();
		peopleListConn.go(params, new PeopleListResponseHandler());


		// Connection for group list
		GetGroupListConn groupListConn = new GetGroupListConn(getHttpClient());
		params = groupListConn.generateParams(false);
		groupListConn.go(params, new GroupListResponseHandler());
	}

	private class MyResponseHandler extends JsonHttpResponseHandler {

		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			String message = "Error when connecting to the server";
			if("EVENT NOT FOUND".equals(responseString)) {
				message = "Event not found";
			} else if("ERROR".equals(responseString)) {
				message = "Error in server";
			}
			Toast.makeText(AddPeople.this, message, Toast.LENGTH_LONG).show();
		}
	}

	private class PeopleListResponseHandler extends MyResponseHandler {
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
			if (statusCode != 200 ) {
				Toast.makeText(AddPeople.this, "Error al traer la lista de personas", Toast.LENGTH_SHORT).show();
				return;
			}
			boolean couldParseAll = true;
			peopleList.clear();
			for(int i=0;i<response.length();i++) {
				try {
					peopleList.add(new Person(response.getString(i)));
				} catch (JSONException e) {
					e.printStackTrace();
					couldParseAll = false;
				}
			}
			if(!couldParseAll) {
				Toast.makeText(AddPeople.this, "Error al traer la lista de personas. Algunas personas no aparecerán", Toast.LENGTH_SHORT).show();
			}
			onDataChanged();
		}
	}

	private class GroupListResponseHandler extends MyResponseHandler {
		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
			if (statusCode != 200 ) {
				Toast.makeText(AddPeople.this, "Error al traer la lista de grupos", Toast.LENGTH_SHORT).show();
				return;
			}

			boolean couldParseAll = true;
			groupsList.clear();
			for(int i=0;i<response.length();i++) {
				try {
					groupsList.add(new Group(response.getJSONObject(i)));
				} catch (JSONException e) {
					e.printStackTrace();
					couldParseAll = false;
				}
			}
			onDataChanged();
		}
	}
}