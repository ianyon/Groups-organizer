package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ian on 22-05-2014.
 */
public class GetGroupListConn extends HttpConnection{
    private static final String mUrl = "list_groups.php";

    public GetGroupListConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(boolean groupsIBelongTo) {
        return new RequestParams("only_member_of", groupsIBelongTo ? "1" : "0");
    }
}
