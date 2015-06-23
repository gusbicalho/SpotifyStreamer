package com.gusbicalho.spotifystreamer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Gustavo on 23/06/2015.
 */
public class ArtistArrayAdapter extends ArrayAdapter<Artist> {

    public ArtistArrayAdapter(Context context, List<Artist> objects) {
        super(context, R.layout.list_item_artist, R.id.list_item_artist_textView, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = super.getView(position, convertView, parent);
        Artist artist = getItem(position);
        ((TextView) listItemView.findViewById(R.id.list_item_artist_textView)).setText(artist.name);
        ImageView thumb = (ImageView) listItemView.findViewById(R.id.list_item_artist_imageView);
        if (artist.images.size() > 0)
            Picasso.with(getContext()).load(artist.images.get(0).url).into(thumb);
        else
            thumb.setImageURI(null);
        return listItemView;
    }
}
