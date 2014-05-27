package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ian on 22-05-2014.
 */
public class GetGroupInfoConn extends HttpConnection{
    private static final String mUrl = "group_info.php";

    public GetGroupInfoConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(String id) {
        return new RequestParams("group_id", id);
    }
}
