package com.example.xkysel.myapplication.AdapterScreen;


class ItemOfAdapter {
    private double _time;
    private double _distance;
    private double _height;

    ItemOfAdapter(double time, double distance, double height){
        this._time = time;
        this._distance = distance;
        this._height = height;
    }

    double get_time() {
        return _time;
    }

    double get_distance() {
        return _distance;
    }

    double get_height() {
        return _height;
    }
}
