package tw.b1ame.smartiptv.models;

import android.content.Context;

import java.util.Map;

import tw.b1ame.smartiptv.network.Network;
import tw.b1ame.smartiptv.parser.PlaylistFactory;
import tw.b1ame.smartiptv.persistence.Storage;

public class Interactor {
    private Network network;
    private Storage storage;

    public Interactor(Context context) {
        this.network = new Network(context);
        this.storage = new Storage(context);
    }

    public void getPlayList(String url, String name, GetPlaylistListener getPlaylistListener) {
        PlaylistFactory.getPlayList(url, name, this.network, playlist -> {
            this.storage.storeNewPlaylist(playlist);
            getPlaylistListener.onGotPlaylist(playlist);
        });
    }

    public Map<String, String> getPlayListsUrls() {
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
        return favPlaylist;
    }

    public void addCustomFavoriteChannel(String name, String url){
        Channel channel = new Channel(name, url);
        this.storage.addFavoriteChannel(channel);
    }

    public void addFavoriteChannel(Channel channel) {
        this.storage.addFavoriteChannel(channel);
    }

    public void delFavoriteChannel(Channel channel) {
        this.storage.delFavoriteChannel(channel);
    }
}
