package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.LoginConn;
import cl.dcc.Groups_Organizer.data.Person;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.parceler.Parcels;

public class Login extends CustomFragmentActivity {
    private TextView tvUser, tvPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tvUser = (TextView) findViewById(R.id.user);

        tvPassword = (TextView) findViewById(R.id.pass);
    }

    public void onSignupClick(View v){
        startActivity(new Intent(this,Register_.class));
    }
    public void onLoginClick(View v) {
        if (false) {
            // TODO: Borrar, Fake autentication
            doLoginVerified(new Person("Juan Valdes", "el_cafetero_mas_loco@gmail.com"));
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

    private void doLoginVerified(Person user) {
        Intent intent = PagerViewHost_.intent(this).get();
        Bundle extras = new Bundle();
        extras.putParcelable("User", Parcels.wrap(user));
        intent.putExtras(extras);
        startActivity(intent);
    }

    // Object for Handling the http response
    private TextHttpResponseHandler httpHandler = new TextHttpResponseHandler() {
        private String message, name;

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString,
                              Throwable throwable) {
            Toast.makeText(Login.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            // TODO: hacer mas verboso el control de errores
            if (statusCode == 200 && parseResponse(responseBody) && message.equals("OK")) {
                Toast.makeText(Login.this, getString(R.string.loginSuccessfull), Toast.LENGTH_SHORT).show();

                Person user = new Person(name, "" + tvUser.getText());

                // Clear the textviews
                tvUser.setText("");
                tvPassword.setText("");

                doLoginVerified(user);
            }else{
                Toast.makeText(Login.this, getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
            }
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
}
