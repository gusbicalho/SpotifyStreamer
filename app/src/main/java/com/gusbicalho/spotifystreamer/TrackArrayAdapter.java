package com.gusbicalho.spotifystreamer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Gustavo on 23/06/2015.
 */
public class TrackArrayAdapter extends ArrayAdapter<Track> {

    public TrackArrayAdapter(Context context, List<Track> objects) {
        super(context, R.layout.list_item_track, R.id.list_item_track_textView, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = super.getView(position, convertView, parent);
        Track track = getItem(position);
        ((TextView) listItemView.findViewById(R.id.list_item_track_textView)).setText(track.name);
        return listItemView;
    }
}
