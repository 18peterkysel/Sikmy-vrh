package com.example.xkysel.myapplication.Animacia;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.xkysel.myapplication.R;
import com.example.xkysel.myapplication.TrajectoryProjectile;

import java.util.ArrayList;

public class AnimationActivity extends AppCompatActivity {
    private TrajectoryProjectile _projectile;
    private ImageView _imageView;
    private ArrayList<Double> _axisX;
    private ArrayList<Double> _axisY;
    private int _actionBarHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

//        TypedValue tv = new TypedValue();
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//        {
//            _actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
//        }

        if (getIntent().hasExtra("Projectile")) {
            _projectile = (TrajectoryProjectile) getIntent().getSerializableExtra("Projectile");
            _axisX = _projectile.get_Xaxis();
            _axisY = _projectile.get_Yaxis();
        }
        _imageView = (ImageView) findViewById(R.id.anim_image);

        int[] location = new int[2];
        _imageView.getLocationOnScreen(location);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.anim_layout);
        int widthLayout = relativeLayout.getWidth();
        int heightLayout = relativeLayout.getHeight();
        relativeLayout.getLocationOnScreen(location);
        float x = relativeLayout.getX();
        float y = relativeLayout.getY();

        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                Log.e("HA", "asdadsa");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.e("HA", "asdadsa");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Log.e("HA", "asdadsa");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Log.e("HA", "asdadsa");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Log.e("HA", "asdadsa");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Log.e("HA", "asdadsa");
                break;
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int density = displayMetrics.densityDpi;
        final float heightInDP = Math.round(height / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        float widthInDP = Math.round(width / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        Display mdisp = getWindowManager().getDefaultDisplay();
        final int maxX= mdisp.getWidth();
        final int maxY= mdisp.getHeight();


        final ValueAnimator valueAnimatorFor = ValueAnimator.ofInt(0, 100);
        valueAnimatorFor.setDuration(5000);
        valueAnimatorFor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int index = (int) valueAnimator.getAnimatedValue();
                _imageView.setX(_axisX.get(index).floatValue() * 10);
////                _imageView.setTranslationY(height/2);
                _imageView.setY(height -  180 - (_axisY.get(index).floatValue() * 10));
//                _axisY.get(index).floatValue()
            }
        });

        valueAnimatorFor.start();

//        startAnimation();
    }

    private void startAnimation(){

        for (int i =0 ; i < _axisX.size(); i++)     {
//            _imageView.animate().scaleX(_axisX.get(i).floatValue());
//            _imageView.animate().scaleY(_axisY.get(i).floatValue());
            _imageView.setX(_axisX.get(i).floatValue());
            _imageView.setY(_axisY.get(i).floatValue());
            _imageView.animate().setDuration(1000);
            _imageView.animate().start();
        }
    }
}
