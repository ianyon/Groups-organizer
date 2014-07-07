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
	public String username;
    public String name;
    public String gender;
    public String email;
    public int age;
    public String id;

    public Person(){}
    
    public Person(String username, String name) {
    	this.username = username;
        this.name = name;
    }

    public Person(String username, String name, String email) {
    	this(username, name);
        this.gender = "unknown";
        this.email = email;
        this.age = -1;
    }

    public Person(String username, String name, String gender, String email, int age) {
    	this(username, name, email);
    	this.gender = gender;
        this.age = age;
    }

    public Person(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public Person(JSONObject jsonPerson) throws JSONException {
        username = jsonPerson.getString("user_name");
        name = jsonPerson.optString("name");
        gender = jsonPerson.optString("gender");
        email = jsonPerson.optString("email");
        age = jsonPerson.optInt("age", -1);
    }

    @Override
    public String toString() {
    	JSONObject jsonObject = new JSONObject();
    	try {
			jsonObject.put("user_name", username);
			jsonObject.put("name", name);
			jsonObject.put("gender", gender);
			jsonObject.put("email", email);
			jsonObject.put("age", age);
			return jsonObject.toString();
		} catch (JSONException e) {
            Log.e("JSON", "Error imprimiendo persona");
		}
    	return "{}";
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonPerson = new JSONObject();

        jsonPerson.put("",username);
        jsonPerson.put("",name);
        jsonPerson.put("",gender);
        jsonPerson.put("",email);
        jsonPerson.put("",age);

        return jsonPerson;
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
    
    public String getUsername() {
    	return username;
    }
}