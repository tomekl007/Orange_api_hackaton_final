package com.example.openstreetmaps;

import java.util.Random;
import java.util.Vector;

import org.apache.commons.collections.BinaryHeap;

import Route.Heap;
import Route.Indexed;
import android.util.Log;

public class TestClass extends Indexed
{
	static Random r = new Random();
	
	public int value;
	
	public TestClass(int v)
	{
		value = v;
	}
	
	public void randomize()
	{
		value = r.nextInt(20);
	}
	
	public static void testHeap()
	{
		Heap<TestClass> heap = new Heap<TestClass>(new TestComparator(), 100);
		
		Vector<TestClass> v = new Vector<TestClass>();
		
		for(int i = 90; i  >= 0; --i)
		{
			TestClass a = new TestClass(i);
			v.add(a);
		}
		
		for(TestClass t : v)
			heap.insert(t);
	
		for(TestClass t : v)	//randomize after adding, it will destroy heap
			t.randomize();
		
		for(TestClass t : v)	//randomize after adding, it will destroy heap
			heap.repairUp(t);		
		
		while(!heap.isEmpty())
		{
			TestClass t = (TestClass)heap.removeBest();
			Log.i("test", "" + t.value);
		}
	}
}
