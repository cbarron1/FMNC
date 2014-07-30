package edu.nd.fmnc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneStateChangeListener extends PhoneStateListener {
	
	private Context context;
	
	public PhoneStateChangeListener(Context c) {
		this.context = c;
	}

	@Override
	public void onDataConnectionStateChanged(int state) {
		switch (state) {
		case TelephonyManager.DATA_CONNECTED:
			//this is the only one that should actually do something
			Toast.makeText(context, "CELL CONNECTED", Toast.LENGTH_SHORT).show();
			Log.d("LISTENER", "Cell Data Connected");
			
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo cNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			if (cNetwork.isConnected()) {
				new ClientThread(context, "Cell Roam", ConnectivityManager.TYPE_MOBILE).start();
			}
			break;
		}
	}
	
	@Override
	public void onCellLocationChanged(CellLocation location) {
		//check if connected to network, and if it is, send the data
		Toast.makeText(context, "CELL LOCATION CHANGED", Toast.LENGTH_SHORT).show();
		
		Log.d("LISTENER", "Cell location changed");
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo cNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if (cNetwork != null) {
			if (cNetwork.isConnected()) {
				new ClientThread(context, "Cell Roam", ConnectivityManager.TYPE_MOBILE).start();
			}
		}
		
	}
}
