package cl.dcc.Groups_Organizer.utilities;

import java.util.Calendar;

/**
 * Created by Noone on 05-07-2014.
 */
public class LoginBackOff {

    private int counter;
    private int exponete;
    private int initialTime;
    private boolean canLog;

    public LoginBackOff(){
        counter = 0;
        exponete = 0;
        Calendar c = Calendar.getInstance();
        initialTime = c.get(Calendar.SECOND);
        canLog = true;

    }

    private final void startCounting(){
            Calendar c = Calendar.getInstance();
            initialTime = c.get(Calendar.SECOND);
    }

    public final boolean canLogAgain(){
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);

        if( (seconds - initialTime)> (Math.exp(exponete) - 1)*25 ){
            canLog = true;
        }

        return canLog;
    }

    public final void success(){
        exponete = 0;
        counter = 0;
    }
    public final void oneTry(){
        if(canLog) {
            startCounting();
            counter++;
            if (counter >= 5) {
                exponete++;
                canLog = false;
                counter = 0;
            }
        }
    }
}
