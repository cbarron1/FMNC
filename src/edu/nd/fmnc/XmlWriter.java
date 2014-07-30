package edu.nd.fmnc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XmlWriter {

	// private Context context;
	boolean exists = false;
	String source;

	public XmlWriter(String source_string) {
		source = source_string;
	}

	Map<String, Object> network = new HashMap<String, Object>();
	Map<String, Object> cellNetwork = new HashMap<String, Object>();
	long timestamp;

	public String writeXml() {
		XmlSerializer xmls = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			xmls.setOutput(writer);

			xmls.startTag(null, "WifiInfo");

			xmls.attribute(null, "Name", "Android.app");
			xmls.attribute(null, "Version", FMNCActivity.VERSION);
			xmls.attribute(null, "ClientID", network.get("ClientID").toString());
			xmls.attribute(null, "Source", source);
			xmls.attribute(null, "SSID", network.get("SSID").toString());
			xmls.attribute(null, "MAC", network.get("MAC").toString());
			// xmls.attribute(null, "Frequency",
			// network.get("FREQ").toString());
			xmls.attribute(null, "RSSI", network.get("RSSI").toString());
			// xmls.attribute(null, "Security",
			// network.get("SECURE").toString());

			xmls.endTag(null, "WifiInfo");

			xmls.flush();

			return writer.toString();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	public String writeXmlForCell() {
		XmlSerializer xmls = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		
		try {
			xmls.setOutput(writer);
			
			xmls.startTag(null, "CellInfo");
			
			xmls.attribute(null, "Name", "Android.app");
			xmls.attribute(null, "Version", FMNCActivity.VERSION);
			xmls.attribute(null, "ClientID", cellNetwork.get("ClientID").toString()); //client's wifi-mac
			xmls.attribute(null, "Source", source);
			xmls.attribute(null, "NetworkType", cellNetwork.get("TYPE").toString());
			xmls.attribute(null, "CellIdentity", cellNetwork.get("CID").toString());
			xmls.attribute(null, "Level", cellNetwork.get("LEVEL").toString());
			xmls.attribute(null, "Dbm", cellNetwork.get("DBM").toString());
			
			xmls.flush();
			
			return writer.toString();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
}
