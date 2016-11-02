package tw.b1ame.smartiptv;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.b1ame.smartiptv.application.App;
import tw.b1ame.smartiptv.fragments.AddPlaylistDialog;
import tw.b1ame.smartiptv.fragments.PlaylistFragment;
import tw.b1ame.smartiptv.models.Interactor;
import tw.b1ame.smartiptv.models.Playlist;

public class MainActivity extends AppCompatActivity implements AddPlaylistDialog.AddPlaylistListener {
    private SectionsPagerAdapter sectionsPagerAdapter;

    @BindView(R.id.container)
    ViewPager viewPager;

    private Interactor interactor;
    private List<Playlist> playlists = new ArrayList<>();

    @BindView(R.id.add_playlist)
    Button addPlaylist;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.interactor = ((App) getApplication()).getInteractor();

        setUI();

        getAllPlayLists();
    }

    private void getAllPlayLists() {
        Set<String> playListUrls = this.interactor.getPlayListsUrls();

        for (String url : playListUrls) {
            this.interactor.getPlayList(url, "noname", playlist -> {
                this.playlists.add(playlist);
                this.sectionsPagerAdapter.notifyDataSetChanged();
            });
        }
    }

    private void setUI() {
        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = ButterKnife.findById(this, R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        this.addPlaylist.setOnClickListener(v -> {
            DialogFragment addPlaylistDialog = new AddPlaylistDialog();
            addPlaylistDialog.show(getSupportFragmentManager(), "addPlaylistDialog");
        });
    }

    @Override
    public void onUserAddedPlaylist(String name, String url) {
        this.interactor.getPlayList(url, name, playlist1 -> {
            playlist1.setName(name);
            this.playlists.add(playlist1);
            this.sectionsPagerAdapter.notifyDataSetChanged();
        });
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
            return playlists.get(position).getName();
        }
    }
}
