package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ian on 22-05-2014.
 */
public class GetGuestListConn extends HttpConnection{
    private static final String mUrl = "login.php";

    public GetGuestListConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(CharSequence user, CharSequence eventName) {
        return new RequestParams("user", user, "eventName", eventName);
    }
}
