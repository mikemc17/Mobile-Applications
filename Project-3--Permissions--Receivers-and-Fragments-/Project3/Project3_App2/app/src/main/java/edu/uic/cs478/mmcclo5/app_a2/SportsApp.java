package edu.uic.cs478.mmcclo5.app_a2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class SportsApp extends AppCompatActivity implements Baseball_Teams.ListSelectionListener,
                                                            Basketball_Teams.ListSelectionListener
{
    static ArrayList<String> baseball_teams = new ArrayList<>();
    static ArrayList<String> baseball_urls = new ArrayList<>();
    static ArrayList<String> basketball_teams = new ArrayList<>();
    static ArrayList<String> basketball_urls = new ArrayList<>();
    private final WebViewFragment web = new WebViewFragment();
    private FragmentManager FragManager;
    private FrameLayout TeamLayout, WebLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    Bundle site = new Bundle();
    static boolean isBaseball;
    FragmentTransaction fragTransaction;
    ListView v;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        // Get the string arrays with the teams and urls
        baseball_teams.addAll(Arrays.asList(getResources().getStringArray(R.array.baseball_teams)));
        basketball_teams.addAll(Arrays.asList(getResources().getStringArray(R.array.basketball_teams)));
        basketball_urls.addAll(Arrays.asList(getResources().getStringArray(R.array.basketball_urls)));
        baseball_urls.addAll(Arrays.asList(getResources().getStringArray(R.array.baseball_urls)));
        v = (ListView)findViewById(R.id.list);
        setContentView(R.layout.main);

        // Get references to the TeamFragment and to the WebFragment
        TeamLayout = (FrameLayout) findViewById(R.id.team_fragment_container);
        WebLayout = (FrameLayout) findViewById(R.id.web_fragment_container);


        // Get a reference to the FragmentManager
        FragManager = getFragmentManager();

        // Start a new FragmentTransaction
       fragTransaction = FragManager.beginTransaction();

        if(getIntent().getAction().equals("baseball"))
        {
            fragTransaction.replace(R.id.team_fragment_container, new Baseball_Teams());
            isBaseball = true;
        }
        else if(getIntent().getAction().equals("basketball"))
        {
            Toast.makeText(this,"action = basketball",   Toast.LENGTH_SHORT).show();
            fragTransaction.replace(R.id.team_fragment_container,new Basketball_Teams());
            isBaseball = false;
        }


        // Commit the FragmentTransaction
        fragTransaction.commit();

        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        FragManager
                .addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
                    }
                });
    }


    @Override
     public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater in = getMenuInflater();
        in.inflate(R.menu.actionbar,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.Baseball:
                fragTransaction.replace(R.id.team_fragment_container,new Baseball_Teams());
                isBaseball = true;
                break;

            case R.id.Basketball:
                fragTransaction.replace(R.id.team_fragment_container, new Basketball_Teams());
                isBaseball = false;
                break;

        }
        return true;
    }

    
    private void setLayout() 
    {
        // Determine whether the QuoteFragment has been added
        if (!web.isAdded()) {

            // Make the TitleFragment occupy the entire layout 
            TeamLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            WebLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));
        }
        else
        {

            // Make the TitleLayout take 1/3 of the layout's width
            TeamLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 1f));

            // Make the QuoteLayout take 2/3's of the layout's width
            WebLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 2f));
        }
    }
    

    // Called when the user selects an item in the TeamFragment
    @Override
    public void onListSelection(int index) 
    {
        if (isBaseball)
            site.putString("site","http://" + baseball_urls.get(index));
        else
            site.putString("site","http://nba.com/" + basketball_urls.get(index));

        web.setArguments(site);

        // If the QuoteFragment has not been added, add it now
        if (!web.isAdded())
        {

            // Start a new FragmentTransaction
            FragmentTransaction fragmentTransaction = FragManager
                    .beginTransaction();

            // Add the WebFragment to the layout
            fragmentTransaction.add(R.id.web_fragment_container,
                    web);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(null);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            FragManager.executePendingTransactions();
        }

    }

}
