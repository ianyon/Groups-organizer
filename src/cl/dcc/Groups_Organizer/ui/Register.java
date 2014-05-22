package cl.dcc.Groups_Organizer.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.Person;
import org.parceler.Parcels;

/**
 * Created by Roberto
 */
public class Register extends Activity implements AdapterView.OnItemSelectedListener {

    private Person mPerson;
    EditText mUserName,
            mUserAge,
            mUserMail,
            mUserConfMail,
            mUserPass,
            mUserConfPass;
    Spinner mGenderSpiner;
    String mGender = "";

    Button mOkButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //create the link with register.xml
        setContentView(R.layout.register);

        Bundle extras = this.getIntent().getExtras();
        mSetup();

        if(extras != null && extras.containsKey("Person")){
            mPerson = Parcels.unwrap(extras.getParcelable("Person"));
            mOkButton.setText("Guardar");

            mUserName.setText(mPerson.getName());
            mUserAge.setText(Integer.toString(mPerson.getAge()));
            mUserMail.setText(mPerson.getEmail());
            mUserConfMail.setText(mPerson.getEmail()) ;
            mUserPass.setText(mPerson.getPassword());
            mUserConfPass.setText(mPerson.getPassword());

        }
    }

    private void mSetup(){
        mUserName = (EditText) findViewById(R.id.registerName);
        mUserAge = (EditText) findViewById(R.id.registerAge);
        mUserMail = (EditText) findViewById(R.id.registermail);
        mUserConfMail = (EditText) findViewById(R.id.registerCpnfirmMail);
        mUserPass = (EditText) findViewById(R.id.registerPass);
        mUserConfPass = (EditText) findViewById(R.id.registerConfirmPass);

        mGenderSpiner = (Spinner) findViewById(R.id.registergender);
        mGenderSpiner.setOnItemSelectedListener(this);

        mOkButton = (Button) findViewById(R.id.registerButton);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void okInfo(View view){

        boolean equalMail = mUserConfMail.equals(mUserMail);
        boolean equalPass = mUserConfPass.equals(mUserPass);
        boolean notEmptyName = mUserName.length() > 0;
        boolean notEmptyAge = mUserAge.length() > 0;
        boolean notEmptyMail = mUserMail.length() > 0;
        boolean notEmptyPass = mUserPass.length() > 5;
        boolean hasSelectGender = !mGender.equals("");

        if (equalMail &&
                equalPass &&
                notEmptyName &&
                notEmptyAge &&
                notEmptyMail &&
                notEmptyPass &&
                hasSelectGender)
            showRegisterWarning("Register OK");
        else {
            if (!equalMail)
                showRegisterWarning("E-mail don't match.");
            if (!equalPass)
                showRegisterWarning("Password don't match.");
            if (!notEmptyName)
                showRegisterWarning("Please enter a name.");
            if (!notEmptyAge)
                showRegisterWarning("Please enter age.");
            if(!notEmptyPass)
                showRegisterWarning("Please enter a password.");
            if(!notEmptyMail)
                showRegisterWarning("Please enter a valid mail.");
            if(!hasSelectGender)
                showRegisterWarning("Please select a gender");
        }
    }

    private void showRegisterWarning(CharSequence text){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        assert context != null;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = (String)parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
