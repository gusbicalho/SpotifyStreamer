package com.gusbicalho.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends Fragment {

    public static final String ARGUMENT_TRACK_ID = "ARGUMENT_TRACK_ID";
    private String mTrackId;

    public static PlayerActivityFragment createInstance(String trackId) {
        PlayerActivityFragment fragment = new PlayerActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_TRACK_ID, trackId);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTrackId = getArguments().getString(ARGUMENT_TRACK_ID);
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        ((TextView)rootView.findViewById(R.id.player_fragment_textView)).setText(mTrackId);
        return rootView;
    }
}
