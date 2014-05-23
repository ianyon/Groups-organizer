package cl.dcc.Groups_Organizer.connection;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ian on 22-05-2014.
 */
public class ConnectionStatus {
        /**
         * Permite saber si el dispositivo tiene conexión a internet
         *
         * @return True si existe una conexión a internet. False, de lo contrario.
         */
        public static boolean isOnline(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }
    }