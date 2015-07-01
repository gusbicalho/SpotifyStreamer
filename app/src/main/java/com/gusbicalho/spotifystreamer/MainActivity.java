package com.gusbicalho.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;


public class MainActivity extends AppCompatActivity implements ArtistSearchFragment.Callback, ArtistDetailFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DETAILFRAGMENT_TAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTwoPane = findViewById(R.id.fragment_artist_detail_container) != null;
        if (!mTwoPane)
            ((ArtistSearchFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_artist_search))
                    .setListChoiceMode(ListView.CHOICE_MODE_NONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_search, menu);
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
    public void onArtistSelected(Artist artist) {
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_artist_detail_container,
                            ArtistDetailFragment.createInstance(artist.id, artist.name),
                            DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, ArtistDetailActivity.class);
            detailIntent.putExtra(ArtistDetailActivity.EXTRA_NAME, artist.name);
            detailIntent.putExtra(ArtistDetailActivity.EXTRA_ID, artist.id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onTrackSelected(Track track) {
        Intent detailIntent = new Intent(this, PlayerActivity.class);
        detailIntent.putExtra(PlayerActivity.EXTRA_ID, track.id);
        startActivity(detailIntent);
    }
}
