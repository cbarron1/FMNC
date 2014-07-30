package edu.nd.fmnc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	XmlWriter xmlwriter;
	
	public AlarmReceiver() {
		//this.xmlwriter = xwriter;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if (ni.isConnected()) {
			//new Thread(new ClientThread(context, "Scheduled")).start();
		}
		
	}

	public void SetAlarm(Context context) {
		Log.d("TIME", Integer.toString(FMNCActivity.TIME));
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		//Intent intent = new Intent(context, AlarmReceiver.class);
		Intent intent = new Intent(NetworkService.ALARM_ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 250,
				1000 * 10, pi);
	}

	public void CancelAlarm(Context context) {
		//Intent intent = new Intent(context, AlarmReceiver.class);
		Intent intent = new Intent(NetworkService.ALARM_ACTION);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.cancel(sender);
	}


}
