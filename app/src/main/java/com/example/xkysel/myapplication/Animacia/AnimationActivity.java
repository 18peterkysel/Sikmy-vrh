package com.example.xkysel.myapplication.Animacia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.xkysel.myapplication.R;
import com.example.xkysel.myapplication.TrajectoryProjectile;

import java.util.ArrayList;

public class AnimationActivity extends AppCompatActivity {
    private TrajectoryProjectile _projectile;
    private ImageView imageView;
    private ArrayList<Double> _axisX;
    private ArrayList<Double> _axisY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        imageView = (ImageView) findViewById(R.id.animImage);
        if (getIntent().hasExtra("Projectile")) {
            _projectile = (TrajectoryProjectile) getIntent().getSerializableExtra("Projectile");
            _axisX = _projectile.get_Xaxis();
            _axisY = _projectile.get_Yaxis();
        }
        startAnimation();
    }

    public void startAnimation() {
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < _axisX.size(); i++) {
                    imageView.setX(_axisX.get(i).floatValue());
                    imageView.setY(_axisY.get(i).floatValue());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
