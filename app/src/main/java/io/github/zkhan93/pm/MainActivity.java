package io.github.zkhan93.pm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.detail_fragment_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id
                                .detail_fragment_container,
                        new DetailActivityFragment(), DetailActivityFragment.TAG).commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(getApplicationContext(), PreferenceActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemSelected(Intent intent) {
        if (mTwoPane) {
            DetailActivityFragment daf = (DetailActivityFragment) getSupportFragmentManager()
                    .findFragmentByTag(DetailActivityFragment.TAG);
            if (daf != null) {
                daf.displayMovieFromIntent(intent);
            } else {
                daf = new DetailActivityFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id
                        .detail_fragment_container, daf, DetailActivityFragment.TAG).commit();
            }
        } else {
            startActivity(intent);
        }
    }
}
