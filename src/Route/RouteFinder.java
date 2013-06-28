package Route;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;
import android.util.Pair;

public class RouteFinder 
{
	Vector<Node> end_nodes;
	int max_level;	//stack overflow is not a danger with this
	
	Vector<Stack<Pair<Node, String>>> solutions = new Vector<Stack<Pair<Node, String>>>(); 
	
	public RouteFinder(Vector<Node> end, int lev)
	{
		end_nodes = end;
		max_level = lev;
	}

	public void findRoutes()
	{	//start nodes have no predecessors
		for(Node end : end_nodes)
			findRoute(end, 0);
	}
	
	protected void findRoute(Node start, int level)
	{
		Log.i("route", "searching (backward) routes from " + start.toString());
		
		Stack<Pair<Node, String>> input = new Stack<Pair<Node, String>>();	//using stack as end nodes appear first here, and start nodes appear last
//		LinkedBlockingQueue<Pair<Node, String>> input = new LinkedBlockingQueue<Pair<Node, String>>();
		input.add(new Pair<Node, String>(start, "(finish)"));
		visit(start, level, input);
	}
	
	protected void visit(Node n, int level, Stack<Pair<Node, String>> route)
	{
		if(level > max_level)
			return;
		
		//Log.i("route", "visiting " + n);
		
		if(n.bs.start_bs || n.predecessors.isEmpty())	//start node has no predecessors, because edges to start nodes are not added
		{
			/*
			Log.i("route", "found route: " );
			
			Iterator<Pair<Node, String>> it = route.iterator();
			while(it.hasNext())
			{
				Pair<Node, String> pair = it.next();
				Log.i("route", pair.first.toString() + ", then go by line " + pair.second);
			}
			*/
			
			solutions.add(route);
		
			return;	//no need to go deeper
		}
		
		for(Edge e : n.predecessors)
		{
			//Log.i("route", "pre of '" + n.toString() + "(" + n.distance + ")' is '" + e.to.toString() + "(" + e.to.distance + ")'" + " by edge (" + e.value + ")[" + e.line + "]");			
			
			//start nodes are source (which has distance 0), so end nodes have > 0 distance. As here we start from end nodes (going backwards), distance will decrease
			if(e.value !=  n.distance - e.to.distance)
				continue;
						
			Stack<Pair<Node, String>> new_route = (Stack<Pair<Node, String>>)route.clone();	//copying pair references is OK here
			new_route.add(new Pair<Node, String>(e.to, e.line));
			//Log.i("route","===adding new node for line " + e.line);
			
			visit(e.to, level + 1, new_route);
		}
	}
	
	public Vector<Stack<Pair<Node, String>>> getSolutions()
	{
		return solutions;
	}
}
