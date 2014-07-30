package edu.nd.fmnc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ClientThread extends Thread {
	private Socket clientSocket;
	WifiManager wm;
	private XmlWriter xmlwriter;
	public Map<String, Object> wifiStats;
	public int networkType;
	private Context mContext;

	public ClientThread(Context context, String source, int NetworkType) {
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// this.xmlwriter = xwriter;
		this.xmlwriter = new XmlWriter(source);
		this.wifiStats = xmlwriter.network;
		this.networkType = NetworkType;
		this.mContext = context;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public synchronized void run() {
		if (networkType == ConnectivityManager.TYPE_WIFI) {
			try {
				InetAddress serverAddr = InetAddress
						.getByName(FMNCActivity.SERVER_IP);
				clientSocket = new Socket(serverAddr, FMNCActivity.SERVERPORT);

				while (!clientSocket.isConnected()) {

				}

				Log.d("CONNECTED", "Connected to Server");

				wifiStats.clear();

				WifiInfo winfo = wm.getConnectionInfo();

				wifiStats.put("ClientID", winfo.getMacAddress());
				wifiStats.put("SSID", winfo.getSSID());
				wifiStats.put("MAC", winfo.getBSSID());
				wifiStats.put("RSSI", winfo.getRssi());

				String str = xmlwriter.writeXml();

				Log.d("WIFI", str);

				PrintWriter out = new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(
								clientSocket.getOutputStream())), true);
				out.println(str);

				BufferedReader input = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));

				Log.d("READ", "waiting for input from server");
				String rec = input.readLine();
				while (rec != null) {
					Log.d("RECEIVED", " " + rec);
					rec = input.readLine();
				}

				clientSocket.close();

				Log.d("CLOSE", "Socket closed");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (networkType == ConnectivityManager.TYPE_MOBILE) {
			try {
				
				  InetAddress serverAddr = InetAddress
				  .getByName(FMNCActivity.SERVER_IP); clientSocket = new
				  Socket(serverAddr, FMNCActivity.SERVERPORT);
				  
				  while (!clientSocket.isConnected()) {
				  
				  }
				 

				Log.d("CONNECTED", "Connected to Server");

				xmlwriter.cellNetwork.clear();

				WifiInfo winfo = wm.getConnectionInfo();

				TelephonyManager tm = (TelephonyManager) mContext
						.getSystemService(Context.TELEPHONY_SERVICE);
				int phoneType = tm.getPhoneType();
				int networkType = tm.getNetworkType();
				if (phoneType == TelephonyManager.PHONE_TYPE_NONE) {
					Log.d("CELL", "No cell capability");
				} else {

					Log.d("CELL", "Has cellular");

					switch (phoneType) {
					case TelephonyManager.PHONE_TYPE_CDMA:
						Log.d("CELLTYPE", "CDMA");
						break;
					case TelephonyManager.PHONE_TYPE_GSM:
						Log.d("CELLTYPE", "GSM");
						break;
					case TelephonyManager.PHONE_TYPE_SIP:
						Log.d("CELLTYPE", "SIP");
						break;
					}

					if (networkType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
						Log.d("CELL", "No network connection");
					} else {
						switch (networkType) {
						case TelephonyManager.NETWORK_TYPE_1xRTT:
							Log.d("CELL", "1xRTT");
							xmlwriter.cellNetwork.put("TYPE", "1xRTT");
							break;
						case TelephonyManager.NETWORK_TYPE_CDMA:
							Log.d("CELL", "CDMA");
							xmlwriter.cellNetwork.put("TYPE", "CDMA");
							break;
						case TelephonyManager.NETWORK_TYPE_EDGE:
							Log.d("CELL", "EDGE");
							xmlwriter.cellNetwork.put("TYPE", "EDGE");
							break;
						case TelephonyManager.NETWORK_TYPE_EHRPD:
							Log.d("CELL", "EHRPD");
							xmlwriter.cellNetwork.put("TYPE", "EHRPD");
							break;
						case TelephonyManager.NETWORK_TYPE_EVDO_0:
							Log.d("CELL", "EVDO_0");
							xmlwriter.cellNetwork.put("TYPE", "EVDO_0");
							break;
						case TelephonyManager.NETWORK_TYPE_EVDO_A:
							Log.d("CELL", "EVDO_A");
							xmlwriter.cellNetwork.put("TYPE", "EVDO_A");
							break;
						case TelephonyManager.NETWORK_TYPE_EVDO_B:
							Log.d("CELL", "EVDO_B");
							xmlwriter.cellNetwork.put("TYPE", "EVDO_B");
							break;
						case TelephonyManager.NETWORK_TYPE_GPRS:
							Log.d("CELL", "GPRS");
							xmlwriter.cellNetwork.put("TYPE", "GPRS");
							break;
						case TelephonyManager.NETWORK_TYPE_HSDPA:
							Log.d("CELL", "HSPDA");
							xmlwriter.cellNetwork.put("TYPE", "HSDPA");
							break;
						case TelephonyManager.NETWORK_TYPE_HSPA:
							Log.d("CELL", "HSPA");
							xmlwriter.cellNetwork.put("TYPE", "HSPA");
							break;
						case TelephonyManager.NETWORK_TYPE_HSPAP:
							Log.d("CELL", "HSPAP");
							xmlwriter.cellNetwork.put("TYPE", "HSPAP");
							break;
						case TelephonyManager.NETWORK_TYPE_HSUPA:
							Log.d("CELL", "HSUPA");
							xmlwriter.cellNetwork.put("TYPE", "HSUPA");
							break;
						case TelephonyManager.NETWORK_TYPE_IDEN:
							Log.d("CELL", "iDen");
							xmlwriter.cellNetwork.put("TYPE", "iDen");
							break;
						case TelephonyManager.NETWORK_TYPE_LTE:
							Log.d("CELL", "LTE");
							xmlwriter.cellNetwork.put("TYPE", "LTE");
							break;
						case TelephonyManager.NETWORK_TYPE_UMTS:
							Log.d("CELL", "UMTS");
							xmlwriter.cellNetwork.put("TYPE", "UMTS");
							break;
						}

						try {
							List<CellInfo> cellinfo = tm.getAllCellInfo();
							if (cellinfo == null) {

								Log.e("NULL", "Trouble Detecting Cell Info");
								
							} else if (cellinfo.isEmpty()) {
								
								Log.e("EMPTY", "Cellinfo List is empty");
								
							} else {
								
								for (CellInfo cinfo : cellinfo) {
									
									if (cinfo.isRegistered()) {
										if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
											Log.i("TYPE_CDMA", "TYPE_CDMA");
											if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {
												Log.i("NETWORK_TYPE", "LTE");
												if (cinfo instanceof CellInfoLte) {
													Log.i("LTE INSTANCE", "YES");
													CellSignalStrengthLte cssl = ((CellInfoLte) cinfo).getCellSignalStrength();
													CellIdentityLte cil = ((CellInfoLte) cinfo).getCellIdentity();
													xmlwriter.cellNetwork.put("CID", cil);
													Log.i("Level", Integer.toString(cssl.getLevel()));
													xmlwriter.cellNetwork.put("LEVEL", cssl.getLevel());
													Log.i("Dbm", Integer.toString(cssl.getDbm()));
													xmlwriter.cellNetwork.put("DBM", cssl.getDbm());
												}
											} else {
												Log.i("NETWORK_TYPE", "NOT LTE");
												if (cinfo instanceof CellInfoCdma) {
													CellSignalStrengthCdma cssc = ((CellInfoCdma) cinfo).getCellSignalStrength();
													CellIdentityCdma cic = ((CellInfoCdma) cinfo).getCellIdentity();
													xmlwriter.cellNetwork.put("CID", cic);
													xmlwriter.cellNetwork.put("LEVEL", cssc.getLevel());
													xmlwriter.cellNetwork.put("DBM", cssc.getDbm());
												}
											}
										} else if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
											if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {
												if (cinfo instanceof CellInfoLte) {
													CellSignalStrengthLte cssl = ((CellInfoLte) cinfo).getCellSignalStrength();
													CellIdentityLte cil = ((CellInfoLte) cinfo).getCellIdentity();
													xmlwriter.cellNetwork.put("CID", cil);
													xmlwriter.cellNetwork.put("LEVEL", cssl.getLevel());
													xmlwriter.cellNetwork.put("DBM", cssl.getDbm());
												}
											} else {
												if (cinfo instanceof CellInfoGsm) {
													CellSignalStrengthGsm cssg = ((CellInfoGsm) cinfo).getCellSignalStrength();
													CellIdentityGsm cig = ((CellInfoGsm) cinfo).getCellIdentity();
													xmlwriter.cellNetwork.put("CID", cig);
													xmlwriter.cellNetwork.put("LEVEL", cssg.getLevel());
													xmlwriter.cellNetwork.put("DBM", cssg.getDbm());
												}
											}
										}
									}
								}
								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

				xmlwriter.cellNetwork.put("ClientID", winfo.getMacAddress());

				String str = xmlwriter.writeXmlForCell();

				Log.d("CELL", str);

				
				 PrintWriter out = new PrintWriter( new BufferedWriter(new
				  OutputStreamWriter( clientSocket.getOutputStream())), true);
				  out.println(str);
				  
				  BufferedReader input = new BufferedReader( new
				  InputStreamReader(clientSocket.getInputStream()));
				  
				  Log.d("READ", "waiting for input from server"); String rec =
				  input.readLine(); while (rec != null) { Log.d("RECEIVED", " "
				  + rec); rec = input.readLine(); }
				  
				  clientSocket.close();
				 

				Log.d("CLOSE", "Socket closed");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
