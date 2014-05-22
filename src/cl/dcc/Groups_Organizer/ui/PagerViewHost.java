package cl.dcc.Groups_Organizer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import cl.dcc.Groups_Organizer.R;
import cl.dcc.Groups_Organizer.data.AdminPreferences;
import org.androidannotations.annotations.*;

import java.text.SimpleDateFormat;
import java.util.*;

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
    private boolean noRefrescar = false;

    @InstanceState
    private boolean viewFechaAbierto;

    @InstanceState
    private boolean allTasksDone = true;
    private TextView textViewRangoFechas;
    private SimpleDateFormat formatterBoton = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat formatterRango = new SimpleDateFormat("dd/MM/yy", Locale.US);

    private Map<String, Object> mapValoresEnCarga;

    @InstanceState
    private boolean mDatosEstaticosCargados;

    @AfterViews
    void initTabHost() {
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

    @AfterViews
    void initVars() {
        AdminPreferences ap = new AdminPreferences(this);

        mapValoresEnCarga = new HashMap<String, Object>();
        mDatosEstaticosCargados = false;

        mTextUserName.setText(mName);
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
        /*for (TareaAsincrona ta : getRunningTasks()) {
            ta.cancel(true);
        }
        allTasksDone = false;
        if (!Conexion.isOnline(this)) {
            Toast.makeText(this, "No hay una conexión de datos.", Toast.LENGTH_SHORT).show();
            allTasksDone = true;
            return;
        }
        mapValoresEnCarga.clear();
        TareaAsincrona task;
        task = new TraerDeudores(this, this);
        ((TraerDeudores) task).setParametros(ano, dateInicio, dateFin);
        runTask(task);
        task = new TraerGastos(this, this);
        ((TraerGastos) task).setParametros(ano, dateInicio, dateFin);
        runTask(task);
        task = new TraerGiradores(this, this);
        ((TraerGiradores) task).setParametros(ano, 0, dateInicio, dateFin);
        runTask(task);
        task = new TraerRanking(this, this);
        ((TraerRanking) task).setParametros(ano, 10, dateInicio, dateFin);
        runTask(task);
        task = new TraerIngresos(this, this);
        ((TraerIngresos) task).setParametros(ano, dateInicio, dateFin);
        runTask(task);
        task = new TraerPersonal(this, this);
        ((TraerPersonal) task).setParametros(ano, dateInicio, dateFin);
        runTask(task);
        // Introducimos una pequeña optimización en que se cargan el
        // logo, los concejales y la info de comuna sólo una vez por sesión.
        if (!mDatosEstaticosCargados) {
            task = new TraerInfoMunicipalidad(this, this);
            runTask(task);
            task = new TraerLogo(this, this);
            runTask(task);
            task = new TraerConcejales(this, this);
            runTask(task);
        }*/
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
//
//    @Override
//    public void onPostExecute(TareaAsincrona tarea, Object... vars) {
//        super.onPostExecute(tarea, vars);
//        AdminPreferencias adminPreferencias = new AdminPreferencias(this);
////        if (tarea instanceof TraerDeudores) {
////            mapValoresEnCarga.put(AdminPreferencias.DEUDORES, vars[0]);
////        } else if (tarea instanceof TraerGastos) {
////            mapValoresEnCarga.put(AdminPreferencias.GASTOS, vars[0]);
////        } else if (tarea instanceof TraerGiradores) {
////            mapValoresEnCarga.put(AdminPreferencias.GIRADORES, vars[0]);
////        } else if (tarea instanceof TraerIngresos) {
////            mapValoresEnCarga.put(AdminPreferencias.INGRESOS, vars[0]);
////        } else if (tarea instanceof TraerPersonal) {
////            mapValoresEnCarga.put(AdminPreferencias.PERSONAL, vars[0]);
////        } else if (tarea instanceof TraerRanking) {
////            mapValoresEnCarga.put(AdminPreferencias.RANKING, vars[0]);
////        } else if (tarea instanceof TraerInfoMunicipalidad) {
////            mapValoresEnCarga.put(AdminPreferencias.INFO_MUNICIPALIDAD, vars[0]);
////        } else if (tarea instanceof TraerLogo) {
////            mapValoresEnCarga.put(AdminPreferencias.IMAGEN_COMUNA, vars[0]);
////        } else if (tarea instanceof TraerConcejales) {
////            mapValoresEnCarga.put(AdminPreferencias.CONCEJALES, vars[0]);
////        }
//        if (getRunningTasks().size() == 0) {
//            allTasksDone = true;
//            volcarValoresCargados();
//        }
//
//    }

    private void volcarValoresCargados() {
        if (mapValoresEnCarga == null || mapValoresEnCarga.size() == 0)
            return;
        AdminPreferences adminPreferences = new AdminPreferences(this);
        for (String s : mapValoresEnCarga.keySet()) {
            adminPreferences.setValores(s, mapValoresEnCarga.get(s));
        }

        // Si hacemos una conexión, estamos seguros que se habrán
        // cargado al menos una vez los datos estáticos.
        mDatosEstaticosCargados = true;
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals(AdminPreferencias.INFO_MUNICIPALIDAD)
//                || key.equals(AdminPreferencias.IMAGEN_COMUNA))
//            onDataChanged();
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

    public void onRegisterClick(View v) {
        startActivity(new Intent(this, Register.class));
    }

    public void onAddFriendsClick(View v) {
        startActivity(new Intent(this, AddPeople.class));
    }

    public void onAddEventClick(View v) {
        startActivity(new Intent(this, EventConfig_.class));
    }

    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost. It relies on a
     * trick. Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show. This is not sufficient for switching
     * between pages. So instead we make the content part of the tab host 0dp
     * high (it is not shown) and the TabsAdapter supplies its own dummy view to
     * show as the tab content. It listens to changes in tabs, and takes care of
     * switch to the correct paged in the ViewPager whenever the selected tab
     * changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter implements
            TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }
    }

}
