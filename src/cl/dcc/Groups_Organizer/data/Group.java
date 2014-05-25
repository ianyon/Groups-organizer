package cl.dcc.Groups_Organizer.data;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ian on 21-05-2014.
 */
public class Group {
    public String name, description;
    public ArrayList<Person> members;
	private int membersCount;

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

	public Group(JSONObject json) {

		name = json.optString("name", "");
		description = json.optString("description", "");

		members = new ArrayList<Person>();
		try {
			JSONArray membersJson = json.getJSONArray("members");
			for(int i = 0; i < membersJson.length(); i++) {
				members.add(new Person(membersJson.getString(i)));
			}
		} catch (JSONException e) {
			Log.i("Group", "Members list not found. Ommiting.");
			members.clear();
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
			e.printStackTrace();
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
}
