package Route;

import java.util.Vector;

import Data.BusStop;
import Data.Line;
import android.util.Pair;

public class Node extends Indexed
{
	BusStop bs;
	public Vector<Edge> predecessors = new Vector<Edge>();	//this simple model of network will work well with Dijkstra
	public Vector<Edge> successors = new Vector<Edge>();
	
	public int distance = Integer.MAX_VALUE;
	public boolean inHeap = false;
	
	public boolean start_node = false;
	
	public Node(BusStop _bs)
	{
		super();
		
		bs = _bs;
		//bs.getLines();	//no exact need to run this here, but it's nice&clean to do so
//		bs.ensureFullData();
	}
	
	public Vector<Pair<BusStop, String>> neighbours()
	{
		Vector<Pair<BusStop, String>> output = new Vector<Pair<BusStop, String>>();
		
		for(Line line : bs.getLines())	//as full data is ensured, there's everything needed here
		{
			Vector<BusStop> next_stops = line.nextStops(bs);
			
			for(BusStop b : next_stops)
			{
				//sprawdzanie czy nie ma juz w output lub predecessors
//				if(output.contains(b))	//predeessors !!!
//					continue;
				
				output.add(new Pair<BusStop, String>(b, line.getName()));
			}
		}
		
		return output;
	}

	public void addPre(Node n, int val, String line)
	{
		for(Edge e : predecessors)
			if(e.to == n)
				break;	//don't add when it's already here
		
//		if(!predecessors.contains(n))
			predecessors.add(new Edge(n, val, line));
	}

	public void addSuc(Node n, int val, String line)
	{
		for(Edge e : successors)
			if(e.to == n)
				break;	//don't add when it's already here

		//		if(!successors.contains(n))
			successors.add(new Edge(n, val, line));
	}
	
	public static Vector<Node> toNodes(Vector<BusStop> stops)
	{
		Vector<Node> output = new Vector<Node>();
		
		for(BusStop _bs : stops)
			output.add(new Node(_bs));
		
		return output;
	}

	protected boolean relax(Edge e)
	{
		if(e.to.distance > distance + e.value)
		{
			e.to.distance = distance + e.value;
			return true;
		}
	
		return false;
	}
	
	public String toString()
	{
		return bs.name;
	}
}
