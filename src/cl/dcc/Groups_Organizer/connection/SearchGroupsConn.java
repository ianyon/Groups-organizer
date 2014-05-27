package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Ian on 22-05-2014.
 */
public class SearchGroupsConn extends HttpConnection{
    private static final String mUrl = "list_groups.php";

    public SearchGroupsConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(int search_text) {
        return new RequestParams("search_text", search_text);
    }
}
