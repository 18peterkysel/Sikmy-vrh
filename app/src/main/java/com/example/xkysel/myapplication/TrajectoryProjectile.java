package com.example.xkysel.myapplication;


import java.io.Serializable;

public class TrajectoryProjectile implements Serializable {
    private double _angle;
    private double _speed;
    private double _distanceTraveled;
    private double _timeOfFlight;

    public TrajectoryProjectile(double angle, double speed) {
        this._angle = angle;
        this._speed = speed;
    }

    public void calculateDistanceTraveled() {
        _distanceTraveled = (Math.pow(_speed, 2) * Math.sin(2 * Math.toRadians(_angle))) / 9.81;
    }

    public void calculateTimeOfFlight() {
        _timeOfFlight = (2 * _speed * Math.sin(Math.toRadians(_angle))) / 9.81;
    }

    public double get_distanceTraveled() {
        return _distanceTraveled;
    }

    public double get_timeOfFlight() {
        return _timeOfFlight;
    }

    public double get_xInTime(double time) {
        return (_speed * time * Math.cos(Math.toRadians(_angle)));
    }

    public double get_yInTime(double time) {
        return (_speed * time * Math.sin(Math.toRadians(_angle))) - ((9.81 * Math.pow(time, 2)) / 2);
    }

    public double get_highestHeight() {
        return (Math.pow(_speed, 2) * Math.pow(Math.sin(Math.toRadians(_angle)), 2) / (2 * 9.81));
    }
}
