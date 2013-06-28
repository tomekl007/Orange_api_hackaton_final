package Route;

import android.util.Log;

public class Edge 
{
	public Node to;
	public int value;
	public String line;
	
	public Edge(Node t, int v, String l)
	{
		//Log.i("edge", "new edge for line " + line);
		
		to = t;
		value = v;
		line = new String(l);
	}
}
