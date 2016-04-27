package tw.b1ame.smartiptv;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import tw.b1ame.smartiptv.application.App;
import tw.b1ame.smartiptv.fragments.PlaylistFragment;
import tw.b1ame.smartiptv.models.Interactor;
import tw.b1ame.smartiptv.models.Playlist;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager mViewPager;
    private Interactor interactor;
    private List<Playlist> playlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.interactor = ((App) getApplication()).getInteractor();

        setUI();

        downloadAndShowFirstPage();
    }

    private void downloadAndShowFirstPage() {
        this.interactor.getPlayList("http://iptv.servzp.pp.ua/pl/vimpel/day/128.68.252.62.m3u", playlist1 -> {
            this.playlists.add(playlist1);
            this.sectionsPagerAdapter.notifyDataSetChanged();
        });
    }

    private void setUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PlaylistFragment playlistFragment = new PlaylistFragment();
            playlistFragment.setPlaylist(playlists.get(position));
            return playlistFragment;
        }

        @Override
        public int getCount() {
            return playlists.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
