package cl.dcc.Groups_Organizer.connection;

import android.text.Editable;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Created by Gonzalo on 29-04-2014.
 */
public class RegisterConn extends HttpConnection {

    private static final String mUrl = "register_user.php";

    public RegisterConn(AsyncHttpClient client) {
        super(client);
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    public RequestParams generateParams(Editable name, Editable age, String gender, Editable user,
                                        Editable email, Editable password) {
        return new RequestParams(   "name", name, "age", age, "gender", gender, "user", user,
                                    "email", email, "pass", password);
    }

}
