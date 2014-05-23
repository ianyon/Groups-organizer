package cl.dcc.Groups_Organizer.data;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;

public class AdminPreferences {
    // Categorías en las preferencias PREFERENCIAS_DATOS.
    public static final String PERSONAL = "Personal";
    public static final String PUBLIC_EVENTS = "PUBLIC", PRIVATE_EVENTS = "PRIVATE", USER = "USER";
    // Preferencias disponibles en la aplicación.
    private static final String PREFERENCIAS_GENERALES = "PREFERENCIAS_GENERALES";
    private Context mContext;

    public AdminPreferences(Context context) {
        mContext = context;
    }

    public SharedPreferences getPreferencias() {
        return mContext.getSharedPreferences(PREFERENCIAS_GENERALES, 0);
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
        setValores(USER, user.toPreferences());
    }

    public Event getEvent(String name) {
        String user = getUser().getName();
        String jsonString = getPreferencias().getString(user + ";" + name, "{}");
        try {
            return new Event(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
