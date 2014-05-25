package cl.dcc.Groups_Organizer.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import android.util.Log;

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
    public Person(JSONObject jsonPerson){
    	username = "";
        name = "";
        gender = "";
        email = "";
        age = -1;

        try {
        	username = jsonPerson.getString("user_name"); 
            name = jsonPerson.getString("name");
            gender = jsonPerson.getString("gender");
            email = jsonPerson.getString("email");
            age = jsonPerson.getInt("age");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON","JSON EXCEPTION: "+e);
        }
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
			e.printStackTrace();
		}
    	return "{}";
    }
    
    public Person(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
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