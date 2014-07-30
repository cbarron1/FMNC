package edu.nd.fmnc;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import edu.nd.fmnc.NetworkService.LocalBinder;

public class FMNCActivity extends Activity implements TabListener {

	// / Settings ///
	public static String SERVER_IP = "129.74.161.212";
	public static int SERVERPORT = 80;
	public static final String VERSION = "0.1";
	public static int TIME = 0;
	public static boolean WIFI_ON = true;
	public static boolean WIFI_ROAM = true;
	public static boolean CELL_ON = false;
	public static boolean CELL_ROAM = false;

	public static final int RESULT_SETTINGS = 1;

	NetworkService wService;
	boolean service_bound;

	MainViewFragment main_fragment;
	ConsoleFragment console_fragment;

	// Service connecton allows for binding of NetworkService
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service_bound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			wService = binder.getService();
			service_bound = true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fmnc);

		main_fragment = new MainViewFragment();
		console_fragment = new ConsoleFragment();

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, main_fragment).commit();
		}

		Log.d("CREATE", "onCreate");

		service_bound = false;

		// Add tabs to layout
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		Tab main_tab = bar.newTab();
		main_tab.setText("Main");
		main_tab.setTabListener(this);
		bar.addTab(main_tab);
		
		Tab console_tab = bar.newTab();
		console_tab.setText("Console");
		console_tab.setTabListener(this);
		bar.addTab(console_tab);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("RESUME", "onResume");
		if (!service_bound) {
			Intent intent = new Intent(this, NetworkService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
			service_bound = true;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Log.d("STOP", "onStop");
		if (service_bound) {
			unbindService(mConnection);
			service_bound = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Log.d("Destroy", "onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Log.d("PAUSE", "onPause");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Log.d("START", "onStart");
	}

	public void StartService(View v) {
		//Log.d("SERVICE", "Start Service");
		Intent intent = new Intent(this, NetworkService.class);
		startService(intent);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public void StopService(View v) {
		if (service_bound) {
			//Log.d("SERVICE", "Stop Service");
			unbindService(mConnection);
			stopService(new Intent(this, NetworkService.class));
			service_bound = false;
		}
	}

	public void ForceScan(View v) {
		//Log.d("SCAN", "Force Scan");
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo cNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wNetwork.getState().equals(State.CONNECTED)) {
			new Thread(new ClientThread(this, "Forced",
					ConnectivityManager.TYPE_WIFI)).start();
		} else if (cNetwork.isConnected()) {
			Log.i("CONNECTED", "Cell is connected");
			new Thread(new ClientThread(this, "Forced",
					ConnectivityManager.TYPE_MOBILE)).start();
		} else {
			Log.i("NOT CONNECTED", "No Connection");
		}
		
	}

	public void dumpInfo(View v) {
		Log.d("DUMP", "Dump information");
	}

	public void clearList(View v) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			modifySettings();
			break;
		}
	}

	public void modifySettings() {
		// if the service is bound and running, it will reset it
		// to apply any changed settings
		Log.d("SETTINGS", "Modify Settings");
		if (service_bound) {
			unbindService(mConnection);
			stopService(new Intent(this, NetworkService.class));
			service_bound = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fmnc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Fragment containing the main view with controls
	 */
	public static class MainViewFragment extends Fragment {

		public MainViewFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;

			rootView = inflater.inflate(R.layout.fragment_fmnc, container,
					false);

			return rootView;
		}
	}

	/**
	 * Fragment containing console view
	 */
	public static class ConsoleFragment extends Fragment {

		public ConsoleFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			rootView = inflater.inflate(R.layout.fragment_console, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.i("TAB", "Tab " + tab.getPosition() + " selected");
		if (tab.getPosition() == 0) {
			ft.replace(R.id.container, main_fragment);
		} else if (tab.getPosition() == 1) {
			ft.replace(R.id.container, console_fragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	// Methods to modify static variables
	public static void setTime(int time) {
		TIME = time;
		Log.i("TIME", Integer.toString(TIME));
	}

	public static void setWifiOn(boolean b) {
		WIFI_ON = b;
		Log.i("WIFI_ON", Boolean.toString(b));
	}

	public static void setWifiRoam(boolean b) {
		WIFI_ON = b;
		Log.i("WIFI_ROAM", Boolean.toString(b));
	}

	public static void setCellOn(boolean b) {
		WIFI_ON = b;
		Log.i("CELL_ON", Boolean.toString(b));
	}

	public static void setCellRoam(boolean b) {
		WIFI_ON = b;
		Log.i("CELL_ROAM", Boolean.toString(b));
	}

}
