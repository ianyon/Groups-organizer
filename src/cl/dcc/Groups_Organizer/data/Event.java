package cl.dcc.Groups_Organizer.data;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ian on 14-04-2014.
 */
@Parcel
public class Event{
    public String name;
    public String description;
    public String location;
    public Date datetime;
    public List<Person> confirmed;
    public List<Person> guestList;
    private int confirmedCount;
    private int guestListCount;
    private int id;
    
    private static SimpleDateFormat datetimeFormatJson = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private String mAdmin;

    public Event(){}

    public Event(int id, String name, String description, String location, Date datetime) {
    	this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.datetime = datetime;
        this.confirmed = new ArrayList<Person>();
        this.guestList = new ArrayList<Person>();
    }

    public Event(int id, String name, String description, String location, Date datetime, ArrayList<Person> guestList) {
    	this(id, name, description, location, datetime);
        this.guestList = guestList;
    }

    public Event(int id, String name, String description, String location, Date datetime, ArrayList<Person> confirmed, ArrayList<Person> guestList) {
    	this(id, name, description, location, datetime, guestList);
        this.confirmed = confirmed;
    }

    public Event(JSONObject jsonEvent) throws JSONException {
    	id = jsonEvent.getInt("id");
        name = jsonEvent.getString("name");
        description = jsonEvent.optString("description","");
        location = jsonEvent.optString("location","");
        if(jsonEvent.has("datetime"))
			try {
				datetime = datetimeFormatJson.parse(jsonEvent.getString("datetime"));
			} catch (ParseException e) {
				datetime = null;
			}
		else
        	datetime = null;

        try {
            mAdmin = jsonEvent.getString("creator");
        }
        catch (Exception e){
            mAdmin = "notAdmin";
        }
        guestList = new ArrayList<Person>();
        confirmed = new ArrayList<Person>();
        
        if(jsonEvent.has("guests")) {
        	JSONArray guests = jsonEvent.getJSONArray("guests");
        	for(int i = 0; i < guests.length(); i++) {
        		JSONObject entry = guests.getJSONObject(i);
        		Person person = new Person(entry.getString("user_id"));
        		guestList.add(person);
        		if(entry.getBoolean("confirmed")) {
        			confirmed.add(person);
        		}
        	}
        	confirmedCount = confirmed.size();
        	guestListCount = guestList.size();
        } else {
        	confirmedCount = jsonEvent.optInt("confirmedCount",0);
        	guestListCount = jsonEvent.optInt("guestListCount",0);
        }
    }
    
    @Override
    public String toString() {
    	JSONObject jsonObject = new JSONObject();
    	try {
    		jsonObject.put("id", id);
    		jsonObject.put("name", name);
    		jsonObject.put("description", description);
    		jsonObject.put("location", location);
    		if(datetime != null) {
	    		jsonObject.put("datetime", datetimeFormatJson.format(datetime));
    		}
    		JSONArray guests = new JSONArray();
    		for(Person person : guestList) {
    			JSONObject entry = new JSONObject();
    			entry.put("user_id", person.getUsername());
    			entry.put("confirmed", confirmed.indexOf(person.getUsername()) == -1 ? "0" : "1");
    			guests.put(entry);
    		}
    		jsonObject.put("guests", guests);
    	} catch(JSONException e) {
    		
    	}
    	return jsonObject.toString();
    }

    public Event(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));

        Log.w("Json event", jsonString);
    }

    public int getGuestCount() {
        return guestListCount;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getTimeDate() {
        if (datetime == null)
            return "";
        return datetime.toString();}
    public List<Person> getConfirmed() {
        return confirmed;
    }

    public List<Person> getGuestList() {
        return guestList;
    }

	public int getId() {
		return id;
	}

    public boolean isAdmin(Person aPerson) {
        return mAdmin.equals(aPerson.getUsername());
    }
}