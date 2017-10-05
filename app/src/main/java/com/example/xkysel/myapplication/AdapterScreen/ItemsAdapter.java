package com.example.xkysel.myapplication.AdapterScreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.xkysel.myapplication.R;

import java.util.ArrayList;

class ItemsAdapter extends ArrayAdapter<ItemOfAdapter> {

    ItemsAdapter(Context context, ArrayList<ItemOfAdapter> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        // Get the data item for this position
        ItemOfAdapter itemOfAdapter = getItem(position);

        // Lookup view for data
        TextView time = convertView.findViewById(R.id.TW_time);
        TextView distance = convertView.findViewById(R.id.TW_distance);
        TextView height = convertView.findViewById(R.id.TW_height);

        //set data
        assert itemOfAdapter != null;
        time.setText(String.valueOf(itemOfAdapter.get_time()));
        distance.setText(String.valueOf(itemOfAdapter.get_distance()));
        height.setText(String.valueOf(itemOfAdapter.get_height()));

        return convertView;
    }
}
