package cl.dcc.Groups_Organizer.connection;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Noone on 25-05-2014.
 */
public class ConfirmConn extends HttpConnection{

    private static final String mUrl = "confirm_invitation.php";

    public ConfirmConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(int eventId,int going){
        return new RequestParams( "event_id",eventId, "confirm", going);
    }
}
