package com.gusbicalho.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistDetailActivityFragment extends Fragment {

    private SpotifyService spotifyService = new SpotifyApi().getService();
    private String artistName, artistId;
    private TrackArrayAdapter trackListAdapter;

    public ArtistDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistName = getActivity().getIntent().getStringExtra(ArtistDetailActivity.EXTRA_NAME);
        artistId = getActivity().getIntent().getStringExtra(ArtistDetailActivity.EXTRA_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_artist_detail, container, false);

        trackListAdapter = new TrackArrayAdapter(getActivity(), new ArrayList<Track>());
        ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
        artistSearchListView.setAdapter(trackListAdapter);
        artistSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = trackListAdapter.getItem(position).name;
                Toast.makeText(getActivity(), listItem, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new SearchTracksTask().execute(artistId);
    }

    private class SearchTracksTask extends AsyncTask<String, Void, List<Track>> {

        @Override
        protected void onPreExecute() {
            View rootView = getView();
            ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
            artistSearchListView.setVisibility(View.GONE);
            ProgressBar artistSearchProgressBar = (ProgressBar) rootView.findViewById(R.id.artistDetail_progressBar);
            artistSearchProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Track> doInBackground(String... params) {
            String id = params[0];
            HashMap<String, Object> query = new HashMap<>();
            query.put(SpotifyService.COUNTRY, "BR");
            return spotifyService.getArtistTopTrack(id, query).tracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            trackListAdapter.clear();
            trackListAdapter.addAll(tracks);
            View rootView = getView();
            ListView songListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
            songListView.setVisibility(View.VISIBLE);
            ProgressBar artistDetailProgressBar = (ProgressBar) rootView.findViewById(R.id.artistDetail_progressBar);
            artistDetailProgressBar.setVisibility(View.GONE);
        }
    }

}
