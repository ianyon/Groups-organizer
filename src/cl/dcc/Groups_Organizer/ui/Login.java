package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.LoginConn;
import cl.dcc.Groups_Organizer.data.Person;
import cl.dcc.Groups_Organizer.utilities.LoadingThing;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.parceler.Parcels;

@EActivity(R.layout.main)
public class Login extends CustomFragmentActivity {
	
	@TextRule(order = 1, minLength = 5, maxLength = 30, messageResId = R.string.registerUserVerification)
	@ViewById(R.id.user)
    TextView tvUser;
	
	@Password(order = 2)
	@TextRule(order = 3, minLength = 5, messageResId = R.string.registerPassVerification)
	@ViewById(R.id.pass)
    TextView tvPassword;

    private LoadingThing myLoadingMsg;

	private Validator validator;
	private ValidationListener validationListener = new DefaultValidationListener(this) {
		@Override
		public void onValidationSucceeded() {
            myLoadingMsg.stratPopUp();
            LoginConn loginConn = new LoginConn(getHttpClient());
	        RequestParams reqParams = loginConn.generateParams(tvUser.getText(), tvPassword.getText());
	        loginConn.go(reqParams, httpHandler);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

        Log.w("hola", "chao");
		super.onCreate(savedInstanceState);
		validator = new Validator(this);
		validator.setValidationListener(validationListener);

        myLoadingMsg = new LoadingThing(Login.this,"","Conecting");


    }


    @Override
    protected void onPause() {
        super.onPause();
        myLoadingMsg.stopPopUp();
    }

    @Click(R.id.signup)
	public void onSignupClick(View v){
        startActivity(new Intent(this,Register_.class));
    }

	@Click(R.id.login)
    public void onLoginClick() { validator.validate();  }

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
            myLoadingMsg.stopPopUp();
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
                myLoadingMsg.stopPopUp();
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
