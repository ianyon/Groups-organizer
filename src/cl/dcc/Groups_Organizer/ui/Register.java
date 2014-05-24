package cl.dcc.Groups_Organizer.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.RegisterConn;
import cl.dcc.Groups_Organizer.data.Person;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mobsandgeeks.saripaar.Rule;
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
public class Register extends CustomFragmentActivity implements ValidationListener {

    @TextRule(order = 1, minLength = 5, maxLength = 60, messageResId = R.string.registerNameVerification)
    @ViewById(R.id.registerName)
    EditText mUserName;

    @NumberRule(order = 2, type = NumberType.INTEGER, gt = 1, lt = 120, messageResId = R.string.registerAgeVerification)
    @ViewById(R.id.registerAge)
    EditText mUserAge;

    @Select(order = 3)
    @ViewById
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null && extras.containsKey("Person")) {
            mPerson = Parcels.unwrap(extras.getParcelable("Person"));
        }
    }

    @AfterViews
    public void loadPersonInfo() {
        if (mPerson != null) {
            mUserName.setText(mPerson.getName());
            mUserMail.setText(mPerson.getEmail());
            mUserUsername.setText(mPerson.getName());
            mUserAge.setText(Integer.toString(mPerson.getAge()));
            mUserPass.setText(mPerson.getPassword());
            mOkButton.setText("Guardar Cambios");
        }
    }

    @Click
    void registerButton() {
        validator.validate();
    }

    @Override
    public void onValidationFailed(View view, Rule<?> rule) {
        String message = rule.getFailureMessage();
        if (view instanceof EditText) {
            view.requestFocus();
            ((EditText) view).setError(message);
        } else {
            showRegisterWarning(message);
        }

    }

    @Override
    public void onValidationSucceeded() {
        // Obtain gender position
        String pos = "" + (registerGender.getSelectedItemPosition() - 1);
        // Connect to server and register new user
        RegisterConn registerConn = new RegisterConn(getHttpClient());
        RequestParams requestParams = registerConn.generateParams(mUserName.getText(), mUserAge.getText(), pos
                , mUserUsername.getText(),
                mUserMail.getText(), mUserPass.getText());

        registerConn.go(requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(Register.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode == 200 && responseString.trim().equals("OK")) {
                    Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Register.this, "Error when connecting to the server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showRegisterWarning(CharSequence text) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        assert context != null;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
