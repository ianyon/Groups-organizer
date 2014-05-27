package cl.dcc.Groups_Organizer.connection;

import android.text.Editable;
import cl.dcc.Groups_Organizer.data.Person;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noone on 25-05-2014.
 */
public class UpdateEventConn extends HttpConnection {

    private static final String mUrl = "edit_event.php";
    public UpdateEventConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(int eventId,Editable eventName, Editable description, Editable location, Editable dateTime, List<Person> guestList){
        ArrayList<String> userIds = new ArrayList<String>();
        for (Person person : guestList) {
            userIds.add(person.getUsername());
        }
        String json = new Gson().toJson(userIds );

        return new RequestParams("id", eventId, "name", eventName, "description", description,"location", location, "datetime", dateTime,
                "invited_people", json);
    }
}
