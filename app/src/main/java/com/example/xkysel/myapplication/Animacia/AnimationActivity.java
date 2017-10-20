package com.example.xkysel.myapplication.Animacia;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.example.xkysel.myapplication.R;
import com.example.xkysel.myapplication.TrajectoryProjectile;

import java.util.ArrayList;

public class AnimationActivity extends AppCompatActivity {
    private ImageView _imageView;
    private ArrayList<Double> _axisX;
    private ArrayList<Double> _axisY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        if (getIntent().hasExtra("Projectile")) {
            TrajectoryProjectile _projectile = (TrajectoryProjectile) getIntent().getSerializableExtra("Projectile");
            _axisX = _projectile.get_Xaxis();
            _axisY = _projectile.get_Yaxis();
        }
        _imageView = (ImageView) findViewById(R.id.anim_image);

        int[] location = new int[2];
        _imageView.getLocationOnScreen(location);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        final ValueAnimator valueAnimatorFor = ValueAnimator.ofInt(0, 100);

        valueAnimatorFor.setDuration(5000);
        valueAnimatorFor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int index = (int) valueAnimator.getAnimatedValue();
                _imageView.setX(_axisX.get(index).floatValue() * 10);
                _imageView.setY(height -  180 - (_axisY.get(index).floatValue() * 10));
            }
        });

        valueAnimatorFor.start();
    }
}
