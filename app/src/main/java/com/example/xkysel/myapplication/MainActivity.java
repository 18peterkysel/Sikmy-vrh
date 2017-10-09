package com.example.xkysel.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xkysel.myapplication.AdapterScreen.TableActivity;

public class MainActivity extends AppCompatActivity {
    private EditText _angle_editText;
    private EditText _speed_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _angle_editText = (EditText) findViewById(R.id.ET_angle);
        _speed_editText = (EditText) findViewById(R.id.ET_speed);
        Button startButton = (Button) findViewById(R.id.button_start);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStartButton();
            }
        });
    }

    private void onClickStartButton() {
        Intent intent = new Intent(this, TableActivity.class);
        int angle = Integer.valueOf(_angle_editText.getText().toString());
        int speed = Integer.valueOf(_speed_editText.getText().toString());

        if (angle > 180) {
            Toast.makeText(this, "Uhol nemoze byt vacsi ako 180 stupnov !", Toast.LENGTH_SHORT).show();
        } else {
            TrajectoryProjectile projectile = new TrajectoryProjectile(angle, speed);
            projectile.calculateDistanceTraveled();
            projectile.calculateTimeOfFlight();

            intent.putExtra("Projectile", projectile);
            startActivity(intent);
        }
    }
}
