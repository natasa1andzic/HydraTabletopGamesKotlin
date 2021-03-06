package com.natasaandzic.hydratabletopgames.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

	private static final String TAG = "MyAndroidFCMIIDService";

	@Override
	public void onTokenRefresh() {
		//Get hold of the registration token
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		//Log the token
		Log.d(TAG, "Refreshed token: " + refreshedToken);
	}
	private void sendRegistrationToServer(String token) {
		//Implement this method if you want to store the token on your server
	}
}