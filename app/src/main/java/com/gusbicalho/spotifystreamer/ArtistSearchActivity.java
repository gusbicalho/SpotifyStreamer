package com.gusbicalho.spotifystreamer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import kaaes.spotify.webapi.android.models.Artist;


public class ArtistSearchActivity extends AppCompatActivity implements ArtistSearchFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);
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
        Intent detailIntent = new Intent(this, ArtistDetailActivity.class);
        detailIntent.putExtra(ArtistDetailActivity.EXTRA_NAME, artist.name);
        detailIntent.putExtra(ArtistDetailActivity.EXTRA_ID, artist.id);
        startActivity(detailIntent);
    }
}
