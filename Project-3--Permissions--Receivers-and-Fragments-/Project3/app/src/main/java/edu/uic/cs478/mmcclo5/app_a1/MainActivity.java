package edu.uic.cs478.mmcclo5.app_a1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity
{
    static Button broadcast_baseball;
    static Button broadcast_basketball;
    static String permission = "edu.uic.cs478.project3";
    final int BASE = 1;
    final int BASKET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadcast_baseball = (Button)findViewById(R.id.button);
        broadcast_basketball = (Button)findViewById(R.id.button2);

        broadcast_baseball.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //checkPermission(1);
                if(checkPermission(BASE))
                {
                    Intent baseball = new Intent("baseball");
                    baseball.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendOrderedBroadcast(baseball, null);
                }
            }
        });

        broadcast_basketball.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //checkPermission(2);
                if(checkPermission(BASKET))
                {
                    Intent basketball = new Intent("basketball");
                    basketball.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendOrderedBroadcast(basketball, null);
                }
            }
        });

    }//end onCreate


    @Override
    public void onRequestPermissionsResult(int code, String[] permissions,int[] granted)
    {
        Intent baseball,basketball;
        switch(code)
        {
            case BASE:
            {
                if (granted.length > 0 && granted[0] == PackageManager.PERMISSION_GRANTED)
                {
                    baseball = new Intent("baseball");
                    baseball.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendOrderedBroadcast(baseball, null);
                }
            }

            case BASKET:
            {
                if (granted.length > 0 && granted[0] == PackageManager.PERMISSION_GRANTED )
                {
                    basketball = new Intent("basketball");
                    basketball.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendOrderedBroadcast(basketball, null);
                }
            }

        }

    }


    public boolean checkPermission(int code)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, 0);
        }
        return true;
    }



}
