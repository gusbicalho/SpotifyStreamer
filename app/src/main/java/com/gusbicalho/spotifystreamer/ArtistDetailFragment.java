package com.gusbicalho.spotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private static final String LOG_TAG = ArtistDetailFragment.class.getSimpleName();
    public static final String ARGUMENT_ARTIST_NAME = "ARGUMENT_ARTIST_NAME";
    public static final String ARGUMENT_ARTIST_ID = "ARGUMENT_ARTIST_ID";
    private SpotifyService mSpotifyService = new SpotifyApi().getService();
    private String mArtistName, mArtistId;
    private boolean mLoaded = false;
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARGUMENT_ARTIST_ID, mArtistId);
        outState.putString(ARGUMENT_ARTIST_NAME, mArtistName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mArtistName = getArguments().getString(ARGUMENT_ARTIST_NAME);
            mArtistId = getArguments().getString(ARGUMENT_ARTIST_ID);
            Log.v(LOG_TAG, "no savedInstanceState, loaded from arguments: "+mArtistName+" - "+mArtistId);
        } else {
            mArtistName = savedInstanceState.getString(ARGUMENT_ARTIST_NAME);
            mArtistId = savedInstanceState.getString(ARGUMENT_ARTIST_ID);
            Log.v(LOG_TAG, "loaded from savedInstanceState: "+mArtistName+" - "+mArtistId);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            assert actionBar != null;
            actionBar.setSubtitle(mArtistName);
        }
        super.onActivityCreated(savedInstanceState);
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
        if (!mLoaded)
            new SearchTracksTask().execute(mArtistId);
    }

    public interface Callback {
        void onTrackSelected(Track track);
    }

    private class SearchTracksTask extends AsyncTask<String, Void, List<Track>> {

        @Override
        protected void onPreExecute() {
            View rootView = getView();
            assert rootView != null;
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
            mLoaded = true;
            mTrackListAdapter.clear();
            mTrackListAdapter.addAll(tracks);
            View rootView = getView();
            assert rootView != null;
            ListView songListView = (ListView) rootView.findViewById(R.id.artistDetail_listView);
            songListView.setVisibility(View.VISIBLE);
            ProgressBar artistDetailProgressBar = (ProgressBar) rootView.findViewById(R.id.artistDetail_progressBar);
            artistDetailProgressBar.setVisibility(View.GONE);
        }
    }

}
