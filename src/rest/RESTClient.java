package rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.FileHelper;
import android.util.Log;

public class RESTClient 
{
	HttpEntity to_consume = null;
	
	protected InputStream sendRequest(String url)
	{
		String username = "hackathon45";
		String password = "2VuChZrk";
		String login = username + ":" + password;
		String base64login = new String(Base64.encodeBase64(login.getBytes()));
		
		try 
        {
        	//Log.i("response", "URL: " + url);
			        		    
        	CookieStore cookieStore = new BasicCookieStore();
        	HttpContext localContext = new BasicHttpContext();
        	localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);        	
        	
        	//DefaultHttpClient httpclient = new DefaultHttpClient();
            
        	DefaultHttpClient httpclient = HttpsClientBuilder.getBelieverHttpsClient();
        	
        	HttpGet httpGet = new HttpGet(url);
        	httpGet.addHeader("Authorization", "Basic " + base64login);
//		    post.addHeader("Authorization", "Basic " + base64login);

            HttpResponse response1 = httpclient.execute(httpGet, localContext);
            
        	//System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            to_consume = entity1;
            InputStream is = entity1.getContent();
            
            return is;
        } 
        catch (Exception e)
        {
        	Log.i("rest", "Exception: " + e.getMessage());
        }
	
		return null;
	}

	protected InputStream sendRequestNoLogin(String url)
	{
		try 
        {
        	Log.i("rest", "URL: " + url);
			
			CookieStore cookieStore = new BasicCookieStore();
        	HttpContext localContext = new BasicHttpContext();
        	localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        	
        	DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response1 = httpclient.execute(httpGet, localContext);
            
        	//System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            to_consume = entity1;
            InputStream is = entity1.getContent();
            
            return is;
        } 
        catch (Exception e)
        {	
        	Log.i("rest", "Exception: " + e.getMessage());
        }		
	
		return null;
	}	
	public void consumeContent()
	{
		try
		{
	        to_consume.consumeContent();
		}
		catch(Exception e)
		{
			
		}
	}
	
	protected void listInputStream(InputStream is)
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
	
	protected void getDataToFile(String url, File file)
	{
		InputStream is = sendRequest(url);

		try
		{
			FileHelper.saveToFile(is, file);
		}
		catch(Exception e)
		{
			Log.i("rest", "exception: " + e.getMessage());
		}		
	}

	public static boolean checkForError(Document doc)	//with JSOUP Document
	{
		Elements error = doc.select("dataError");
		
		if(!error.isEmpty())	//there is error
	    {
	    	Log.i("rest", "error while downloading data from API");
	    	
	    	for(Element err : error)	//there will be max one
	    		for(Element e : err.children())
	    		{
	    			if(e.nodeName().equals("status"))
	    			{
	    				Log.i("rest", "status: " + e.text());
	    			}
	    			else if(e.nodeName().equals("description"))
	    			{
	    				Log.i("rest", "description: " + e.text());		    			
	    			}
	    		}
	    
	    	return true;
	    }
	
		return false;
	}
	
/*
	protected String constructURL()
	{
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
*/
}
