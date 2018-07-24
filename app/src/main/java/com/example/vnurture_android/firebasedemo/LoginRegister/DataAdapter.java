package com.example.vnurture_android.firebasedemo.LoginRegister;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vnurture_android.firebasedemo.R;

import java.util.ArrayList;
import java.util.List;

class DataAdapter extends ArrayAdapter<DataModel> {
    private Activity context;
    List<DataModel> artists;

    public DataAdapter(DataStoreActivity dataStoreActivity, ArrayList<DataModel> list) {
        super(dataStoreActivity, R.layout.custom_layout,list);

        this.context = dataStoreActivity;
        this.artists = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_layout, null, true);

        TextView mail = (TextView) listViewItem.findViewById(R.id.c_mail);
        TextView name = (TextView) listViewItem.findViewById(R.id.c_name);

        DataModel artist = artists.get(position);
        mail.setText(artist.getEmail());
        name.setText(artist.getName());
        return listViewItem;
    }


}
