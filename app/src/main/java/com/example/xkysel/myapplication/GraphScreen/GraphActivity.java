package com.example.xkysel.myapplication.GraphScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.xkysel.myapplication.AdapterScreen.TableActivity;
import com.example.xkysel.myapplication.Animacia.AnimationActivity;
import com.example.xkysel.myapplication.R;
import com.example.xkysel.myapplication.TrajectoryProjectile;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
    private LineGraphSeries<DataPoint> _series;
    private TrajectoryProjectile _projectile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarGraph);

        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu);

        GraphView graph = (GraphView) findViewById(R.id.lineChart);

        _series = new LineGraphSeries<>();
        _series.setColor(Color.GREEN);
        graph.addSeries(_series);

        _projectile = (TrajectoryProjectile) getIntent().getSerializableExtra("Projectile");

        double distanceOfTraveled = _projectile.get_distanceTraveled();
        double highestHeight = _projectile.get_highestHeight();

        Viewport viewport = graph.getViewport();

        viewport.setXAxisBoundsManual(true);
        viewport.setYAxisBoundsManual(true);

        viewport.setMinY(0);
        viewport.setMaxY(highestHeight + 1);

        viewport.setMinX(0);
        viewport.setMaxX(distanceOfTraveled);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {

            @Override
            public void run() {
                ArrayList<Double> axisX = _projectile.get_Xaxis();
                ArrayList<Double> axisY = _projectile.get_Yaxis();

                for (int i = 0; i < axisX.size(); i++) {
                    double x = axisX.get(i);
                    double y = axisY.get(i);

                    _series.appendData(new DataPoint(x, y), false,  101);
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_table:
                startTableActivity();
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

    private void startTableActivity() {
        Intent intent = new Intent(this, TableActivity.class);

        intent.putExtra("Projectile", _projectile);
        startActivity(intent);
    }

    private void startAnimationActivity() {
        Intent intent = new Intent(this, AnimationActivity.class);

        intent.putExtra("Projectile", _projectile);
        startActivity(intent);
    }
}
