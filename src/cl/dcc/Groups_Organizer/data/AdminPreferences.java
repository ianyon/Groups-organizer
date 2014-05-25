package cl.dcc.Groups_Organizer.data;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminPreferences {
    // Categorías en las preferencias PREFERENCIAS_DATOS.
    public static final String PERSONAL = "Personal";
    public static final String PUBLIC_EVENTS = "PUBLIC", PRIVATE_EVENTS = "PRIVATE", USER = "USER";
    // Preferencias disponibles en la aplicación.
    public static final String PREFERENCIAS_GENERALES = "PREFERENCIAS_GENERALES",
    		PREFERENCIAS_USUARIOS = "PREFERENCIAS_USUARIOS", PREFERENCIAS_EVENTOS = "PREFERENCIAS_EVENTOS"
		    , PREFERENCIAS_GRUPOS = "PREFERENCIAS_GRUPOS";
    private Context mContext;

    public AdminPreferences(Context context) {
        mContext = context;
    }

    public SharedPreferences getPreferencias() {
        return mContext.getSharedPreferences(PREFERENCIAS_GENERALES, 0);
    }
    
    public SharedPreferences getPreferencias(String tipo) {
    	return mContext.getSharedPreferences(tipo, 0);
    }

    public void setValores(String tipoValores, Object data) {
        if (data == null) return;
        getPreferencias().edit().putString(tipoValores, data.toString()).commit();
    }

    public EventListData getValores(String tipoValores) {
        String jsonString = getPreferencias().getString(tipoValores, "{}");
        try {
            return new EventListData(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Person getUser() {
    	String[] array = getPreferencias().getString(USER, "{}").split(";");
        return new Person(array[0], array[1]);
    }

    public void setUser(Person user) {
    	setValores(USER, user.toString());
    }

	public List<Person> getPersonList() {
		SharedPreferences preferences = getPreferencias(PREFERENCIAS_USUARIOS);
		Map<String, ?> map = preferences.getAll();
		List<Person> list = new ArrayList<Person>();
		for(String key : map.keySet()) {
			Object value = map.get(key);
			try {
				Person p = new Person(value.toString());
				list.add(p);
			} catch (JSONException e) {
				e.printStackTrace();
				preferences.edit().remove(key);
			}
		}
		return list;
	}

	public Person getPerson(String username) throws JSONException {
		return new Person(getPreferencias(PREFERENCIAS_USUARIOS).getString(username, "{}"));
	}

    public void savePerson(Person user) {
    	getPreferencias(PREFERENCIAS_USUARIOS).edit().putString(user.getUsername(), user.toString()).commit();
    }

	public List<Group> getGroupList() {
		SharedPreferences preferences = getPreferencias(PREFERENCIAS_GRUPOS);
		Map<String, ?> map = preferences.getAll();
		List<Group> list = new ArrayList<Group>();
		for(String key : map.keySet()) {
			Object value = map.get(key);
			try {
				Group g = new Group(new JSONObject(value.toString()));
				list.add(g);
			} catch (JSONException e) {
				e.printStackTrace();
				preferences.edit().remove(key);
			}
		}
		return list;
	}

	public Person getGroup(String groupName) throws JSONException {
		return new Person(getPreferencias(PREFERENCIAS_GRUPOS).getString(groupName, "{}"));
	}

	public void saveGroup(Group group) {
		getPreferencias(PREFERENCIAS_GRUPOS).edit().putString(group.name, group.toString()).commit();
	}
    
    public void saveEvent(Event event) {
    	getPreferencias(PREFERENCIAS_EVENTOS).edit().putString(""+event.getId(), event.toString()).commit();
    }

    public Event getEvent(int id) {
        String jsonString = getPreferencias(PREFERENCIAS_EVENTOS).getString(id+"", "{}");
        try {
            return new Event(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
