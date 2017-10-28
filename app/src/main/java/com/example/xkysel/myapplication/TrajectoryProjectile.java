package com.example.xkysel.myapplication;


import java.io.Serializable;
import java.util.ArrayList;

public class TrajectoryProjectile implements Serializable {
    private double _angle;
    private double _speed;
    private double _distanceTraveled;
    private double _highestHeight = 0;
    private double _timeOfFlight;

    private ArrayList<Double> _listOfX = new ArrayList<>();
    private ArrayList<Double> _listOfY = new ArrayList<>();
    private ArrayList<Double> _listOfTimes = new ArrayList<>();

    TrajectoryProjectile(double angle, double speed) {
        this._angle = angle;
        this._speed = speed;
    }

    void calculateDistanceTraveled() {
        _distanceTraveled = (Math.pow(_speed, 2) * Math.sin(2 * Math.toRadians(_angle))) / 9.81;
    }

    void calculateTimeOfFlight() {
        _timeOfFlight = (2 * _speed * Math.sin(Math.toRadians(_angle))) / 9.81;
    }

    void calculate_X_Y_axis() {
        double timeOfFlight = get_timeOfFlight();
        double increaseTime = timeOfFlight / 100;

        if (_distanceTraveled >= 0) {
            calculateXYtoPostiveDistance(timeOfFlight, increaseTime);
        } else {
            calculateXYfromNegativeDistance(timeOfFlight, increaseTime);
        }
    }

    private void calculateXYtoPostiveDistance(double timeOfFlight, double increaseTime) {
        for (double time = 0; time <= timeOfFlight; time += increaseTime) {
            if (time + increaseTime > timeOfFlight) {
                time = timeOfFlight;
            }
            double x = get_xInTime(time);
            double y = get_yInTime(time);

            _listOfX.add(x);
            _listOfY.add(y);
            _listOfTimes.add(time);
        }
    }

    private void calculateXYfromNegativeDistance(double timeOfFlight, double increaseTime) {
        for (double time = timeOfFlight; time >= 0; time -= increaseTime) {
            if (time - increaseTime < 0) {
                time = 0;
            }
            double x = get_xInTime(time);
            double y = get_yInTime(time);

            _listOfX.add(x);
            _listOfY.add(y);
            _listOfTimes.add(time);
        }
    }

    public double get_distanceTraveled() {
        return _distanceTraveled;
    }

    public double get_timeOfFlight() {
        return _timeOfFlight;
    }

    private double get_xInTime(double time) {
        return (_speed * time * Math.cos(Math.toRadians(_angle)));
    }

    private double get_yInTime(double time) {
        return (_speed * time * Math.sin(Math.toRadians(_angle))) - ((9.81 * Math.pow(time, 2)) / 2);
    }

    public double get_highestHeight() {
        if (_highestHeight != 0) {
            return _highestHeight;
        }
        return (Math.pow(_speed, 2) * Math.pow(Math.sin(Math.toRadians(_angle)), 2) / (2 * 9.81));
    }

    public ArrayList<Double> get_Xaxis() {
        return _listOfX;
    }

    public ArrayList<Double> get_Yaxis() {
        return _listOfY;
    }

    public ArrayList<Double> get_listOfTimes() {
        return _listOfTimes;
    }

    public void set_distanceTraveled(double _distanceTraveled) {
        this._distanceTraveled = _distanceTraveled;
    }

    public void set_timeOfFlight(double _timeOfFlight) {
        this._timeOfFlight = _timeOfFlight;
    }

    public void set_listOfX(ArrayList<Double> _listOfX) {
        this._listOfX = _listOfX;
    }

    public void set_listOfY(ArrayList<Double> _listOfY) {
        this._listOfY = _listOfY;
    }

    public void set_listOfTimes(ArrayList<Double> _listOfTimes) {
        this._listOfTimes = _listOfTimes;
    }

    public void set_highestHeight(double _highestHeight) {
        this._highestHeight = _highestHeight;
    }
}
