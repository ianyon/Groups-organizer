package cl.dcc.Groups_Organizer.utilities;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Noone on 23-05-2014.
 */
public class LoadingThing {

    private ProgressDialog progressDialog;
    private Context myContext;
    private String myTitle;
    private String myMsg;

    public LoadingThing(Context aContext, String title, String msg){
        myContext = aContext;
        myTitle = title;
        myMsg = msg;

    }

    public LoadingThing(Context aContext){
        myContext = aContext;
        myTitle = "";
        myMsg = "Waitng...";
    }

    public void stratPopUp(){
        progressDialog = ProgressDialog.show(myContext, myTitle, myMsg);
    }

    public void stopPopUp(){
        progressDialog.dismiss();
    }
}
