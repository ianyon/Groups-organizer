package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Gonzalo on 08-05-2014.
 */
public class GetEventListConn extends HttpConnection {

    private static final String mUrl = "list_events.php";

    public GetEventListConn(AsyncHttpClient client) { super(client); }

    public RequestParams generateParams(boolean onlyPrivate) {
        return new RequestParams("only_invited_to", onlyPrivate?"1":"0");
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }
}
