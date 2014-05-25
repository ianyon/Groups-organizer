package cl.dcc.Groups_Organizer.ui;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;

public class DefaultValidationListener implements ValidationListener {
	private Context context;
	public DefaultValidationListener(Context context) {
		this.context = context;
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

	}
	
	private void showRegisterWarning(CharSequence text) {
        int duration = Toast.LENGTH_SHORT;

        assert context != null;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
