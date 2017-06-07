package edu.uic.cs478.mmcclo5.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context con, Intent in)
    {
        if(in.getAction().equals("baseball"))
            Toast.makeText(con,"App 2 received Baseball Broadcast.",Toast.LENGTH_SHORT).show();
        else if(in.getAction().equals("basketball"))
            Toast.makeText(con,"App 2 received Basketball Broadcast.",Toast.LENGTH_SHORT).show();

    }

}
