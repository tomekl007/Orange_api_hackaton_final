package com.example.openstreetmaps;

import Data.BusStop;
import Data.Line;
import Data.TextHelper;
import Route.Algorithm;
import Route.Node;
import Threads.BusStopDataThread;
import Threads.LineDataThread;
import alchemyUtils.AlchemyAnalyzer;
import alchemyUtils.DestinationAlchemyAnalyzer;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import oah.database.DatabaseHelper;
import oah.database.ResultList;
import oah.model.Destination;
import oah.show_route.AlarmManagerUtils;

import java.util.Stack;
import java.util.Vector;

/**
 * acitivity responsible for creating new task, adding it to database,
 * and fires up analyze event
 */
public class CreateNewTask extends Activity 
{

    DatabaseHelper databaseHelper;

    //Analyzer analyzer;
    AlchemyAnalyzer alchemyAnalyzer;
    EditText viewToDo;
    AlarmManagerUtils alarmManagerUtils;
    static String TAG = CreateNewTask.class.getCanonicalName();

    Location currentLocation = null;
    
	LocationListener locationListener = new LocationListener() 
	{
		public void onLocationChanged(Location location) 
		{
			System.out.println("onlocationChanged");
			if(location == null){
			System.out.println("location is null!");
			}
			// Called when a new location is found by the network location provider.

			currentLocation = location;

//			makeUseOfNewLocation(currentLocation);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {}

		public void onProviderEnabled(String provider) {}

		public void onProviderDisabled(String provider) {}		
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_task);
        databaseHelper = new DatabaseHelper(this);
       // analyzer = new DestinationLocationAnalyzer();
        alchemyAnalyzer = new DestinationAlchemyAnalyzer();
        viewToDo = (EditText)findViewById(R.id.todoText);
        alarmManagerUtils = new AlarmManagerUtils(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_task, menu);
		return true;
	}

    /**
     * after adding record to database task is analyzed by AlechemyApi
     * then best route is find and alarm with activity presenting this best
     * route is scheduled
     * @param view
     */
	public void addRecordToDatabase(View view){

        System.out.println("add record to Database");
        //String name=LoadPreferences(USER_NAME);

        String toDoText = viewToDo.getText().toString();
        System.out.println("viewToDo : " + toDoText);

      //  Log.d(TAG, "adding : " + viewToDo.getText().toString());
        //String todoText = "i should go to School at 10:00";//viewToDo.getText().toString();

        if(!(toDoText.length()==0 || toDoText.equals("empty") ))
              databaseHelper.saveRecord(toDoText);


      //  analyzer.analyze(toDoText);
        Destination destination = (Destination) alchemyAnalyzer.analyze(toDoText);
        System.out.println("after analuzing get destination : " + destination);
        //tutaj wywolanie api wyszukujacego optymalna trase lineFInder(destination)
        String result = findBestRoute(destination);
            if(!result.equals(""))
                alarmManagerUtils.setAlarmAtSpecyficHour(0, 1 , result);
            else
                alarmManagerUtils.setAlarmAtSpecyficHour(0, 1 , "there is no best route for now");

        Intent intent = new Intent(this, ResultList.class);
        startActivity(intent);

	}
	
    private String findBestRoute(Destination destination) {

        Log.d(TAG, "findBestROute");

        destination.getPlace();
        
        BusStop.clearStaticData();
        Line.clearStaticData();

        BusStop.readIDsData();
        
        //PKWiN - for example purposes
        double lat = 52.229863;
		double lon = 21.004915;
        //lat = currentLocation.getLatitude();
        //lon = currentLocation.getLongitude()();
		
        OSMClient osm = new OSMClient();
        Vector<BusStop> local_stops = osm.get(lat, lon, 100);
        String dest = destination.getPlace();
        Vector<BusStop> targets = new Vector<BusStop>();
        targets.add(BusStop.getByName(TextHelper.parseString(dest)));
        
        LineDataThread.startThreads(20);
        BusStopDataThread.startThreads(2);
		
        Algorithm alg = new Algorithm();
		
		alg.constructNetwork(local_stops, targets);
		Vector<Stack<Pair<Node, String>>> sol = alg.getSolutions();

        String result = "";
		for(Stack<Pair<Node, String>> solution : sol)
		{
			Log.i("solution", "route:");

			while(!solution.empty())
			{
				Pair<Node, String> pair = solution.pop();
                result+="on stop " + pair.first.toString() + " take line " + pair.second+"\n";
				Log.i("solution", "on stop " + pair.first.toString() + " take line " + pair.second);
			}
		}
        return result;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseHelper.close();
    }


}
