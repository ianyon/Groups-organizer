package cl.dcc.Groups_Organizer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.connection.ConnectionStatus;
import cl.dcc.Groups_Organizer.connection.GetEventListConn;
import cl.dcc.Groups_Organizer.controller.TabsAdapter;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.androidannotations.annotations.*;
import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@EActivity(R.layout.pager_view)
public class PagerViewHost extends CustomFragmentActivity implements
        OnSharedPreferenceChangeListener {
    private static final boolean DEBUG = false;
    // Sección pager
    @ViewById(android.R.id.tabhost)
    TabHost mTabHost;

    @ViewById(R.id.pager)
    ViewPager mViewPager;

    /*@ViewById(R.id.pager)
    ImageView botonRefresh;

    @ViewById(R.id.pager)
    ImageView botonFecha;*/

    @ViewById(R.id.pagerViewTextName)
    TextView mTextUserName;

    @ViewById(R.id.pagerViewButtonGroups)
    ImageButton mButtonGroups;

    @ViewById(R.id.pagerViewButtonProfile)
    ImageButton mButtonProfile;

    @ViewById(R.id.pagerViewButtonCreateEvent)
    View mCreateEvent;

    @Extra
    String mName;

    TabsAdapter mTabsAdapter;

    @InstanceState
    Date dateInicio, dateFin;
    // Sección fechas

    @InstanceState
    boolean noRefrescar = false;

    @InstanceState
    boolean viewFechaAbierto;

    private TextView textViewRangoFechas;
    private SimpleDateFormat formatterBoton = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat formatterRango = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private AdminPreferences preferences;

    @AfterViews
    void initVars() {
        preferences = new AdminPreferences(this);
        mTextUserName.setText(mName);

        // TabHost configuration
        mTabHost.setup();

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mViewPager.setOffscreenPageLimit(2);

        mTabsAdapter.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Public events",
                        getResources().getDrawable(R.drawable.ic_launcher)),
                PublicEvents.class, null
        );
        mTabsAdapter.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("My events",
                        getResources().getDrawable(R.drawable.ic_launcher)),
                MyEvents.class, null
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        new AdminPreferences(this).getPreferenciasDatos()
                .registerOnSharedPreferenceChangeListener(this);
//        onDataChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        new AdminPreferences(this).getPreferenciasDatos()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onBotonRefresh(View v) {
        refresh();
    }

    public void refresh() {
        if (!ConnectionStatus.isOnline(this)) {
            Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Connection for public event list
        GetEventListConn eventsConn = new GetEventListConn(getHttpClient());
        RequestParams reqParams = eventsConn.generateParams("Public");
        eventsConn.go(reqParams, new EventListHttpResponseHandler());

        // Connection for personal event list
        eventsConn = new GetEventListConn(getHttpClient());
        reqParams = eventsConn.generateParams("Private");
        eventsConn.go(reqParams, new EventListHttpResponseHandler());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null) {
            mTabHost.setCurrentTabByTag(state.getString("tab"));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals(AdminPreferencias.INFO_MUNICIPALIDAD)
//                || key.equals(AdminPreferencias.IMAGEN_COMUNA))
//            onDataChanged();
    }

    /*private DatosSeccion generateRandomData(int j) {
        Map<String, Object> auxMap = new HashMap<String, Object>();
        Random rand = new Random();
        for (int i = 0; i < 25; i++)
            if (j == 0)
                auxMap.put("Depto muni num " + i, Long.valueOf((Math.abs(rand.nextInt()))));
            else if (j == 1)
                auxMap.put("Depto muni num " + i, "" + Long.valueOf(Math.abs(rand.nextInt())) + ","
                        + Long.valueOf(Math.abs(rand.nextInt())));
        return new DatosSeccion(0, "Exito", auxMap);
    }*/

    public void onRegisterClick(View v) {
        startActivity(new Intent(this, Register.class));
    }

    /*private void onDataChanged() {
        DatosSeccion datosSeccion = new AdminPreferencias(this)
                .getValores(AdminPreferencias.INFO_MUNICIPALIDAD);
        Map<String, Object> map = datosSeccion.getMap();
        if (map != null && map.size() != 0) {
            mTextUserName.setText(map.get("nombre_municipalidad").toString());
        } else {
            mTextUserName.setText("");
        }
    }*/

    public void onAddFriendsClick(View v) {
        startActivity(new Intent(this, AddPeople.class));
    }

    public void onAddEventClick(View v) {
        startActivity(new Intent(this, EventConfig_.class));
    }

    // Object for Handling the http response
    private class EventListHttpResponseHandler extends TextHttpResponseHandler {
        private String category, data;

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString,
                              Throwable throwable) {
            Toast.makeText(PagerViewHost.this, "Error when connecting to the server", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            // TODO: hacer mas verboso el control de errores
            if (statusCode == 200 && parseResponse(responseBody)) {
                Toast.makeText(PagerViewHost.this, "Datos recibidos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PagerViewHost.this, "Error en la recepción de datos", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean parseResponse(String body) {
            body = body.trim();
            int i;
            if ((i = body.indexOf("\n")) == -1)
                return false;

            category = body.substring(0, i);
            data = body.substring(i);

            AdminPreferences preferences = new AdminPreferences(PagerViewHost.this);
            preferences.setValores(category, data);
            return true;
        }
    }

}
