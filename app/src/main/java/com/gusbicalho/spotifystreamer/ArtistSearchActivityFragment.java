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
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;


public class ArtistSearchActivityFragment extends Fragment {
    private SpotifyService spotifyService = new SpotifyApi().getService();
    private ArtistArrayAdapter artistSearchListAdapter;

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

        artistSearchListAdapter = new ArtistArrayAdapter(getActivity(), new ArrayList<Artist>());
        ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistSearch_list_listView);
        artistSearchListView.setAdapter(artistSearchListAdapter);
        artistSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistSearchListAdapter.getItem(position);
                Intent detailIntent = new Intent(getActivity(), ArtistDetailActivity.class);
                detailIntent.putExtra(ArtistDetailActivity.EXTRA_NAME, artist.name);
                detailIntent.putExtra(ArtistDetailActivity.EXTRA_ID, artist.id);
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    private class SearchArtistTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected void onPreExecute() {
            View rootView = getView();
            ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistSearch_list_listView);
            artistSearchListView.setVisibility(View.GONE);
            ProgressBar artistSearchProgressBar = (ProgressBar) rootView.findViewById(R.id.artistSearch_progressBar);
            artistSearchProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            String search = params[0];
            return spotifyService.searchArtists(search).artists.items;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            artistSearchListAdapter.clear();
            artistSearchListAdapter.addAll(artists);
            View rootView = getView();
            ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistSearch_list_listView);
            artistSearchListView.setVisibility(View.VISIBLE);
            ProgressBar artistSearchProgressBar = (ProgressBar) rootView.findViewById(R.id.artistSearch_progressBar);
            artistSearchProgressBar.setVisibility(View.GONE);
        }
    }
}
