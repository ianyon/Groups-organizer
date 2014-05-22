package cl.dcc.Groups_Organizer.ui;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.NumberRule.NumberType;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

/**
 * Created by Roberto
 */
@EActivity(R.layout.register)
public class Register extends Activity implements ValidationListener {
	
	@TextRule(order = 1, minLength = 5, maxLength = 60, messageResId = R.string.registerNameVerification)
	@ViewById(R.id.registerName)
    EditText mUserName;
	
	@NumberRule(order = 2, type = NumberType.INTEGER, gt = 1, lt = 120, messageResId = R.string.registerAgeVerification)
	@ViewById(R.id.registerAge)
    EditText mUserAge;
	
	@TextRule(order = 3, minLength = 5, maxLength = 30, messageResId = R.string.registerUserVerification)
	@ViewById(R.id.registerUser)
    EditText mUserUsername;
	
	@Required(order = 4)
	@Email(order = 5)
	@TextRule(order = 6, maxLength = 60, messageResId = R.string.registerEmailVerification)
	@ViewById(R.id.registerMail)
    EditText mUserMail;
	
	@Password(order = 7)
	@TextRule(order = 8, minLength = 5, messageResId = R.string.registerPassVerification)
	@ViewById(R.id.registerPass)
    EditText mUserPass;
	
	@ConfirmPassword(order = 9)
	@ViewById(R.id.registerConfirmPass)
    EditText mUserConfPass;
	
	Validator validator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		validator = new Validator(this);
		validator.setValidationListener(this);
	}

	@Click
    void registerButton(){
		validator.validate();
    }
	
	@Override
	public void onValidationFailed(View view, Rule<?> rule) {
		String message = rule.getFailureMessage();
		if(view instanceof EditText) {
			view.requestFocus();
			((EditText)view).setError(message);
		} else {
			showRegisterWarning(message);
		}
		
	}
	
	@Override
	public void onValidationSucceeded() {
		// Connect to server and register new user
	}

    private void showRegisterWarning(CharSequence text){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        assert context != null;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
