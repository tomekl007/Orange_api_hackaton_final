package API;

import Data.BusStop;
import android.util.Log;
import com.example.openstreetmaps.MyApplication;
import rest.RESTClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CityData extends RESTClient
{		
	public static final String IDs_filename = "IDs2.xml";	//was IDs.xml
	
	protected String constructURLforAllIDs()
	{
		//"https://openmiddleware.pl/orange/oracle/ztm/busstop?busstop=%%%"
		StringBuilder sb = new StringBuilder("https://openmiddleware.pl:443/orange/oracle/ztm/busstop?busstop=%25%25%25");
		
		return sb.toString();
	}
	
	protected static String constructURLforLines(BusStop bs)
	{
		//https://host:port/orange/oracle/ztm/lines?busstopId=...&busstopNr=...
		//"https://openmiddleware.pl/orange/oracle/ztm/lines?busstopId=...&busstopNr=...
//		StringBuilder sb = new StringBuilder("https://openmiddleware.pl:443/orange/oracle/ztm/lines?busstopId%3D");
		StringBuilder sb = new StringBuilder("https://openmiddleware.pl:443/orange/oracle/ztm/lines?busstopId=");
		sb.append(bs.id);
		sb.append("&busstopNr=");
		sb.append("01");	//what to do with it ?
		Log.i("busstop", sb.toString());
		return sb.toString();
	}
	
	protected static String linesFilename(BusStop bs)
	{
		return bs.name + ".dat";	//was .xml
	}

	public static File linesFile(BusStop bs)
	{
		File lines_dir = MyApplication.getAppContext().getDir("bs_lines", MyApplication.getAppContext().MODE_PRIVATE);
		File file = new File(lines_dir, CityData.linesFilename(bs));		
		return file;
	}
	
	public static File IDsFile()
	{
		File file = new File(MyApplication.getAppContext().getFilesDir(), IDs_filename);
		return file;
	}
	
	public void getLinesToFile(BusStop bs)
	{
		getDataToFile(constructURLforLines(bs), linesFile(bs));
	}
	
	public void getIDsToFile(String IDs_filename) throws IOException
	{
		MyApplication.getAppContext().getFilesDir();
		getDataToFile(constructURLforAllIDs(), CityData.IDsFile());
	}

	private static void print(String msg, Object... args) {
        //System.out.println(String.format(msg, args));
		Log.i("Html", String.format(msg, args));
	}

	public void testFile(String IDs_filename)
	{
		File file = new File(MyApplication.getAppContext().getFilesDir(), IDs_filename);
	
		try
		{
			InputStream inputStream = new FileInputStream(file);			
			listInputStream(inputStream);
		}
		catch(Exception e)
		{
			Log.i("citydata", "exception: " + e.getMessage());
		}
		 
	}
}

