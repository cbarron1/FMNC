package edu.nd.fmnc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkService extends Service {

	WifiStateReceiver wSR;
	IntentFilter filter;
	XmlWriter xmlwriter;
	AlarmReceiver alarm;
	TelephonyManager tm;
	PhoneStateChangeListener pscl;

	public static final String ALARM_ACTION = "com.example.fmnc.ALARM_ACTION";
	private IntentFilter alarmFilter = new IntentFilter(ALARM_ACTION);

	private final IBinder mBinder = new LocalBinder();
	private NotificationManager nm;
	private Notification n;
	private int nID = 001;

	private boolean receiver_registered;

	public class LocalBinder extends Binder {
		NetworkService getService() {
			return NetworkService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		alarm = new AlarmReceiver();

		receiver_registered = false;

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setContentTitle("Network Service")
				.setContentText("Service Running")
				.setSmallIcon(android.R.drawable.stat_notify_more)
				.setWhen(System.currentTimeMillis()).setAutoCancel(false)
				.setOngoing(true);

		Intent intent = new Intent(this, FMNCActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		wSR = new WifiStateReceiver(this);
		filter = new IntentFilter();
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

		builder.setContentIntent(pi);
		n = builder.build();
	}

	@Override
	public void onStart(Intent intent, int startID) {
		nm.notify(nID, n);
		if (!receiver_registered) {
			// register WifiStateReceiver
			registerReceiver(wSR, filter);
			registerReceiver(alarm, alarmFilter);
			receiver_registered = true;
		}
		
		pscl = new PhoneStateChangeListener(this);
		
		tm.listen(pscl, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_CELL_LOCATION);
		

		if (FMNCActivity.WIFI_ON) {
			if (alarm != null) {
				alarm.SetAlarm(this.getApplicationContext());
			} else {
				Log.e("NULL", "Alarm is null");
			}
		}
	}

	@Override
	public void onDestroy() {
		nm.cancel(nID);
		if (receiver_registered) {
			unregisterReceiver(wSR);
			unregisterReceiver(alarm);
			receiver_registered = false;
		}

		tm.listen(pscl, PhoneStateListener.LISTEN_NONE);
		
		if (alarm != null) {
			alarm.CancelAlarm(this.getApplicationContext());
		}
	}
	
	public void resetAlarm() {
		
	}


}
