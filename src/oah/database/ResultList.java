package oah.database;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import com.example.openstreetmaps.R;
import oah.model.Task;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/11/13
 * Time: 5:54 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * activity presenting history of task submitted by user
 */
public class ResultList extends Activity {


    private static String TAG=ResultList.class.getCanonicalName();
    DatabaseHelper databaseHelper;
    HistoryAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        ListView listView = (ListView )findViewById(R.id.results_list);
        //listView.addHeaderView(new TextView("todays tasks"));

        databaseHelper = new DatabaseHelper(this);
        List<Task> tasks = databaseHelper.getAllRecors();
        System.out.println(tasks);

        Cursor cursor = databaseHelper.getRecordsForDate(new Date());
        adapter = new HistoryAdapter(this, cursor);
        listView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.result_list, menu);
        return true;
    }
}