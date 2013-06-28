package com.example.openstreetmaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import oah.database.DatabaseHelper;
import oah.database.ResultList;

/**
 * activity in which user get simple menu with two options for now
 */
public class MainActivity extends Activity {



    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Injector injector = Guice.createInjector(new LuceneModule());
        //  System.out.println("-> injector : " + injector);
        // databaseHelper = new DatabaseHelper(this);
        //   databaseHelper.bogusDatabase();//for now init with some data
        //   indexer = new SimpleIndexer();//injector.getInstance(Indexer.class);
        //  searcher = new DestinationLocationSearcher();
        //   System.out.println("init indexer thread for : " + indexer);
        //   initIndexerThread();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void goToCreatingNewTask(View view){
        Intent intent = new Intent(this,CreateNewTask.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); it clear history
        startActivity(intent);

    }

    public void goToResultList(View view){
        Intent intent = new Intent(this,ResultList.class);
        startActivity(intent);

    }




}
