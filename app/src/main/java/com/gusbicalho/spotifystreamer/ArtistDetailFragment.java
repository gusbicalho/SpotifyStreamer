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
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistDetailFragment extends Fragment {
    public static final String ARGUMENT_ARTIST_NAME = "ARGUMENT_ARTIST_NAME";
    public static final String ARGUMENT_ARTIST_ID = "ARGUMENT_ARTIST_ID";
    private SpotifyService mSpotifyService = new SpotifyApi().getService();
    private String mArtistName, mArtistId;
    private TrackArrayAdapter mTrackListAdapter;

    public static ArtistDetailFragment createInstance(String artistId, String artistName) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_ARTIST_NAME, artistName);
        args.putString(ARGUMENT_ARTIST_ID, artistId);
        ArtistDetailFragment df = new ArtistDetailFragment();
        df.setArguments(args);
        return df;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtistName = getArguments().getString(ARGUMENT_ARTIST_NAME);
        mArtistId = getArguments().getString(ARGUMENT_ARTIST_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_artist_detail, container, false);

        mTrackListAdapter = new TrackArrayAdapter(getActivity(), new ArrayList<Track>());
        ListView artistSearchListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
        artistSearchListView.setAdapter(mTrackListAdapter);
        artistSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Callback) getActivity()).onTrackSelected(mTrackListAdapter.getItem(position));
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new SearchTracksTask().execute(mArtistId);
    }

    public interface Callback {
        public void onTrackSelected(Track track);
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
            return mSpotifyService.getArtistTopTrack(id, query).tracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            mTrackListAdapter.clear();
            mTrackListAdapter.addAll(tracks);
            View rootView = getView();
            ListView songListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
            songListView.setVisibility(View.VISIBLE);
            ProgressBar artistDetailProgressBar = (ProgressBar) rootView.findViewById(R.id.artistDetail_progressBar);
            artistDetailProgressBar.setVisibility(View.GONE);
        }
    }

}
