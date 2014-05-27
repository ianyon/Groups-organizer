package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ian on 22-05-2014.
 */
public class GetPersonListConn extends HttpConnection{
    private static final String mUrl = "list_users.php";

    public GetPersonListConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

	public RequestParams generateParams() {
		return new RequestParams();
	}

    public RequestParams generateParams(String groupName) {
        return new RequestParams("group_name", groupName);
    }
}
