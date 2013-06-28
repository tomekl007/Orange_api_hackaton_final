package com.example.openstreetmaps;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.BusStop;
import Data.TextHelper;
import android.util.Log;

public class OSMParser
{
	public Vector<BusStop> parse(String url)
	{
		Vector<BusStop> output = new Vector<BusStop>();
		
		try
        {
	        //Log.i("xml", "=====");
	        Document doc = Jsoup.connect(url).ignoreContentType(true).get();
	        //Document doc = Jsoup.connect(url).get();	        	
	        //Log.i("xml", "---------- ");

	        Elements nodes = doc.select("node");
	        
        	String slat = null;
        	String slon = null;
        	String name = null;

	        Log.i("xml", "nodes: " + nodes.size());
        	
        	for (Element node : nodes) 
	        {	        	
	        	slat = new String(node.attr("lat"));
	        	slon = new String(node.attr("lon"));
	        	name = null;
	        	
	        	for(Element e : node.children())
	        		if(e.attr("k").equals("name"))
	        			name = new String(e.attr("v"));
	        }
        
	        String preformatted_name = (TextHelper.parseString(name)).replace("dworzec ", "dw.");
	        int pos = preformatted_name.lastIndexOf(' ');
	        BusStop bs = BusStop.getByName(preformatted_name.substring(0, pos));
	        bs.latitude = Double.valueOf(slat);
	        bs.longitude = Double.valueOf(slon);
	        
        	output.add(bs);
        }
        catch(Exception e)
        {
        	Log.i("xml", "exception: " + e);
        }	        		
	
		return output;
	}
}
