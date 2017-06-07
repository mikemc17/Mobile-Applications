package edu.uic.cs478.mmcclo5.FunCenter;

import cs478.mmcclo5.clientConnect;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class FunCenter extends Service
{
    ArrayList<Integer> songs = new ArrayList<>(Arrays.asList(R.raw.aol,R.raw.joker,
                                                                        R.raw.sweet_home_alabama));

    ArrayList<Integer> pics = new ArrayList<>(Arrays.asList(R.drawable.car,R.drawable.dog,R.drawable.nes,
                                                                        R.drawable.ps3,R.drawable.uic));


    private final Binder binder = new Binder();
    Bitmap pic;
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent)
    {
        return myBinder;
    }


    @Override
    public void onCreate()
    {
        Log.i(TAG, "Service onCreate");
        Toast.makeText(this,"service created",Toast.LENGTH_LONG).show();

    }

    private final clientConnect.Stub myBinder = new clientConnect.Stub() {


        public void playAudio(int index)
        {
            synchronized (myBinder)
            {
                try
                {
                   player = MediaPlayer.create(FunCenter.this,songs.get(index));
                   player.start();
                }
                catch(Exception r)
                {
                    Log.i(TAG, "getAudioClip: exception ");
                }
            }
        }


        public void pauseAudio()
        {
            synchronized (myBinder)
            {
                try
                {
                    player.pause();
                }
                catch(Exception r)
                {
                    Log.i(TAG, "getAudioClip: exception ");
                }
            }
        }


        public void stopAudio()
        {
            synchronized (myBinder)
            {
                try
                {
                    player.stop();
                }
                catch(Exception r)
                {
                    Log.i(TAG, "getAudioClip: exception ");
                }
            }
        }


        public void resumeAudio()
        {
            int position;
            synchronized (myBinder)
            {
                try
                {
                   position = player.getCurrentPosition();
                   player.seekTo(position);
                   player.start();
                }
                catch(Exception r)
                {
                    Log.i(TAG, "getAudioClip: exception ");
                }
            }
        }


        public Bitmap getPicture(int index)
        {
            Log.i(TAG, "getPicture: ");

            synchronized (myBinder)
            {
                try
                {
                    pic = BitmapFactory.decodeResource(FunCenter.this.getResources(), pics.get(index));
                } catch (Exception r)
                {
                    Log.i(TAG, "getPicture: exception ");
                }
            }

            return pic;
        }

    };

}
