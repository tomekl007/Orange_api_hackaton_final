package com.example.openstreetmaps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import rest.RESTClient;
import Data.BusStop;
import android.util.Log;
//import java.net.CookieStore;

public class OSMClient //downloads data from open street maps
{
	Vector<BusStop> bus_stops = new Vector<BusStop>();
	
	protected String constructURL(double lat, double lon, int radius)	//radius in meters
	{	//constucting url for OSM overpass API
        //%28 (		//%2E .		//%3D =
        //%29 )		//%2C ,
        //%2B 		//%5B [
        //%3A :		//%22 "

        //przykladowy url, 100 metrow wokol palacu kultury i nauki
		//http://overpass-api.de/api/interpreter?data=node%28around%3A100%2E0%2C52%2E229863%2C21%2E004915%29%5B%22highway%22%3D%22bus%5Fstop%22%5D%3Bout%20body%3B%0A
        
		//przykladowy url, poprzednia wersja z prostokatnym obszarem
        //http://overpass-api.de/api/interpreter?data=node%2821%2E0041%2C52%2E2194%2C21%2E0302%2C52%2E2347%29%5B%22highway%22%3D%22bus%5Fstop%22%5D%3Bout%20body%3B%0A
		
		String slat = Double.toString(lat);
        String slon = Double.toString(lon);
        slat = slat.replace(".", "%2E");	//zamiana kropek na kodowanie url
        slon = slon.replace(".", "%2E");

		StringBuilder sb = new StringBuilder("http://overpass-api.de/api/interpreter?data=node%28around%3A");
        sb.append(radius);	//nie trzeba sie przejmowac zamiana zadnych znakow
        sb.append("%2C");
        
        sb.append(slat);
        sb.append("%2C");	//,
        sb.append(slon);
        sb.append("%29");	//)
        
        sb.append("%5B%22highway%22%3D%22bus%5Fstop%22%5D%3Bout%20body%3B%0A");
        return sb.toString();
		
	}
	
	public Vector<BusStop> get(double lat, double lon, int radius)
	{
        String url = constructURL(lat, lon, radius);

        OSMParser parser = new OSMParser();
        return parser.parse(url);
	}

	private void listInputStream(InputStream is)	//for test purposes
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		
		try
		{
			while ((inputLine = in.readLine()) != null)
			    Log.i("XML", inputLine);
			in.close();						
		}
		catch(Exception e)
		{
		}
	}
	
	public static void test()
	{
		//PKWiN
		double lat = 52.229863;
		double lon = 21.004915;
		int radius = 100;	//w metrach
		OSMClient osm = new OSMClient();
		Vector<BusStop> stops = osm.get(lat, lon, radius);
		
		if(stops == null)
			Log.i("test", "null stops !!!");
		
		Log.i("test", "stops: ");
		
		for(BusStop bs : stops)
		{
			Log.i("test", bs.toString());
		}
		//Log.i("test", "=-=-=-=-");
	}
}