package edu.vt.cs.cs5254.gallery;

import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.vt.cs.cs5254.gallery.model.GalleryItemLab;

public class GalleryActivity extends AppCompatActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        updateFragment(GalleryFragment.newInstance());

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                GalleryItemLab.getInstance().refreshItems(
                        (GalleryItemLab.OnRefreshItemsListener) mFragment
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateFragment(Fragment fragment) {
        mFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_gallery:
                updateFragment(GalleryFragment.newInstance());
                return true;
            case R.id.navigation_search:
                return true;
            case R.id.navigation_map:
                updateFragment(PhotoMapFragment.newInstance());
                return true;
        }
        return false;
    };
}






