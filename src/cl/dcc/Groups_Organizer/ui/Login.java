package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.LoginConn;
import cl.dcc.Groups_Organizer.data.Event;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.parceler.Parcels;

public class Login extends CustomFragmentActivity {
    private TextView tvUser, tvPassword;

    // Object for Handling the http response
    private TextHttpResponseHandler httpHandler = new TextHttpResponseHandler(){
        private String message, name;

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString,
                              Throwable throwable) {
            Toast.makeText(Login.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            if (statusCode != 200 || !parseResponse(responseBody) || !message.equals("OK")) {
                Toast.makeText(Login.this, getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(Login.this, getString(R.string.loginSuccessfull), Toast.LENGTH_SHORT).show();

            doLoginVerified(name);
        }

    private boolean parseResponse(String body) {
        String[] response = body.trim().split("\n");

        if (response.length != 2)
            return false;

        message = response[0];
        name = response[1];

        return true;
    }
};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tvUser = (TextView) findViewById(R.id.user);

        tvPassword = (TextView) findViewById(R.id.pass);
    }

    public void onLoginClick(View v) {
        if (true) {
            // TODO: Borrar, Fake autentication
            doLoginVerified("Juan Valdes");
            return;
        }
        if (tvUser.getText().length() == 0 || tvPassword.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
            return;
        }

        LoginConn loginConn = new LoginConn(getHttpClient());
        RequestParams reqParams = loginConn.generateParams(tvUser.getText(), tvPassword.getText());
        loginConn.go(reqParams, httpHandler);
    }

    private void doLoginVerified(String s) {
        PagerViewHost_.intent(this).mName(s).start();
    }
}
