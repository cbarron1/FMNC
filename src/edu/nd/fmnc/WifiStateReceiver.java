package edu.nd.fmnc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiStateReceiver extends BroadcastReceiver {

	WifiManager wm;
	NetworkService parent;

	public WifiStateReceiver(NetworkService mParent) {
		// this.xmlwriter = xwriter;
		this.parent = mParent;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();

		if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = (NetworkInfo) intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (networkInfo.getState() == State.CONNECTED) {
				// String mBSSID =
				// intent.getStringExtra(WifiManager.EXTRA_BSSID);
				// Toast.makeText(context, "BSSID: " + mBSSID ,
				// Toast.LENGTH_SHORT).show();
			} else {
				// Toast.makeText(context, networkInfo.getState().toString(),
				// Toast.LENGTH_SHORT).show();
			}
		} else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {

			//Toast.makeText(context, "SUPPLICANT STATE CHANGED",
			//		Toast.LENGTH_SHORT).show(); 
			try {
				SupplicantState state = (SupplicantState) intent
						.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
				Log.d("STATE", "Supplicant State: " + state.toString());

				if (state.equals(SupplicantState.COMPLETED)) {

					 new Thread(new ClientThread(context,
					 "Wifi Roam", ConnectivityManager.TYPE_WIFI)).start();
					 
					 parent.resetAlarm();

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			return;
		}
	}

}
