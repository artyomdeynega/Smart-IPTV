package tw.b1ame.smartiptv;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.b1ame.smartiptv.application.App;
import tw.b1ame.smartiptv.fragments.AddCustomFavoriteChannelDialog;
import tw.b1ame.smartiptv.fragments.AddPlaylistDialog;
import tw.b1ame.smartiptv.fragments.PlaylistFragment;
import tw.b1ame.smartiptv.models.Channel;
import tw.b1ame.smartiptv.models.Interactor;
import tw.b1ame.smartiptv.models.Playlist;
import tw.b1ame.smartiptv.utils.ChannelUtils;

public class MainActivity extends AppCompatActivity implements AddPlaylistDialog.AddPlaylistListener, PlaylistFragment.PlaylistFragmentEventsListener, AddCustomFavoriteChannelDialog.AddCustomFavoriteChannelListener {
    private SectionsPagerAdapter playlistsPagerAdapter;

    @BindView(R.id.container)
    ViewPager viewPager;

    private Interactor interactor;
    private List<Playlist> playlists = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.interactor = ((App) getApplication()).getInteractor();

        setUI();

        getAllSavedPlaylists();
    }

    private void getAllSavedPlaylists() {
        AsyncTask<Void, Void, Void> checkChannelALiveTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (Channel channel : playlists.get(0).getChannelList()) {
                    boolean isAlive = ChannelUtils.isChannelAlive(channel.getUrl());

                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), String.format("Channel %s alive=%b", channel.getName(), isAlive), Toast.LENGTH_SHORT).show());
                }

                return null;
            }
        };

        this.interactor.getAllStoredPlaylists(playlists1 -> {
            this.playlists.clear();
            this.playlists.add(this.interactor.getFavoritesPlaylist());
            this.playlists.addAll(playlists1);
            this.playlistsPagerAdapter.notifyDataSetChanged();

            checkChannelALiveTask.execute();
        });
    }

    private void setUI() {
        setSupportActionBar(toolbar);

        playlistsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(playlistsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                playlistsPagerAdapter.getRegisteredFragment(position).onThisFragmentBecameActive();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = ButterKnife.findById(this, R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(view -> {
            if (this.viewPager.getCurrentItem() == 0) {
                showAddCustomFavoriteChannel();
            } else {
                showAddPlaylist();
            }
        });
    }

    private void showAddPlaylist() {
        DialogFragment addPlaylistDialog = new AddPlaylistDialog();
        addPlaylistDialog.show(getSupportFragmentManager(), "addPlaylistDialog");
    }

    private void showAddCustomFavoriteChannel() {
        DialogFragment addPlaylistDialog = new AddCustomFavoriteChannelDialog();
        addPlaylistDialog.show(getSupportFragmentManager(), "addFavoriteChannelDialog");
    }

    @Override
    public void onUserAddedCustomFavChannel(String name, String url) {
        this.interactor.addCustomFavoriteChannel(name, url);
        this.playlistsPagerAdapter.getRegisteredFragment(0).refreshChannels();
    }

    private void removePlaylist() {
        int currentItem = this.viewPager.getCurrentItem();
        if (currentItem == 0) {
            Toast.makeText(this, "Нельзя удалить избранное!", Toast.LENGTH_LONG).show();
        } else {
            viewPager.setCurrentItem(currentItem - 1);
            Playlist playlistToRemove = this.playlists.get(currentItem);
            this.interactor.removePlaylist(playlistToRemove);
            this.playlists.remove(playlistToRemove);
            this.playlistsPagerAdapter.notifyDataSetChanged();
        }
    }

    private PlaylistFragment getTopPlaylistFragment() {
        return this.playlistsPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
    }

    @Override
    public void onUserAddedPlaylist(String name, String url) {
        this.interactor.getPlayList(url, name, playlist1 -> {
            this.playlists.add(playlist1);
            this.playlistsPagerAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onUserAddedChannelToFavorites(Channel channel) {
        this.interactor.addFavoriteChannel(channel);
        Toast.makeText(this, "Канал добавлен в избранное!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserDeletedChannelToFavorites(Channel channel) {
        this.interactor.delFavoriteChannel(channel);
        Toast.makeText(this, "Канал удален из избранного!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                int currentItem = this.viewPager.getCurrentItem();

                if (currentItem > 0) this.viewPager.setCurrentItem(currentItem - 1, true);
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                int currentItem1 = this.viewPager.getCurrentItem();

                if (currentItem1 < this.viewPager.getAdapter().getCount() - 1)
                    this.viewPager.setCurrentItem(currentItem1 + 1, true);
                return true;

//            case KeyEvent.KEYCODE_DPAD_DOWN:
//            case KeyEvent.KEYCODE_DPAD_UP:
//                getTopPlaylistFragment().proceedKeyEvent(event);
//                return true;
        }

        return super.onKeyDown(keyCode, event);
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
        } else if (id == R.id.action_add_playlist) {
            showAddPlaylist();
            return true;
        } else if (id == R.id.action_remove_playlist) {
            removePlaylist();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

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
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public PlaylistFragment getRegisteredFragment(int position) {
            return (PlaylistFragment) registeredFragments.get(position);
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
