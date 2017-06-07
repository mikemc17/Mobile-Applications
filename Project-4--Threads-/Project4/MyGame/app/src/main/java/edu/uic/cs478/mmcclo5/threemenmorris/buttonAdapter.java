package edu.uic.cs478.mmcclo5.threemenmorris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * Created by Mike on 4/9/2017.
 */

public class buttonAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String[] button_text;

    public buttonAdapter(Context context, String[] text) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        button_text = text;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.buttonview, null);
        }

        Button button = (Button)convertView.findViewById(R.id.button);
        button.setText(button_text[position]);
        button.setClickable(false);
        button.setHeight(300);
        button.setWidth(300);

        return convertView;
    }

    @Override
    public int getCount() {
        return button_text.length;
    }

    @Override
    public Object getItem(int position) {
        return button_text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
