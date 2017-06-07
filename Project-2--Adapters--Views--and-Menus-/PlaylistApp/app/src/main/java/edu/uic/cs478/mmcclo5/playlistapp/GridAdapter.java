package edu.uic.cs478.mmcclo5.playlistapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Mike on 2/19/2017.
 */

public class GridAdapter extends BaseAdapter {

    private static final int PADDING = 8;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private Context con;
    private List<Integer> coverIds;


    public GridAdapter(Context c, List<Integer> ids)
    {
        con = c;
        this.coverIds = ids;
    }


    @Override
    public int getCount()
    {
        return coverIds.size();
    }


    @Override
    public Object getItem(int position)
    {
        return coverIds.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return coverIds.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView img = (ImageView) convertView;

        if	(img	==	null)	{
            img	=	new	ImageView(con);
            img.setLayoutParams(new	GridView.LayoutParams(WIDTH,	HEIGHT));
            img.setPadding(PADDING,	PADDING,	PADDING,	PADDING);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        img.setImageResource(coverIds.get(position));

        return img;
    }

}
