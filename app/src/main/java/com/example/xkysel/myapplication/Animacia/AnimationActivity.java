package com.example.xkysel.myapplication.Animacia;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.xkysel.myapplication.AdapterScreen.TableActivity;
import com.example.xkysel.myapplication.GraphScreen.GraphActivity;
import com.example.xkysel.myapplication.R;
import com.example.xkysel.myapplication.TrajectoryProjectile;

import java.util.ArrayList;

public class AnimationActivity extends AppCompatActivity {
    private ImageView _imageView;
    private ArrayList<Double> _axisX;
    private ArrayList<Double> _axisY;
    private TrajectoryProjectile _projectile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarAnim);

        setSupportActionBar(myToolbar);
        myToolbar.inflateMenu(R.menu.menu);

        if (getIntent().hasExtra("Projectile")) {
            _projectile = (TrajectoryProjectile) getIntent().getSerializableExtra("Projectile");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_table:
                startTableActivity();
                break;
            case R.id.nav_chart:
                starGraphActivity();
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

    private void starGraphActivity() {
        Intent intent = new Intent(this, GraphActivity.class);

        intent.putExtra("Projectile", _projectile);
        startActivity(intent);
    }
}
