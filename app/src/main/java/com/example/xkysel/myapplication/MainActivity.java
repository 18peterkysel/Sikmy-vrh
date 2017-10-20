package com.example.xkysel.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xkysel.myapplication.AdapterScreen.TableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private EditText _angle_editText;
    private EditText _speed_editText;
    private CheckBox _checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _angle_editText = (EditText) findViewById(R.id.ET_angle);
        _speed_editText = (EditText) findViewById(R.id.ET_speed);
        _checkBox = (CheckBox) findViewById(R.id.checkbox);

        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onClickStartButton();
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onClickStartButton() throws IOException, ExecutionException, InterruptedException {
        int angle = Integer.valueOf(_angle_editText.getText().toString());
        int speed = Integer.valueOf(_speed_editText.getText().toString());
        TrajectoryProjectile projectile = new TrajectoryProjectile(angle, speed);

        Intent intent = new Intent(this, TableActivity.class);

        if (angle > 89) {
            Toast.makeText(this, "Uhol nemoze byt vacsi ako 89 stupnov !", Toast.LENGTH_SHORT).show();
        } else {
            if (_checkBox.isChecked()) {
                projectile.calculateDistanceTraveled();
                projectile.calculateTimeOfFlight();
                projectile.calculate_X_Y_axis();
            } else {
                ArrayList<ArrayList<Double>> XYaxis;
                String link = "http://192.168.1.146:8080/getXY/angle=" + String.valueOf(angle) + "&speed="+ String.valueOf(speed);
                Download download = new Download("getXY");
                download.execute(link);

                while (!download.get().equals("FINISHED"));

                projectile.set_listOfX(download.get_xAxis());
                projectile.set_listOfY(download.get_yAxis());
            }
            intent.putExtra("Projectile", projectile);
            startActivity(intent);
        }
    }

    private class Download extends AsyncTask<String, Void, String> {
        private ArrayList<Double> _listOfTimes = new ArrayList<>();
        private ArrayList<Double> _xAxis;
        private ArrayList<Double> _yAxis;

        private String _downloadGoal;
        private String _distanceTraveled;
        private String _timeOfFlight;
        private String _highestHeight;


        Download(String downloadGoal) {
            this._downloadGoal = downloadGoal;
        }

        @Override
        protected String doInBackground(String... strings) {
            String link = strings[0];
            HttpURLConnection conn;
            URL url;
            try {
                url = new URL(link);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String jsonString = "",data;

                while ((data = reader.readLine()) != null){
                    jsonString += data + "\n";
                }

                if (_downloadGoal.equals("getXY") ) {
                    processXY(jsonString);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return "FINISHED";
        }

        private boolean processXY(String jsonString) throws JSONException {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray xAxisJSON = jsonObject.getJSONArray("1");
            JSONArray yAxisJSON = jsonObject.getJSONArray("0");

            _xAxis = new ArrayList<>();
            _yAxis = new ArrayList<>();

            for (int i = 0; i < xAxisJSON.length(); i++) {
                _xAxis.add(Double.valueOf(xAxisJSON.get(i).toString()));
                _yAxis.add(Double.valueOf(yAxisJSON.get(i).toString()));
            }
            return true;
        }

        public ArrayList<Double> get_xAxis() {
            return _xAxis;
        }

        public ArrayList<Double> get_yAxis() {
            return _yAxis;
        }
    }
}
