package com.example.xkysel.myapplication.GraphScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.xkysel.myapplication.AdapterScreen.TableActivity;
import com.example.xkysel.myapplication.R;
import com.example.xkysel.myapplication.TrajectoryProjectile;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends AppCompatActivity {
    private LineGraphSeries<DataPoint> _series;
    private TrajectoryProjectile _projectile;
    private double _distanceOfTraveled;
    private double _highestHeight;

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

        _distanceOfTraveled = _projectile.get_distanceTraveled();
        _highestHeight = _projectile.get_highestHeight();

        Viewport viewport = graph.getViewport();

        viewport.setXAxisBoundsManual(true);
        viewport.setYAxisBoundsManual(true);

        viewport.setMinY(0);
        viewport.setMaxY(_highestHeight + 1);

        if (_distanceOfTraveled >= 0) {
            viewport.setMinX(-_distanceOfTraveled);
            viewport.setMaxX(_distanceOfTraveled);
        } else {
            viewport.setMinX(_distanceOfTraveled);
            viewport.setMaxX(-_distanceOfTraveled);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {

            @Override
            public void run() {
                double timeOfFlight = _projectile.get_timeOfFlight();
                double increaseTime = timeOfFlight / 100;

                if (_distanceOfTraveled >= 0) {
                    for (double time = 0; time <= timeOfFlight; time += increaseTime) {
                        if (time + increaseTime > timeOfFlight) {
                            time = timeOfFlight;
                        }
                        double x = _projectile.get_xInTime(time);
                        double y = _projectile.get_yInTime(time);

                        _series.appendData(new DataPoint(x, y), false,  100);
                    }
                } else {
                    for (double time = timeOfFlight; time >= 0; time -= increaseTime) {
                        if (time - increaseTime < 0) {
                            time = 0;
                        }
                        double x = _projectile.get_xInTime(time);
                        double y = _projectile.get_yInTime(time);

                        _series.appendData(new DataPoint(x, y), false,  100);
                    }
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
                break;
            case R.id.nav_info:
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
}
