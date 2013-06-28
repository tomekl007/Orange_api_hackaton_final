package ZTM;

import java.io.InputStream;

import rest.RESTClient;
import android.util.Log;

public class ZTMClient extends RESTClient
{
	protected String constructURL(int line)
	{
		//przyk³ad http://m.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&q=115#content
		StringBuilder sb = new StringBuilder("http://m.ztm.waw.pl/rozklad_nowy.php?c=182&l=1&q=");
		sb.append(line);
		sb.append("#content");
		
		return sb.toString();
	}
	
	public void getStopsForLine(int line)
	{
		String url = constructURL(line);
	
		InputStream is = sendRequest(url);
	
		//listInputStream(is);
		
		ZtmHtmlParser parser = new ZtmHtmlParser();
/*		
		try
		{
			parser.parse(is);			
		}
		catch(Exception e)
		{
			Log.i("Html", "exception:" + e.getMessage());
		}
*/		
		consumeContent();

	}
}
