package cl.dcc.Groups_Organizer.data;

import android.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Ian on 22-05-2014.
 */
public class EventListData {
    private static final String CODIGO_ESTADO = "CodEstado", DESCRIPCION_ESTADO = "DescEstado",
            DATOS = "Data";
    private List<Event> eventList;

    Map<String, Object> mDatos;
    int mCodEstado;
    String mDescEstado;

    public EventListData() {
        mDescEstado = "";
    }

    public EventListData(int codEstado, String descEstado, Map<String, Object> datos) {
        mCodEstado = codEstado;
        mDescEstado = descEstado;
        mDatos = datos;
    }

    public EventListData(String serialized) throws JSONException {
        JSONArray array = new JSONArray(serialized);
        // Initialize event eventList
        eventList = new ArrayList<Event>(array.length());

        for (int i = 0; i < array.length(); i++) {
            eventList.add(new Event(array.getJSONObject(i)));
        }

        // Old code
//        this(new JSONObject(serialized));

    }

/*    public EventListData(JSONObject json) {
        setCodEstado(json.optInt(CODIGO_ESTADO, -1000));
        setDescEstado(json.optString(DESCRIPCION_ESTADO, "Esperando respuesta"));
        JSONObject jsonDatos = json.optJSONObject(DATOS);
        Map<String, Object> map = new HashMap<String, Object>();
        if (jsonDatos != null) {
            Iterator i = jsonDatos.keys();
            while (i.hasNext()) {
                String s = (String) i.next();
                try {
                    Object value = jsonDatos.get(s);
                    if (value instanceof Integer) {
                        value = Long.valueOf((Integer) value);
                    }
                    map.put(s, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }

        }
        setDatos(map);

    }

    public EventListData(String jsonString, boolean b) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        setCodEstado(json.optInt(CODIGO_ESTADO, -1000));
        setDescEstado(json.optString(DESCRIPCION_ESTADO, "Esperando respuesta"));
        JSONObject jsonDatos = json.optJSONObject(DATOS);
        Map<String, Object> map = new HashMap<String, Object>();
        if (jsonDatos != null) {
            Iterator i = jsonDatos.keys();
            while (i.hasNext()) {
                String s = (String) i.next();
                try {
                    Object value = jsonDatos.get(s);
                    if (value instanceof String) {
                        String[] pair = ((String) value).split(",");
                        map.put(s, new Pair<Long, Long>(Long.valueOf(pair[0]), Long.valueOf(pair[1])));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }

        }
        setDatos(map);

    }*/

    public void setDescEstado(String descEstado) {
        this.mDescEstado = descEstado;
    }

    public void setDatos(Map<String, Object> datos) {
        this.mDatos = datos;
    }

    public int getCodEstado() {
        return mCodEstado;
    }

    public void setCodEstado(int codEstado) {
        this.mCodEstado = codEstado;
    }

    public String getdescEstado() {
        return mDescEstado;
    }

    public Map<String, Object> getMap() {
        return this.mDatos;
    }

    public LinkedHashMap<String, Object> getSortedMap(boolean hight_to_low) {
        Map<String, Object> unsortMap = getMap();
        LinkedList<Entry<String, Object>> list = new LinkedList<Entry<String, Object>>(
                unsortMap.entrySet());

        // sort eventList based on comparator
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<Object>) ((Map.Entry<String, Object>) (o1)).getValue())
                        .compareTo((Comparable<Object>) ((Map.Entry<String, Object>) (o2)).getValue());
            }
        });

        if (hight_to_low) {
            Collections.reverse(list);
        }

        // put sorted eventList into map again
        // LinkedHashMap make sure order in which keys were inserted
        LinkedHashMap<String, Object> sortedMap = new LinkedHashMap<String, Object>();
        for (Iterator<Entry<String, Object>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            sortedMap.put((String) entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CODIGO_ESTADO, "" + mCodEstado);
        map.put(DESCRIPCION_ESTADO, mDescEstado);
        if (mDatos != null) {
            map.put(DATOS, new JSONObject(mDatos));
        } else {
            map.put(DATOS, new JSONObject(new HashMap<String, String>()));
        }
        JSONObject json = new JSONObject(map);
        return json.toString();
    }

}
