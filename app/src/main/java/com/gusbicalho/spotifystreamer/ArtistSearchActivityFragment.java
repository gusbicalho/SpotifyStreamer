package com.gusbicalho.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistSearchActivityFragment extends Fragment {

    private ArrayAdapter<String> artistSearchListAdapter;

    public ArtistSearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        EditText searchEditText = (EditText) rootView.findViewById(R.id.artistSearch_search_editText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                new SearchArtistTask().execute(v.getText().toString());
                return true;
            }
        });

        artistSearchListAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.list_item_artist,
                R.id.list_item_artist_textView, new ArrayList<String>());
        ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistSearch_list_listView);
        artistSearchListView.setAdapter(artistSearchListAdapter);
        artistSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = artistSearchListAdapter.getItem(position);
                Intent detailIntent = new Intent(getActivity(), ArtistDetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, listItem);
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    private class SearchArtistTask extends AsyncTask<String, Void, String[]> {

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
                    "Coldplay", "Coldplay & Lele", "Coldplay & Rihanna",
                    "Various Artists - Coldplay Tribute", "Marcelo Cold Prey",
                    "Delícia de Coldplay", "Supercold Play", "Playstation One",
                    "Playstation Two", "Preisteixo"
            };
        }

        @Override
        protected void onPostExecute(String[] strings) {
            artistSearchListAdapter.clear();
            artistSearchListAdapter.addAll(strings);
            View rootView = getView();
            ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistSearch_list_listView);
            artistSearchListView.setVisibility(View.VISIBLE);
            ProgressBar artistSearchProgressBar = (ProgressBar) rootView.findViewById(R.id.artistSearch_progressBar);
            artistSearchProgressBar.setVisibility(View.GONE);
        }
    }
}
