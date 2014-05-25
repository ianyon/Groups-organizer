package cl.dcc.Groups_Organizer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.SearchGroupsConn;
import cl.dcc.Groups_Organizer.connection.SearchPeopleConn;
import cl.dcc.Groups_Organizer.data.Event;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ian on 15-05-2014.
 */

@EActivity(R.layout.add_people)
public class AddPeople extends CustomFragmentActivity {

    @ViewById
    MultiAutoCompleteTextView multiAutoCompleteSearch;

    @ViewById
    ImageButton buttonSearch;

    @ViewById
    RadioGroup radioGroupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people);
    }

    public void onSearch(View v) {
        if (radioGroupType.getCheckedRadioButtonId() == R.id.radioPeople) {
            SearchPeopleConn searchPeopleConn = new SearchPeopleConn(getHttpClient());
            searchPeopleConn.go(null, new JsonHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    String message = "Error when connecting to the server";
                    if("User NOT FOUND".equals(responseString)) {
                        message = "Event not found";
                    } else if("ERROR".equals(responseString)) {
                        message = "Error in server";
                    }
                    Toast.makeText(AddPeople.this, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode != 200 ) {
                        Toast.makeText(AddPeople.this, "Error al traer la información de usuarios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // TODO: implementar

                    super.onSuccess(statusCode, headers, response);
                }
            });
        } else {
            SearchGroupsConn searchPeopleConn = new SearchGroupsConn(getHttpClient());
            searchPeopleConn.go(null, new JsonHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    String message = "Error when connecting to the server";
                    if("User NOT FOUND".equals(responseString)) {
                        message = "Event not found";
                    } else if("ERROR".equals(responseString)) {
                        message = "Error in server";
                    }
                    Toast.makeText(AddPeople.this, message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode != 200 ) {
                        Toast.makeText(AddPeople.this, "Error al traer la información de usuarios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // TODO: implementar

                    super.onSuccess(statusCode, headers, response);
                }
            });

        }
    }


}