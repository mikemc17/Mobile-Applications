package edu.uic.cs478.mmcclo5.app1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.URI;


public class MainActivity extends AppCompatActivity {

    String tag = "editor button";
    String tag2 = "browser button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button editor = (Button) findViewById(R.id.editor);
        final Button browser = (Button) findViewById(R.id.browser);


        editor.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    Intent i = new Intent(MainActivity.this,editor_activity.class) ;
                    startActivityForResult(i,1);
                }
                catch (Exception e)
                {
                    // Log any error messages to LogCat using Log.e()
                    Log.e(tag, e.toString());
                }
            }
        });

        browser.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/index.html"));
                    startActivity(browser);
                }
                catch (Exception e)
                {
                    // Log any error messages to LogCat using Log.e()
                    Log.e(tag2, e.toString());
                }
            }
        });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        final TextView returnText = (TextView) findViewById(R.id.returnText);

        if (requestCode == requestCode)
        {
            if (resultCode == RESULT_OK)
            {
                returnText.setText(data.getData().toString());
            }
        }
    }

}
