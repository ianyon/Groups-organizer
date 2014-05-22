package cl.dcc.Groups_Organizer.data;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class AdminPreferences {
    // Categorías en las preferencias PREFERENCIAS_DATOS.
    public static final String PERSONAL = "Personal";

    // Preferencias disponibles en la aplicación.
    private static final String PREFERENCIAS_GENERALES = "PREFERENCIAS_GENERALES";

    public static final String PUBLIC_EVENTS = "PUBLIC", PRIVATE_EVENTS = "PRIVATE", USER = "USER";

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

    public void setUser(Person user) {
        setValores(USER, user.toPreferences());
    }

    public Person getUser() {
        String[] array = getPreferencias().getString(USER, "{}").split(";");
        return new Person(array[0], array[1]);
    }
}
