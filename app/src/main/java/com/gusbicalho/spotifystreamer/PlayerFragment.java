package com.gusbicalho.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerFragment extends Fragment
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String LOG_TAG = PlayerFragment.class.getSimpleName();
    public static final String ARGUMENT_TRACK_ID = "ARGUMENT_TRACK_ID";
    private SpotifyService mSpotifyService = new SpotifyApi().getService();
    private String mTrackId;
    private Track mTrack;
    private TextView mArtistTextView;
    private TextView mAlbumTextView;
    private ImageView mAlbumImageView;
    private TextView mTrackTextView;
    private TextView mDurationTextView;
    private TextView mProgressTextView;
    private ProgressBar mProgressBar;
    private MediaPlayer mPlayer;
    private Timer mTimer;

    public static PlayerFragment createInstance(String trackId) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_TRACK_ID, trackId);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerFragment() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARGUMENT_TRACK_ID, mTrackId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mTrackId = getArguments().getString(ARGUMENT_TRACK_ID);
        } else {
            mTrackId = savedInstanceState.getString(ARGUMENT_TRACK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        mArtistTextView = ((TextView)rootView.findViewById(R.id.fragment_player_artist_textView));
        mAlbumTextView = ((TextView)rootView.findViewById(R.id.fragment_player_album_textView));
        mAlbumImageView = ((ImageView)rootView.findViewById(R.id.fragment_player_album_imageView));
        mTrackTextView = ((TextView)rootView.findViewById(R.id.fragment_player_track_textView));
        mDurationTextView = ((TextView)rootView.findViewById(R.id.fragment_player_duration_textView));
        mProgressTextView = ((TextView)rootView.findViewById(R.id.fragment_player_progress_textView));
        mProgressBar = ((ProgressBar)rootView.findViewById(R.id.fragment_player_progress_progressBar));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mTrack == null)
            new GetTrackInfoTask().execute(mTrackId);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTrack != null)
            setTrack(mTrack);
    }

    private class GetTrackInfoTask extends AsyncTask<String, Void, Track> {

        @Override
        protected void onPreExecute() {
            View rootView = getView();
            assert rootView != null;
            View contentView = rootView.findViewById(R.id.fragment_player_content);
            contentView.setVisibility(View.GONE);
            View loadingBar = rootView.findViewById(R.id.fragment_player_loadingBar);
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Track doInBackground(String... params) {
            String trackId = params[0];
            HashMap<String, Object> query = new HashMap<>();
            query.put(SpotifyService.COUNTRY, "BR");
            return mSpotifyService.getTrack(trackId);
        }

        @Override
        protected void onPostExecute(Track track) {
            setTrack(track);
            View rootView = getView();
            assert rootView != null;
            View contentView = rootView.findViewById(R.id.fragment_player_content);
            contentView.setVisibility(View.VISIBLE);
            View loadingBar = rootView.findViewById(R.id.fragment_player_loadingBar);
            loadingBar.setVisibility(View.GONE);
        }
    }

    private String formatArtists(List<ArtistSimple> artists) {
        if (artists.size() == 0)
            return "";
        if (artists.size() == 1)
            return artists.get(0).name;
        String arts = artists.get(0).name;
        for (ArtistSimple a : artists.subList(1,artists.size()))
            arts = getString(R.string.format_artist_list, arts, a.name);
        return arts;
    }

    private String formatTime(int segs) {
        return DateUtils.formatElapsedTime(segs);
    }

    protected void setTrack(Track track) {
        mTrack = track;
        if (mPlayer != null)
            mPlayer.release();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTrack == null) {
            mArtistTextView.setText(null);
            mAlbumTextView.setText(null);
            mAlbumImageView.setImageURI(null);
            mTrackTextView.setText(null);
            mDurationTextView.setText(null);
            mProgressTextView.setText(null);
            mProgressBar.setIndeterminate(true);
            mPlayer = null;
        }
        mArtistTextView.setText(formatArtists(mTrack.artists));
        mAlbumTextView.setText(mTrack.album.name);
        mTrackTextView.setText(mTrack.name);
        if (mTrack.album.images.size() > 0)
            Picasso.with(getActivity()).load(mTrack.album.images.get(0).url).into(mAlbumImageView);
        else
            mAlbumImageView.setImageURI(null);
        mDurationTextView.setText(formatTime(30));
        mProgressTextView.setText(formatTime(0));
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(30);
        mProgressBar.setProgress(0);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(mTrack.preview_url);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            Toast.makeText(getActivity(), R.string.streaming_error_message, Toast.LENGTH_SHORT);
            setTrack(null);
        }
        mTimer = new Timer();
        mTimer.schedule(mMediaObserver, 0L, 1000L);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.prepareAsync();
        Log.v(LOG_TAG, "Track set, waiting for prepare... "+mTrack.uri);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.v(LOG_TAG, "onPrepared");
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(LOG_TAG,
                (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED ?
                        "MEDIA_ERROR_SERVER_DIED " :
                        "MEDIA_ERROR_UNKNOWN ") +
                (extra == MediaPlayer.MEDIA_ERROR_IO ? "MEDIA_ERROR_IO" :
                 extra == MediaPlayer.MEDIA_ERROR_MALFORMED ? "MEDIA_ERROR_MALFORMED" :
                 extra == MediaPlayer.MEDIA_ERROR_UNSUPPORTED ? "MEDIA_ERROR_UNSUPPORTED" :
                 extra == MediaPlayer.MEDIA_ERROR_TIMED_OUT ? "MEDIA_ERROR_TIMED_OUT" :
                 "ERROR_OTHER"));
        return false;
    }

    TimerTask mMediaObserver = new TimerTask() {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int progress = mProgressBar.getProgress();
                    if (mPlayer != null && mPlayer.isPlaying() &&
                            progress < mProgressBar.getMax()) {
                        mProgressBar.setProgress(mProgressBar.getProgress() + 1);
                        mProgressTextView.setText(formatTime(progress + 1));
                    }
                }
            });
        }
    };
}
