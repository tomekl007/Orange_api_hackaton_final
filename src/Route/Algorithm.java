package Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import Data.BusStop;
import Data.TextHelper;
import android.util.Log;
import android.util.Pair;

public class Algorithm 
{
	static int max_travels = 2;	//how many travels are accepted at most
	
	Map<BusStop, Node> bs_to_node = new HashMap<BusStop, Node>();	//to not mess BusStop more, although this solution will cost more
	
	Vector<Stack<Pair<Node, String>>> solutions = new Vector<Stack<Pair<Node, String>>>();
	
	protected Node getNode(BusStop bs)
	{
		if(!bs_to_node.containsKey(bs))
		{
			Node n = new Node(bs);
			bs_to_node.put(bs, n);
			return n;
		}
		else
			return bs_to_node.get(bs);
	}
		
	public void constructNetwork(Vector<BusStop> start, Vector<BusStop> end)
	{	//creates part of network, in which search will be performed
		Log.i("route", "construct networks");
		
		Vector<Node> start_nodes = Node.toNodes(start);
		Vector<Node> end_nodes = new Vector<Node>();
		Vector<Node> from = new Vector<Node>(start_nodes);
		Vector<Node> to = new Vector<Node>();
		Vector<Node> tmp = null;
		
		Vector<Node> all_nodes = new Vector<Node>(from);	//to keeps nodes from deleting
	
		Map<BusStop, Boolean> end_marks = new HashMap<BusStop, Boolean>();	//BS that were marked as achieved by algorithm
		
		for(int i = 0; i < max_travels; ++i)	//public transport network is constructed to be effective, so I'm not excepting more than 4-5 tranfers in any query
		{
			Log.i("route", i + " transfers");
			
			to.clear();
			
			for(Node n : from)
			{
				for(Pair<BusStop, String> pair : n.neighbours())
				{					
					if(start.contains(pair.first))	//don't create edges to start nodes
					{
						pair.first.start_bs = true;
						continue;
					}
					
					Node new_node = getNode(pair.first);	//create only when don't exists
					new_node.addPre(n, 1, pair.second);
					n.addSuc(new_node, 1, pair.second);
					
					if(end.contains(pair.first))	//end should be not big, O(n) won't hurt
					{
						if(!end_marks.containsKey(pair.first))	// to prevent multiple adding of one node to end_nodes [ O(lg n) ]
							end_nodes.add(new_node);
					
						end_marks.put(pair.first, true);
					}
					else
					{
						to.add(new_node);
					}

					all_nodes.add(new_node);
				}
			}
		
			tmp = from;	//swap references to 2 objects as they change their role
			from = to;
			to = tmp;
		}

		all_nodes.addAll(end_nodes);	//they're not added here as they're not added to 'to' which becames 'from'						
		
		Log.i("route", "start nodes");
		for(Node n : start_nodes)
			Log.i("route", n.toString());
		Log.i("route", "end nodes");
		for(Node n : end_nodes)
			Log.i("route", n.toString());
		if(!end_nodes.isEmpty())
			getPaths(start_nodes, end_nodes, all_nodes);
		else
			Log.i("route", "no end nodes");
	}	

	protected void getPaths(Vector<Node> start_nodes, Vector<Node> end_nodes, Vector<Node> all_nodes)
	{//network is already constructed, let's work
		for(Node n : start_nodes)
		{
			Log.i("route", "===== search for routes, start from " + n.toString());
		
			dijkstra(n, end_nodes, all_nodes);
		}
	}
		
	protected void dijkstra(Node start_node, Vector<Node> end_nodes, Vector<Node> all_nodes)
	{
		Heap<Node> heap = new Heap<Node>(new NodeComparator(), all_nodes.size());
		
		for(Node n : all_nodes)
		{
			n.distance = Integer.MAX_VALUE;	//'infinity'
			//n.distance = 99999;	//'infinity'
			n.inHeap = true;
			heap.insert(n);
		}

		Log.i("route", "heap size " + heap.size());		
		
		start_node.distance = 0;
		heap.repairUp(start_node);
		
		while(!heap.isEmpty())
		{
			Node best = heap.removeBest();
			best.inHeap = false;
			
			for(Edge suc : best.successors)
			{
				if(!suc.to.inHeap)
				{
					//Log.i("route", "not in heap: " + suc.toString().toString());
					continue;
				}
				
				if(best.relax(suc))
				{
					heap.upheap(suc.to.getIndex());
					//repairing changed heap data
					//relaxing can only decrease (make better) distance value, so only upheaps are taken into consideration
				}
			}
		}

		reconstructPaths(end_nodes);
	}

	protected void reconstructPaths(Vector<Node> end_nodes)	//by going by backward edges
	{
		//DFS
		Log.i("route", "reconstructPaths");
		RouteFinder rf = new RouteFinder(end_nodes, max_travels);
		rf.findRoutes();
		solutions.addAll(rf.getSolutions());
	}

	public Vector<Stack<Pair<Node, String>>> getSolutions()
	{
		return solutions;
	}
	
	public static void test()
	{
		Algorithm alg = new Algorithm();
		Vector<BusStop> start = new Vector<BusStop>(); 
		Vector<BusStop> end = new Vector<BusStop>(); 
		
		start.add(BusStop.getByName(TextHelper.parseString("Conrada")));
//		end.add(BusStop.getByName(TextHelper.parseString("PKP Kasprzaka")));
		end.add(BusStop.getByName(TextHelper.parseString("Pl.Zamkowy")));
		alg.constructNetwork(start, end);
		Vector<Stack<Pair<Node, String>>> sol = alg.getSolutions();
	
		for(Stack<Pair<Node, String>> solution : sol)
		{
			Log.i("solution", "route:");

			while(!solution.empty())
			{
				Pair<Node, String> pair = solution.pop();
				Log.i("solution", "on stop " + pair.first.toString() + " take line " + pair.second);
			}
		}	
	}
}