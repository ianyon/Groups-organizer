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
import cl.dcc.Groups_Organizer.utilities.LoginBackOff;
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
    private LoginBackOff backOff;

	private Validator validator;
	private ValidationListener validationListener = new DefaultValidationListener(this) {
		@Override
		public void onValidationSucceeded() {
            myLoadingMsg.startPopUp();
            try {
                LoginConn loginConn = new LoginConn(getHttpClient());
                RequestParams reqParams = loginConn.generateParams(tvUser.getText(), tvPassword.getText());
                loginConn.go(reqParams, httpHandler);
            }
            catch (Exception e){
                Log.e("Error Login","Error al conectarse con el servidor. " + e.getMessage());
            }
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        try {
            validator = new Validator(this);
            validator.setValidationListener(validationListener);
        }
        catch (Exception e){
            Log.e("Error Login","Error al crear el validor. " + e.getMessage());
        }
        try {
            myLoadingMsg = new LoadingThing(Login.this, "", "Connecting");
        }
        catch (Exception e){
            Log.e("Error Login","Erroe al crear LoadingThing");
        }

        backOff = new LoginBackOff();

    }


    @Override
    protected void onPause() {
        super.onPause();
        myLoadingMsg.stopPopUp();
    }

    @Click(R.id.signup)
	public void onSignupClick(View v){
        try {
            startActivity(new Intent(this, Register_.class));
        }
        catch (Exception e)
        {
            Log.e("Error Login","Error al lanzar el registro. " + e.getMessage());
        }
    }

	@Click(R.id.login)
    public void onLoginClick() {
        if(backOff.canLogAgain())
            validator.validate();
        else
            Toast.makeText(Login.this, "Please try again in a few seconds.", Toast.LENGTH_SHORT).show();

    }

    private void doLoginVerified(Person user) {

        Intent intent = PagerViewHost_.intent(this).get();
        Bundle extras = new Bundle();
        extras.putParcelable("User", Parcels.wrap(user));
        intent.putExtras(extras);

        try {
            startActivity(intent);
        }
        catch(Exception e) {
            Log.e("Error en Login", "Activity fail to start. User name: " + user.getUsername() + " ." + e.getMessage());
            Toast.makeText(Login.this, "Please try again, if this error continues please contact the development team.", Toast.LENGTH_SHORT).show();
        }
    }



    // Object for Handling the http response
    private TextHttpResponseHandler httpHandler = new TextHttpResponseHandler() {
        private String message, name;

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString,
                              Throwable throwable) {
            myLoadingMsg.stopPopUp();
            Log.e("Error Login","Error al conectar al servidor. " + throwable.getMessage());
            Toast.makeText(Login.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            // TODO: hacer mas verboso el control de errores

            if (statusCode == 200 && parseResponse(responseBody) && message.equals("OK")) {
                backOff.success();
                Toast.makeText(Login.this, getString(R.string.loginSuccessfull), Toast.LENGTH_SHORT).show();

                Person user = new Person("" + tvUser.getText(),name);

                // Clear the textviews
                tvUser.setText("");
                tvPassword.setText("");

                doLoginVerified(user);
            }else{
                myLoadingMsg.stopPopUp();
                Toast.makeText(Login.this, getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
                backOff.oneTry();
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
