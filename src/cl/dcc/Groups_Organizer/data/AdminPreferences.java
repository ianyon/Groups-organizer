package cl.dcc.Groups_Organizer.data;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;

public class AdminPreferences {
    // Categorías en las preferencias PREFERENCIAS_DATOS.
    public static final String PERSONAL = "Personal";
    public static final String PUBLIC_EVENTS = "PUBLIC", PRIVATE_EVENTS = "PRIVATE", USER = "USER";
    // Preferencias disponibles en la aplicación.
    private static final String PREFERENCIAS_GENERALES = "PREFERENCIAS_GENERALES", USUARIOS = "USUARIOS", EVENTOS = "EVENTOS";
    private Context mContext;

    public AdminPreferences(Context context) {
        mContext = context;
    }

    public SharedPreferences getPreferencias() {
        return mContext.getSharedPreferences(PREFERENCIAS_GENERALES, 0);
    }
    
    private SharedPreferences getPreferencias(String tipo) {
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

    public Person getPerson(String username) throws JSONException {
    	return new Person(getPreferencias(USUARIOS).getString(username, "{}"));
    }
    
    public Person getUser() {
    	String[] array = getPreferencias().getString(USER, "{}").split(";");
        return new Person(array[0], array[1]);
    }

    public void setUser(Person user) {
    	setValores(USER, user.toString());
    }
    
    public void savePerson(Person user) {
    	getPreferencias(USUARIOS).edit().putString(user.getUsername(), user.toString()).commit();
    }
    
    public void saveEvent(Event event) {
    	getPreferencias(EVENTOS).edit().putString(""+event.getId(), event.toString()).commit();
    }

    public Event getEvent(int id) {
        String jsonString = getPreferencias().getString(id+"", "{}");
        try {
            return new Event(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
