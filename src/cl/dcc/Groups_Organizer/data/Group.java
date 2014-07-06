package cl.dcc.Groups_Organizer.data;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 21-05-2014.
 */
@Parcel
public class Group {
    public String name, description;
    public List<Person> members;
	private int membersCount;

	public Group() {}

	public Group(String name, String description, int membersCount) {
        this.name = name;
        this.description = description;
        this.members = new ArrayList<Person>();
		this.membersCount = membersCount;
    }

    public Group(String name, String description, ArrayList<Person> members) {
        this.name = name;
        this.description = description;
        this.members = members;
	    this.membersCount = members.size();
    }

	public Group(String json) throws JSONException {
		this(new JSONObject(json));
	}

	public Group(JSONObject json) throws JSONException {

		name = json.getString("name");
		description = json.optString("description", "");

		members = new ArrayList<Person>();
		if(json.has("members")) {
			JSONArray membersJson = json.getJSONArray("members");
			for(int i = 0; i < membersJson.length(); i++) {
				members.add(new Person(membersJson.getString(i)));
			}
		}
		membersCount = json.optInt("membersCount");
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", name);
			jsonObject.put("description", description);
			JSONArray membersJson = new JSONArray();
			for(Person p : members) {
				membersJson.put(p.toString());
			}
			jsonObject.put("members", membersJson);
			jsonObject.put("membersCount", membersCount);
		} catch (JSONException e) {
            Log.e("JSON", "Error imprimiendo grupo");
		}
		return jsonObject.toString();
	}

	public int getMembersCount() {
		return membersCount;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<Person> getMemberList() {
		return members;
	}
}
