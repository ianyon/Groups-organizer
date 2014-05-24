package cl.dcc.Groups_Organizer.data;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Ian on 21-05-2014.
 */
@Parcel
public class Person {
    public String name;
    public String gender;
    public String email;
    public int age;

    public Person() {
    }

    public Person(String name, String email) {
        this.name = name;
        this.gender = "unknown";
        this.email = email;
        this.age = -1;
    }

    public Person(String name, String gender, String email, int age) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.age = age;
    }
    public Person(JSONObject jsonPerson){
        name = "";
        gender = "";
        email = "";
        age = -1;

        try {
            name = jsonPerson.getString("name");
            gender = jsonPerson.getString("gender");
            email = jsonPerson.getString("email");
            age = jsonPerson.getInt("age");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON","JSON EXCEPTION: "+e);
        }
    }
    public Person(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String toPreferences() {
        return name + ";" + email;
    }
}