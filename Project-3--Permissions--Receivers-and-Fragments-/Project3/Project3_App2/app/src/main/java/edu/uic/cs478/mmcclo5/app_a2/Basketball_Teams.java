package edu.uic.cs478.mmcclo5.app_a2;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Basketball_Teams extends ListFragment
{
    private ListSelectionListener mListener = null;
    ListView v;

    public interface ListSelectionListener
    {
        void onListSelection(int index);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id)
    {

        // Indicates the selected item has been checked
        getListView().setItemChecked(pos, true);

        // Inform the QuoteViewerActivity that the item in position pos has been selected
        mListener.onListSelection(pos);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mListener = (ListSelectionListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.team_text, null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.team_text,SportsApp.basketball_teams));

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


}
