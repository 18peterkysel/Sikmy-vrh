package com.example.xkysel.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
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

    private TrajectoryProjectile _projectile;
    private EditText _angle_editText;
    private EditText _speed_editText;
    private Switch _switch;
    private boolean _responseError = false;
    private Intent _intent;

    protected ProgressBar _loadingWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _angle_editText = (EditText) findViewById(R.id.ET_angle);
        _speed_editText = (EditText) findViewById(R.id.ET_speed);
        _switch = (Switch) findViewById(R.id.main_switch);
        _loadingWheel = (ProgressBar) findViewById(R.id.progressBar);



        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    _responseError = false;
                    onClickStartButton();
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadingStart() {
        _loadingWheel.setVisibility(View.VISIBLE);
    }
    private void loadingFinish() {
        _loadingWheel.setVisibility(View.INVISIBLE);
    }

    private void onClickStartButton() throws IOException, ExecutionException, InterruptedException {
        String angle_value = _angle_editText.getText().toString();
        String speed_value = _speed_editText.getText().toString();

        if (angle_value.equals("") || speed_value.equals("")) {
            Toast.makeText(this, "Uhol a rychlost musia byt zadane !", Toast.LENGTH_SHORT).show();
            return;
        }

        int angle = Integer.valueOf(angle_value);
        int speed = Integer.valueOf(speed_value);
        _projectile  = new TrajectoryProjectile(angle, speed);

        _intent = new Intent(this, TableActivity.class);

        if (angle > 89) {
            Toast.makeText(this, "Uhol nemoze byt vacsi ako 89 stupnov !", Toast.LENGTH_SHORT).show();
        } else {
            //start loading
            loadingStart();

            if (!_switch.isChecked()) {
                _projectile.calculateDistanceTraveled();
                _projectile.calculateTimeOfFlight();
                _projectile.calculate_X_Y_axis();

                // finish loading
                loadingFinish();

                _intent.putExtra("Projectile", _projectile);
                startActivity(_intent);
            } else {
                String _ip = "192.168.137.1";
                String _port = "8080";
                final String link = "http://" + _ip + ":" + _port + "/";

                download_XYaxis(link, angle, speed);
                download_getListOfTimes(link);
                download_distanceTraveled(link);
                download_getTimeOfFlight(link);
                download_getHighestHeight(link);
            }
        }
    }

    private void printErrorDialog() {
        // finish loading
        loadingFinish();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Chyba pri pripojeni na server !").setTitle("Error");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void download_XYaxis(String link, int angle, int speed) {
        String getRequest = "getXY/angle=" + String.valueOf(angle) + "&speed="+ String.valueOf(speed);
        link = link + getRequest;
        final Download download = new Download("getXY");
        download.setListener(new ResponseListener() {
            @Override
            public void onResponse(String data) {
                if (_responseError) {
                    return;
                } else if (data.equals("Error")) {
                    _responseError = true;
                    printErrorDialog();
                } else if (data.equals("FINISHED")) {
                    _responseError = false;
                    _projectile.set_listOfX(download.get_xAxis());
                    _projectile.set_listOfY(download.get_yAxis());
                }

                if (!_responseError && !_projectile.get_Xaxis().isEmpty() &&
                        !_projectile.get_Yaxis().isEmpty() && !_projectile.get_listOfTimes().isEmpty() &&
                        _projectile.get_highestHeight() != -1 && _projectile.get_timeOfFlight() != -1 &&
                        _projectile.get_distanceTraveled() != -1) {
                    // finish loading
                    loadingFinish();

                    _intent.putExtra("Projectile", _projectile);
                    startActivity(_intent);
                }
            }
        });
        download.execute(link);
    }

    private void download_getListOfTimes(String link) {
        String getRequest = "getListOfTimes";
        link = link + getRequest;
        final Download download = new Download("getListOfTimes");
        download.setListener(new ResponseListener() {
            @Override
            public void onResponse(String data) {
                if (_responseError) {
                    return;
                } else if (data.equals("Error")) {
                    _responseError = true;
                    printErrorDialog();
                } else if (data.equals("FINISHED")) {
                    _responseError = false;
                    _projectile.set_listOfTimes(download.get_listOfTimes());
                }

                if (!_responseError && !_projectile.get_Xaxis().isEmpty() &&
                        !_projectile.get_Yaxis().isEmpty() && !_projectile.get_listOfTimes().isEmpty() &&
                        _projectile.get_highestHeight() != -1 && _projectile.get_timeOfFlight() != -1 &&
                        _projectile.get_distanceTraveled() != -1) {
                    // finish loading
                    loadingFinish();

                    _intent.putExtra("Projectile", _projectile);
                    startActivity(_intent);
                }
            }
        });
        download.execute(link);
    }

    private void download_distanceTraveled(String link) {
        String getRequest = "getDistanceTraveled";
        link = link + getRequest;
        final Download download = new Download("getDistanceTraveled");
        download.setListener(new ResponseListener() {
            @Override
            public void onResponse(String data) {
                if (_responseError) {
                    return;
                } else if (data.equals("Error")) {
                    _responseError = true;
                    printErrorDialog();
                } else if (data.equals("FINISHED")) {
                    _responseError = false;
                    _projectile.set_distanceTraveled(download.get_distanceTraveled());
                }

                if (!_responseError && !_projectile.get_Xaxis().isEmpty() &&
                        !_projectile.get_Yaxis().isEmpty() && !_projectile.get_listOfTimes().isEmpty() &&
                        _projectile.get_highestHeight() != -1 && _projectile.get_timeOfFlight() != -1 &&
                        _projectile.get_distanceTraveled() != -1) {
                    // finish loading
                    loadingFinish();

                    _intent.putExtra("Projectile", _projectile);
                    startActivity(_intent);
                }
            }
        });
        download.execute(link);
    }

    private void download_getTimeOfFlight(String link) {
        String getRequest = "getTimeOfFlight";
        link = link + getRequest;
        final Download download = new Download("getTimeOfFlight");
        download.setListener(new ResponseListener() {
            @Override
            public void onResponse(String data) {
                if (_responseError) {
                    return;
                } else if (data.equals("Error")) {
                    _responseError = true;
                    printErrorDialog();
                } else if (data.equals("FINISHED")) {
                    _responseError = false;
                    _projectile.set_timeOfFlight(download.get_timeOfFlight());
                }

                if (!_responseError && !_projectile.get_Xaxis().isEmpty() &&
                        !_projectile.get_Yaxis().isEmpty() && !_projectile.get_listOfTimes().isEmpty() &&
                        _projectile.get_highestHeight() != -1 && _projectile.get_timeOfFlight() != -1 &&
                        _projectile.get_distanceTraveled() != -1) {
                    // finish loading
                    loadingFinish();

                    _intent.putExtra("Projectile", _projectile);
                    startActivity(_intent);
                }
            }
        });
        download.execute(link);
    }

    private void download_getHighestHeight(String link) {
        String getRequest = "getHighestHeight";
        link = link + getRequest;
        final Download download = new Download("getHighestHeight");
        download.setListener(new ResponseListener() {
            @Override
            public void onResponse(String data) {
                if (_responseError) {
                    return;
                } else if (data.equals("Error")) {
                    _responseError = true;
                    printErrorDialog();
                } else if (data.equals("FINISHED")) {
                    _responseError = false;
                    _projectile.set_highestHeight(download.get_highestHeight());
                }

                if (!_responseError && !_projectile.get_Xaxis().isEmpty() &&
                        !_projectile.get_Yaxis().isEmpty() && !_projectile.get_listOfTimes().isEmpty() &&
                        _projectile.get_highestHeight() != -1 && _projectile.get_timeOfFlight() != -1 &&
                        _projectile.get_distanceTraveled() != -1) {
                    // finish loading
                    loadingFinish();

                    _intent.putExtra("Projectile", _projectile);
                    startActivity(_intent);
                }
            }
        });
        download.execute(link);
    }

    private class Download extends AsyncTask<String, Void, String> {
        private ArrayList<Double> _listOfTimes = new ArrayList<>();
        private ArrayList<Double> _xAxis;
        private ArrayList<Double> _yAxis;

        private String _downloadGoal;
        private Double _distanceTraveled;
        private Double _timeOfFlight;
        private Double _highestHeight;

        private ResponseListener _listener;

        Download(String downloadGoal) {
            this._downloadGoal = downloadGoal;
        }

        public void setListener(ResponseListener listener) {
            _listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                switch (_downloadGoal) {
                    case "getXY":
                        process_XY(jsonString);
                        break;
                    case "getListOfTimes":
                        process_listOfTimes(jsonString);
                        break;
                    case "getDistanceTraveled":
                        process_distanceTraveled(jsonString);
                        break;
                    case "getTimeOfFlight":
                        process_timeOfFlight(jsonString);
                        break;
                    case "getHighestHeight":
                        process_highestHeight(jsonString);
                        break;
                    default:
                        return "Error";
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return "Error";
            }
            return "FINISHED";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(_listener != null) {
                _listener.onResponse(s);
            }
        }

        private boolean process_XY(String jsonString) throws JSONException {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray xAxisJSON = jsonObject.getJSONArray("0");
            JSONArray yAxisJSON = jsonObject.getJSONArray("1");

            _xAxis = new ArrayList<>();
            _yAxis = new ArrayList<>();

            for (int i = 0; i < xAxisJSON.length(); i++) {
                _xAxis.add(Double.valueOf(xAxisJSON.get(i).toString()));
                _yAxis.add(Double.valueOf(yAxisJSON.get(i).toString()));
            }
            return true;
        }

        private boolean process_listOfTimes(String jsonString) throws JSONException {
            jsonString = jsonString.replace("[", "").replace("]", "");
            String[] splitJsonString = jsonString.split(",");

            _listOfTimes = new ArrayList<>();

            for (String aSplitJsonString : splitJsonString) {
                _listOfTimes.add(Double.valueOf(aSplitJsonString));
            }
            return true;
        }

        private boolean process_distanceTraveled(String jsonString) throws JSONException {
            this._distanceTraveled = Double.valueOf(jsonString);

            return true;
        }

        private boolean process_timeOfFlight(String jsonString) throws JSONException {
            this._timeOfFlight = Double.valueOf(jsonString);

            return true;
        }

        private boolean process_highestHeight(String jsonString) throws JSONException {
            this._highestHeight = Double.valueOf(jsonString);

            return true;
        }

        private ArrayList<Double> get_xAxis() {
            return _xAxis;
        }

        private ArrayList<Double> get_yAxis() {
            return _yAxis;
        }

        private ArrayList<Double> get_listOfTimes() {
            return _listOfTimes;
        }

        private Double get_distanceTraveled() {
            return _distanceTraveled;
        }

        private Double get_timeOfFlight() {
            return _timeOfFlight;
        }

        private Double get_highestHeight() {
            return _highestHeight;
        }
    }
}
