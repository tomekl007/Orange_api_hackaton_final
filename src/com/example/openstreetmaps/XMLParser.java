package com.example.openstreetmaps;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import Data.BusStop;
import Data.TextHelper;
import android.util.Log;
import android.util.Xml;
import java.util.Vector;

public class XMLParser //for parsing OSM data
{
	private static final String ns = null;
	
    public Vector<BusStop> parse(InputStream in) throws XmlPullParserException, IOException
    {
    	Vector<BusStop> output;
    	
    	try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            output = readOSM(parser);
        } 
    	finally {
            in.close();
        }

    	return output;
    }
    
    private Vector<BusStop> readOSM(XmlPullParser parser) throws XmlPullParserException, IOException {
    	Vector<BusStop> output = new Vector<BusStop>();
    	
    	parser.require(XmlPullParser.START_TAG, ns, "osm");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("node")) 
            {
                BusStop bs = readNode(parser);
                if(bs != null)
                	output.add(readNode(parser));
            } else {
                skip(parser);
            }
        }  
    
        return output;
    }
/*
    private BusStop readNode(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//Log.i("XML", "read node");
    	
    	BusStop output = null;
    	
//        String ID = parser.getAttributeValue(null, "id");
        String lat = parser.getAttributeValue(null, "lat");
        String lon = parser.getAttributeValue(null, "lon");

//        output.latitude = Double.valueOf(lat);	//troche zaokragla, mimo ze to double
 //       output.longitude = Double.valueOf(lon);
        
    	parser.require(XmlPullParser.START_TAG, ns, "node");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("tag")) 
            {
            	Log.i("XML", "found tag");
            	
    		    String key = parser.getAttributeValue(null, "k");
                String value = parser.getAttributeValue(null, "v");
             
                if(key.equals("name"))
                {
               	 Log.i("XML", "found name: " + value);
                 Log.i("XML", "" + lat + " " + lon);

               	 //output.name = new String(value);
                	int i = value.lastIndexOf(' ');
                	
                	String preformatted_name = (TextHelper.parseString(value.substring(0, i))).replace("dworzec ", "dw.");
                	output = BusStop.getByName(preformatted_name);	//without bus stop number
                	//if(output == null)
                	//	return null;
                	
                	output.latitude = Double.valueOf(lat);
                	output.longitude = Double.valueOf(lon);
                }
            	
            	parser.nextTag();
            } else {
                skip(parser);
            }
        }
        
        //Log.i("XML", output.toString());
        
        return output;
    }
*/ 
    private BusStop readNode(XmlPullParser parser) throws XmlPullParserException, IOException {
    	//Log.i("XML", "read node");
    	
        String ID = parser.getAttributeValue(null, "id");
        String lat = parser.getAttributeValue(null, "lat");
        String lon = parser.getAttributeValue(null, "lon");

        //Log.i("XML", ID + " " + lat + " " + lon);
        BusStop output = new BusStop("", "");
        
        output.id = ID;
        output.latitude = Double.valueOf(lat);	//troche zaokragla, mimo ze to double
        output.longitude = Double.valueOf(lon);
        
    	parser.require(XmlPullParser.START_TAG, ns, "node");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("tag")) {
            	//Log.i("XML", "found tag");
            	
    		    String key = parser.getAttributeValue(null, "k");
                String value = parser.getAttributeValue(null, "v");
             
                if(key.equals("name"))
                {
               	 Log.i("XML", "found name: " + value);
               	 output.name = new String(value);
                }           	
            	
            	parser.nextTag();
            } else {
                skip(parser);
            }
        }
        
        Log.i("XML", output.toString());
        
        return output;
    }    

    
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
