package cl.dcc.Groups_Organizer.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.RegisterConn;
import cl.dcc.Groups_Organizer.data.Person;
import cl.dcc.Groups_Organizer.utilities.LoadingThing;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.*;
import com.mobsandgeeks.saripaar.annotation.NumberRule.NumberType;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.parceler.Parcels;

/**
 * Created by Roberto
 */
@EActivity(R.layout.register)
public class Register extends CustomFragmentActivity {

    @TextRule(order = 1, minLength = 5, maxLength = 60, messageResId = R.string.registerNameVerification)
    @ViewById(R.id.registerName)
    EditText mUserName;

    @NumberRule(order = 2, type = NumberType.INTEGER, gt = 1, lt = 120, messageResId = R.string.registerAgeVerification)
    @ViewById(R.id.registerAge)
    EditText mUserAge;

    @Select(order = 3, message = "Select a Gender.")
    @ViewById(R.id.registerGender)
    Spinner registerGender;

    @TextRule(order = 4, minLength = 5, maxLength = 30, messageResId = R.string.registerUserVerification)
    @ViewById(R.id.registerUser)
    EditText mUserUsername;

    @Required(order = 5)
    @Email(order = 6)
    @TextRule(order = 7, maxLength = 60, messageResId = R.string.registerEmailVerification)
    @ViewById(R.id.registerMail)
    EditText mUserMail;

    @Password(order = 8)
    @TextRule(order = 9, minLength = 5, messageResId = R.string.registerPassVerification)
    @ViewById(R.id.registerPass)
    EditText mUserPass;

    @ConfirmPassword(order = 10)
    @ViewById(R.id.registerConfirmPass)
    EditText mUserConfPass;

    @ViewById(R.id.registerButton)
    Button mOkButton;

    Validator validator;
    private Person mPerson;
    private LoadingThing loadingMsg;
    
    private ValidationListener validationListener = new DefaultValidationListener(this) {

    	@Override
    	public void onValidationSucceeded() {
    		// Obtain gender position
            String pos = "" + (registerGender.getSelectedItemPosition() - 1);
            // Connect to server and register new user

            loadingMsg.stratPopUp();

            RegisterConn registerConn = new RegisterConn(getHttpClient());
            RequestParams requestParams = registerConn.generateParams(mUserName.getText(), mUserAge.getText(), pos
                    , mUserUsername.getText(),
                    mUserMail.getText(), mUserPass.getText());


            registerConn.go(requestParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                loadingMsg.stopPopUp();
                    Toast.makeText(Register.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (statusCode == 200 && responseString.trim().equals("OK")) {
                        Toast.makeText(getApplicationContext(), "User is regitered", Toast.LENGTH_SHORT).show();
                    loadingMsg.stopPopUp();
                        finish();
                    } else {
                    loadingMsg.stopPopUp();
                        Toast.makeText(Register.this, "Error when connecting to the server", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    	}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(validationListener);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null && extras.containsKey("User")) {
            mPerson = Parcels.unwrap(extras.getParcelable("User"));
        }
        loadingMsg = new LoadingThing(Register.this);
    }

    @AfterViews
    public void loadPersonInfo() {
        if (mPerson != null) {
            mUserName.setText(mPerson.getName());
            mUserMail.setText(mPerson.getEmail());
            mUserUsername.setText(mPerson.getUsername());
            mUserUsername.setKeyListener(null);
            mUserAge.setText(Integer.toString(mPerson.getAge()));
            try {
                registerGender.setSelection((Integer.parseInt(mPerson.getGender()) + 1));
            }
            catch(Exception e){
                Toast.makeText(Register.this, "Error Gender, yours " + mPerson.getGender(), Toast.LENGTH_SHORT).show();
                registerGender.setSelection(0);
            }

            mOkButton.setText("Guardar Cambios");
        }
    }

    @Click
    void registerButton() {
        validator.validate();
    }

}
