package edu.uic.cs478.mmcclo5.playlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mike on 2/17/2017.
 */

public class SongGridActivity extends AppCompatActivity
{
    ArrayList<String> song_list = new ArrayList<>();
    //String[] song_list;
    GridAdapter grid_Adapter;
    GridView songs;

    //arraylist of ids for the pictures
    ArrayList<Integer> ids = new ArrayList<>(Arrays.asList(
            R.drawable.friends_in_low_places, R.drawable.boot_scootin_boogie, R.drawable.achy_breaky_heart,
            R.drawable.tulsa_time, R.drawable.dont_rock_the_jukebox, R.drawable.forever_and_ever_amen,
            R.drawable.the_shake, R.drawable.fast_as_you, R.drawable.red_solo_cup, R.drawable.thats_my_job,
            R.drawable.god_blessed_texas, R.drawable.prop_me_up));

    //array of urls for the songs
    String[] urls;

    //array of urls for the artists' wikipedia page
    String[] artist_page;

    //array of urls for the songs' wikipedia page
    String[] song_page;

    // arraylist that gets its items from the array sent by the intent
    ArrayList<Integer> playlist = new ArrayList<>();

    //arraylist of the names of the songs (for checking which was selected from main activity)
    ArrayList<String> song_names = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_activity);

        song_names.addAll(Arrays.asList(getResources().getStringArray(R.array.song_names)));
        urls = getResources().getStringArray(R.array.urls);
        artist_page = getResources().getStringArray(R.array.artist_page);
        song_page = getResources().getStringArray(R.array.song_page);

        song_list = getIntent().getExtras().getStringArrayList("song list");
        //Log.i("array check","song_list[0] = " + song_list[0]);
        playlist = getSelectedSongs(song_list);
        grid_Adapter = new GridAdapter(this,playlist);
        songs = (GridView)findViewById(R.id.gridview);
        songs.setAdapter(grid_Adapter);
        songs.setLongClickable(true);
        registerForContextMenu(songs);

        songs.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "";

                for(int i = 0;i < 6; i++)
                {
                    if(id == ids.get(i))
                    {
                        url = urls[i];
                        break;
                    }
                }


                Intent in = new Intent(view.getContext(),WebActivity.class);
                in.putExtra("url",url);
                startActivity(in);
            }
        });


    }

    AdapterView.AdapterContextMenuInfo info;
    @Override
    public void onCreateContextMenu(ContextMenu cm, View v, ContextMenu.ContextMenuInfo cm_info)
    {
        super.onCreateContextMenu(cm,v,cm_info);
        MenuInflater in = getMenuInflater();
        in.inflate(R.menu.context_menu,cm);

    }


    @Override
    public boolean onContextItemSelected(MenuItem m)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) m.getMenuInfo();
        String video_url = "",song_url = "",artist_url = "";
        Intent in;

        for(int i = 0;i < ids.size(); i++)
        {
            if(info.id == ids.get(i))
            {
                video_url = urls[i];
                song_url = song_page[i];
                artist_url = artist_page[i];
                break;
            }
        }

        if(m.getItemId() == R.id.play)
        {
            in = new Intent(SongGridActivity.this,WebActivity.class);
            in.putExtra("url",video_url);
            startActivity(in);
        }
        else if(m.getItemId() == R.id.song)
        {
            in = new Intent(SongGridActivity.this,WebActivity.class);
            in.putExtra("url",song_url);
            startActivity(in);
        }
        else if(m.getItemId() == R.id.artist)
        {
            in = new Intent(SongGridActivity.this,WebActivity.class);
            in.putExtra("url",artist_url);
            startActivity(in);
        }

        return true;
    }


    public ArrayList<Integer> getSelectedSongs(ArrayList<String> songs)
    {
        ArrayList<Integer> temp = new ArrayList<>();

        for(int i = 0; i < songs.size(); i++)
        {
            Log.i("outer", "getSelectedSongs: " + songs.size());
            for(int j = 0; j < song_names.size(); j++)
                if(songs.get(i).equals(song_names.get(j)))
                    temp.add(ids.get(j));

            /*if(songs.get(i).equals("Friends In Low Places"))
                temp.add(ids.get(0));
            else if(songs.get(i).equals("Boot Scootin Boogie"))
                temp.add(ids.get(1));
            else if(songs.get(i).equals("Achy Breaky Heart"))
                temp.add(ids.get(2));
            else if(songs.get(i).equals("Tulsa Time"))
                temp.add(ids.get(3));
            else if(songs.get(i).equals("Don't Rock The Jukebox"))
                temp.add(ids.get(4));
            else if(songs.get(i).equals("Forever And Ever Amen"))
                temp.add(ids.get(5));*/
        }

        return temp;
    }


}
