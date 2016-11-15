package tw.b1ame.smartiptv.interaction;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import tw.b1ame.smartiptv.application.App;
import tw.b1ame.smartiptv.models.Channel;
import tw.b1ame.smartiptv.models.GetPlaylistListener;
import tw.b1ame.smartiptv.models.Playlist;
import tw.b1ame.smartiptv.network.Network;
import tw.b1ame.smartiptv.parser.PlaylistFactory;
import tw.b1ame.smartiptv.persistence.Storage;
import tw.b1ame.smartiptv.utils.CheckChannelsAvailabilityTask;

public class Interactor {
    @Inject
    Network network;
    @Inject
    Storage storage;

    private List<WeakReference<GlobalEventsListener>> globalEventsListeners = new ArrayList<>();

    private CheckChannelsAvailabilityTask checkFavChannelsTask = new CheckChannelsAvailabilityTask();

    public Interactor() {
        App.getAppComponent().inject(this);
    }

    public void addGlobalEventsListener(GlobalEventsListener listener) {
        this.globalEventsListeners.add(new WeakReference<>(listener));
    }

    public void removeGlobalEventsListener(GlobalEventsListener listener) {
        Iterator<WeakReference<GlobalEventsListener>> iterator = this.globalEventsListeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<GlobalEventsListener> iterrListener = iterator.next();
            if (listener == iterrListener.get()) iterator.remove();
        }
    }

    public void fireEvent(GlobalEvent event) {
        Iterator<WeakReference<GlobalEventsListener>> iterator = this.globalEventsListeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<GlobalEventsListener> iterrListener = iterator.next();
            if (iterrListener.get() != null) iterrListener.get().onGlobalEvent(event);
        }
    }

    public void addPlayList(String url, String name, GetPlaylistListener getPlaylistListener) {
        PlaylistFactory.getPlayList(url, name, this.network, playlist -> {
            this.storage.storeNewPlaylist(playlist);
            getPlaylistListener.onGotPlaylist(playlist);
        });
    }

    private Map<String, String> getPlayListsUrls() {
        return this.storage.getPlayListUrls();
    }

    public void getAllStoredPlaylists(PlaylistFactory.GetAllPlaylistsListener getAllPlaylistsListener) {
        Map<String, String> playlistsUrls = getPlayListsUrls();
        PlaylistFactory.getPlaylists(playlistsUrls, this.network, playlists -> getAllPlaylistsListener.onGotAllPlaylists(playlists));
    }

    public void removePlaylist(Playlist playlist) {
        this.storage.removePlaylist(playlist);
    }

    public Playlist getFavoritesPlaylist() {
        Playlist favPlaylist = new Playlist("Избранное", "");
        favPlaylist.setFavoritesPlaylist(true);
        favPlaylist.setChannelList(this.storage.getFavoriteChannels());

        startCheckFavoriteChannelsTask(new ArrayList<>(favPlaylist.getChannelList()));

        return favPlaylist;
    }

    private void startCheckFavoriteChannelsTask(List<Channel> channels) {
        switch (this.checkFavChannelsTask.getStatus()) {
            case RUNNING:
                this.checkFavChannelsTask.cancel(true);

            default:
                this.checkFavChannelsTask = new CheckChannelsAvailabilityTask() {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        fireEvent(GlobalEvent.ON_FAVORITES_UPDATED);
                    }
                };
                this.checkFavChannelsTask.execute(channels);
                break;
        }
    }

    public void addCustomFavoriteChannel(String name, String url) {
        Channel channel = new Channel(name, url);
        this.storage.addFavoriteChannel(channel);
        fireEvent(GlobalEvent.ON_FAVORITES_CHANGED);
    }

    public void delFavoriteChannel(Channel channel) {
        this.storage.removeFavoriteChannel(channel);
        fireEvent(GlobalEvent.ON_FAVORITES_CHANGED);
    }

}
