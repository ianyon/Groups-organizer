package cl.dcc.Groups_Organizer.connection;

import cl.dcc.Groups_Organizer.data.Person;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gonzalo on 29-04-2014.
 */
public class EditGroupConn extends HttpConnection {

    private static final String mUrl = "add_group.php";

    public EditGroupConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(String name, String description, List<Person> memberList) {
        // Convert the array of Person to an array of username of guests
        ArrayList<String> userIds = new ArrayList<String>();
        for (Person person : memberList) {
            userIds.add(person.getUsername());
        }
        String json = new Gson().toJson(userIds );

        return new RequestParams("name", name, "description", description, "users", json);
    }

}
