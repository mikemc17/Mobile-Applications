package edu.uic.cs478.mmcclo5.app_a2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class MainReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context con, Intent in)
    {
        Intent sport = new Intent(con.getApplicationContext(), SportsApp.class);

        if(in.getAction().equals("baseball"))
        {
            //sport = new Intent(con.getApplicationContext(), BaseballApp.class);
            sport.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sport.setAction("baseball");
            con.startActivity(sport);
        }
        else if(in.getAction().equals("basketball")) 
        {
            Toast.makeText(con,"action = basketball",Toast.LENGTH_SHORT).show();
            //sport = new Intent(con.getApplicationContext(), BasketballApp.class);
            sport.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sport.setAction("basketball");
            con.startActivity(sport);

        }

    }

}
