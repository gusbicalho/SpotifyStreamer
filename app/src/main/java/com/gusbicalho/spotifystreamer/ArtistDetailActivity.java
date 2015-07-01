package com.gusbicalho.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import kaaes.spotify.webapi.android.models.Track;


public class ArtistDetailActivity extends AppCompatActivity implements ArtistDetailFragment.Callback {
    public static final String EXTRA_NAME = ArtistDetailActivity.class.getName()+".EXTRA_NAME";
    public static final String EXTRA_ID = ArtistDetailActivity.class.getName()+".EXTRA_ID";
    private static final String DETAILFRAGMENT_TAG = "DETAILFRAGMENT_TAG";
    private String artistName, artistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        artistName = getIntent().getStringExtra(EXTRA_NAME);
        artistId = getIntent().getStringExtra(EXTRA_ID);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_artist_detail_container,
                            ArtistDetailFragment.createInstance(artistId, artistName),
                            DETAILFRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public View onCreateView(View parent, String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrackSelected(Track track) {
        Toast.makeText(this, track.name, Toast.LENGTH_SHORT).show();
    }
}
