package edu.uic.cs478.mmcclo5.playlistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static edu.uic.cs478.mmcclo5.playlistapp.R.id.CheckAll;
import static edu.uic.cs478.mmcclo5.playlistapp.R.id.Invert;
import static edu.uic.cs478.mmcclo5.playlistapp.R.id.clear;
import static edu.uic.cs478.mmcclo5.playlistapp.R.id.create;

public class SongListActivity extends AppCompatActivity
{
    //arrays of the song names in forward and inverted form
    String[] song_names;

    //boolean check_boxes = false;
    boolean inverted = false;

    //array of the songs selected
    ArrayList<String> selected_songs = new ArrayList<>();

    ArrayAdapter listAdapter;
    ListView songs;
    Toast t;
    Button create_playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        create_playlist = (Button)findViewById(R.id.button);
        song_names = getResources().getStringArray(R.array.song_names);

        t = Toast.makeText(getApplicationContext(),"Please Select At Least One Song.",Toast.LENGTH_LONG);
        t.show();

        listAdapter = new ArrayAdapter<>(this,R.layout.checkbox,song_names);

        songs = (ListView) findViewById(R.id.songs);
        songs.setAdapter(listAdapter);

        create_playlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addSongToSelected();
                Intent grid = new Intent(SongListActivity.this,SongGridActivity.class) ;
                grid.putStringArrayListExtra("song list",selected_songs);
                startActivity(grid);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater in = getMenuInflater();
        in.inflate(R.menu.options_menu,menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()) {
            case create:
                try
                {
                    addSongToSelected();
                    Intent grid = new Intent(this,SongGridActivity.class) ;
                    grid.putStringArrayListExtra("song list",selected_songs);
                    startActivity(grid);
                }
                catch (Exception e)
                {
                    Log.e("case create", e.toString());
                }
                break;

            case CheckAll:
                clear_or_set_all_Checks(true);
                t.cancel();
                break;

            case Invert:
                if (inverted)
                    inverted = false;
                else
                    inverted = true;

                invert_all_checks();
                break;

            case clear:
                clear_or_set_all_Checks(false);
                t.show();
                break;
        }

        return  true;
    }


    public void addSongToSelected()
    {
        for(int i = 0; i < songs.getChildCount(); i++)
        {
            CheckBox cb = ((CheckBox) songs.getChildAt(i));
            if (cb.isChecked())
                selected_songs.add(cb.getText().toString());

        }
    }


    public void clear_or_set_all_Checks(boolean check)
    {
        for(int i = 0; i < songs.getChildCount(); i++)
        {
            ((CheckBox) songs.getChildAt(i)).setChecked(check);
        }

    }

    public void invert_all_checks()
    {
        CheckBox cb;

        for(int i = 0; i < songs.getChildCount(); i++)
        {
            cb = ((CheckBox) songs.getChildAt(i));
            if (cb.isChecked())
                cb.setChecked(false);
            else
                cb.setChecked(true);
        }

    }

}
