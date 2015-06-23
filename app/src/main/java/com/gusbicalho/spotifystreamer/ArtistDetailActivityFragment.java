package com.gusbicalho.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistDetailActivityFragment extends Fragment {

    private String artistName;
    private ArrayAdapter<String> songListAdapter;

    public ArtistDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistName = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_artist_detail, container, false);

        songListAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.list_item_artist,
                R.id.list_item_artist_textView, new ArrayList<String>());
        ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
        artistSearchListView.setAdapter(songListAdapter);
        artistSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = songListAdapter.getItem(position);
                Toast.makeText(getActivity(), listItem, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        new SearchSongsTask().execute(artistName);
    }

    private class SearchSongsTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            View rootView = getView();
            ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistSearch_list_listView);
            artistSearchListView.setVisibility(View.GONE);
            ProgressBar artistSearchProgressBar = (ProgressBar) rootView.findViewById(R.id.artistSearch_progressBar);
            artistSearchProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new String[]{
                    "Viva la Vida", "A Sky Full of Stories", "The Scientist",
                    "Fix You", "Yellow",
                    "Paradise", "Supermassive Buracororo", "Snarf"
            };
        }

        @Override
        protected void onPostExecute(String[] strings) {
            songListAdapter.clear();
            songListAdapter.addAll(strings);
            View rootView = getView();
            ListView songListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
            songListView.setVisibility(View.VISIBLE);
            ProgressBar artistDetailProgressBar = (ProgressBar) rootView.findViewById(R.id.artistDetail_progressBar);
            artistDetailProgressBar.setVisibility(View.GONE);
        }
    }

}
