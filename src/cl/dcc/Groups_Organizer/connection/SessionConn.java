package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Gonzalo on 29-04-2014.
 */
public class SessionConn extends HttpConnection {

	private static final String mUrl = "login.php";

	public SessionConn(AsyncHttpClient client) {
		super(client);
	}

	@Override
	protected String getUrl() {
		return mUrl;
	}

	public RequestParams generateParams(CharSequence user, CharSequence pass) {
        return new RequestParams("user", user, "pass", pass);
    }

}
