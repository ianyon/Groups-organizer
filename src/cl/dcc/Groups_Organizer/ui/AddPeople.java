package cl.dcc.Groups_Organizer.ui;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
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
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Ian on 15-05-2014.
 */
@EActivity(R.layout.add_people)
public class AddPeople extends CustomFragmentActivity {

	@ViewById(R.id.radioGroup)
	RadioGroup radioGroup;

	@ViewById(R.id.radioPeople)
	RadioButton radioPeople;

	@ViewById(R.id.listView)
	ListView listView;

	boolean showingPeople = true;

	ArrayList<Person> peopleList;
	ArrayList<Group> groupsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		peopleList = new ArrayList<Person>();
		groupsList = new ArrayList<Group>();
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
	}

	@Override
	public void onStart() {
		super.onStart();
		refresh();

	}

	private void onDataChanged() {

		if(showingPeople) {
            /* Cargamos la info */
			listView.setAdapter(
					new PersonAdapter(this, peopleList));

		} else {
			listView.setAdapter(
					new GroupAdapter(this, groupsList));
		}
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