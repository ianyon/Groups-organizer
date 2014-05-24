package cl.dcc.Groups_Organizer.ui;

import android.support.v4.app.FragmentActivity;
import cl.dcc.Groups_Organizer.app.GroupsOrganizerApp;

import com.loopj.android.http.AsyncHttpClient;

public abstract class CustomFragmentActivity extends FragmentActivity {

	public AsyncHttpClient getHttpClient() {
		return ((GroupsOrganizerApp)getApplication()).getAsyncHttpClient();

	}
	

}
