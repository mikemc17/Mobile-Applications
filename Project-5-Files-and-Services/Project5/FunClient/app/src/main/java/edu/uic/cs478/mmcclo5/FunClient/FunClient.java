package edu.uic.cs478.mmcclo5.FunClient;
import cs478.mmcclo5.clientConnect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;


public class FunClient extends AppCompatActivity
{
    ArrayAdapter listAdapter;
    ListView list;
    ArrayList<String> logs = new ArrayList<>();

    Boolean isBound = false;
    clientConnect myConnection;
    Button b,play,pause,stop,resume;
    EditText text,song_text;
    Bitmap mypic;
    ImageView img;
    MediaPlayer player;
    Intent in;
    private final static String logFile = "client_log.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button)findViewById(R.id.pic_button);
        text = (EditText) findViewById(R.id.text);
        song_text = (EditText)findViewById(R.id.song);
        img = (ImageView)findViewById(R.id.imageView2);
        play = (Button)findViewById(R.id.play);
        pause = (Button)findViewById(R.id.pause);
        stop = (Button)findViewById(R.id.stop);
        resume = (Button)findViewById(R.id.resume);
        list = (ListView)findViewById(R.id.log);
        listAdapter = new ArrayAdapter<>(this,R.layout.text_view,logs);
        list.setAdapter(listAdapter);


        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                int num = Integer.parseInt(text.getText().toString());
                try
                {
                    if(num >= 0 && num < 5)
                    {
                        mypic = myConnection.getPicture(num);
                        img.setImageBitmap(mypic);
                        logs.add(0,"pic " + num);
                        listAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(FunClient.this,"Please enter a number between 0 and 5.",
                                                                         Toast.LENGTH_SHORT).show();
                    }

                }
                catch(RemoteException r)
                {
                    Log.i("getPicture exception", r.getMessage());
                }
            }

        });


        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                int num = Integer.parseInt(song_text.getText().toString());

                if(num >= 0 && num < 3)
                {
                    try
                    {
                        myConnection.playAudio(num);
                        logs.add(0, "clip " + num);
                        listAdapter.notifyDataSetChanged();
                    }
                    catch (RemoteException r)
                    {
                        Log.i("getPicture exception", r.getMessage());
                    }
                }
                else
                    Toast.makeText(FunClient.this,"Choose a number between 0 and 2",Toast.LENGTH_SHORT).show();
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                try
                {
                    myConnection.pauseAudio();
                }
                catch(Exception e)
                {
                    Log.i("audio pause exception", e.getMessage());
                }
            }
        });


        resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                try
                {
                    myConnection.resumeAudio();
                }
                catch (Exception e)
                {
                    Log.i("audio resume exception", e.getMessage());
                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                try
                {
                    myConnection.stopAudio();
                }
                catch (Exception e)
                {
                    Log.i("audio stop exception", e.getMessage());
                }
            }
        });


        //create a new file if it doesn't exist
        if (!getFileStreamPath(logFile).exists())
        {
            try
            {
                writeFile();
            }
            catch (FileNotFoundException e)
            {
                Log.i("FILE ERROR", "FileNotFoundException");
            }
        }


        // Read the data from the text file
        try
        {
            readFile();
        }
        catch (IOException e)
        {
            Log.i("FILE READ ERROR", "IOException");
        }

    }


    @Override
    protected void onStart()
    {
        super.onStart();
        in = new Intent(clientConnect.class.getName());
        ResolveInfo info = getPackageManager().resolveService(in,Context.BIND_AUTO_CREATE);
        in.setComponent(new ComponentName(info.serviceInfo.packageName,info.serviceInfo.name));
        bindService(in,this.mConnection,Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onPause()
    {
        if (isBound)
        {
            unbindService(this.mConnection);
        }

        try
        {
            writeFile();
        }
        catch (FileNotFoundException e)
        {
            Log.i("FILE ERROR", "FileNotFoundException");
        }
2
        super.onPause();
    }


    @Override
    protected void onDestroy()
    {
        stopService(in);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (!isBound)
        {
            in = new Intent(clientConnect.class.getName());
            ResolveInfo info = getPackageManager().resolveService(in, Context.BIND_AUTO_CREATE);
            in.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            bindService(in, this.mConnection, Context.BIND_AUTO_CREATE);
        }
    }


    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder myService)
        {
            myConnection = clientConnect.Stub.asInterface(myService);
            isBound = true;

        }

        public void onServiceDisconnected(ComponentName className)
        {
            myConnection = null;
            isBound = false;
        }
    };


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater in = getMenuInflater();
        in.inflate(R.menu.options_menu,menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        logs.clear();
        listAdapter.notifyDataSetChanged();
        return true;
    }


    private void writeFile() throws FileNotFoundException
    {

        FileOutputStream fos = openFileOutput(logFile, MODE_PRIVATE);

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));

        for(int j = 0; j < logs.size(); j++)
        {
            pw.println(logs.get(j));
        }
        pw.close();

    }


    private void readFile() throws IOException
    {

        FileInputStream fis = openFileInput(logFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line;

        while (null != (line = br.readLine()))
        {
            logs.add(line);
        }

        listAdapter.notifyDataSetChanged();
        br.close();
    }

}


