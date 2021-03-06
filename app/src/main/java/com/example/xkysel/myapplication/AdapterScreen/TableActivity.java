package com.example.xkysel.myapplication.AdapterScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xkysel.myapplication.Animacia.AnimationActivity;
import com.example.xkysel.myapplication.GraphScreen.GraphActivity;
import com.example.xkysel.myapplication.R;
import com.example.xkysel.myapplication.TrajectoryProjectile;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {
    private TrajectoryProjectile _projectile;
    private TextView _totalDistance;
    private TextView _totalHeight;
    private TextView _totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarTable);

        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu);

        // get intent
        _projectile = (TrajectoryProjectile) getIntent().getSerializableExtra("Projectile");

        // get info bar views
        _totalDistance = (TextView) findViewById(R.id.TW_total_distance);
        _totalHeight = (TextView) findViewById(R.id.TW_total_height);
        _totalTime = (TextView) findViewById(R.id.TW_total_time);

        // set info bar
        setInfoBar();

        // set adapter
        ArrayList<ItemOfAdapter> items = returnItems();
        ItemsAdapter itemsAdapter = new ItemsAdapter(this, items);
        ListView listView = (ListView) findViewById(R.id.items_listView);

        listView.setAdapter(itemsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_chart:
                startGraphActivity();
                break;
            case R.id.nav_anim:
                startAnimationActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private ArrayList<ItemOfAdapter> returnItems(){
        ArrayList<ItemOfAdapter> items = new ArrayList<>();
        ArrayList<Double> axisX = _projectile.get_Xaxis();
        ArrayList<Double> axisY = _projectile.get_Yaxis();
        ArrayList<Double> timeArray = _projectile.get_listOfTimes();

        for (int i = 0; i < axisX.size(); i++) {
            items.add(new ItemOfAdapter(timeArray.get(i), axisX.get(i), axisY.get(i)));
        }

        return items;
    }

    private void setInfoBar() {
        String totalDistance = String.format("%.2f", _projectile.get_distanceTraveled());
        String totaTime = String.format("%.2f", _projectile.get_timeOfFlight());
        String totalHeight = String.format("%.2f", _projectile.get_highestHeight());

        _totalDistance.setText(totalDistance);
        _totalHeight.setText(totalHeight);
        _totalTime.setText(totaTime);
    }

    private void startGraphActivity() {
        Intent intent = new Intent(this, GraphActivity.class);

        intent.putExtra("Projectile", _projectile);
        startActivity(intent);
    }

    private void startAnimationActivity() {
        Intent intent = new Intent(this, AnimationActivity.class);

        intent.putExtra("Projectile", _projectile);
        startActivity(intent);
    }
}
