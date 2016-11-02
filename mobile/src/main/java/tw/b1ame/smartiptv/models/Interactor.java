package tw.b1ame.smartiptv.models;

import android.content.Context;

import java.util.Set;

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

    public Set<String> getPlayListsUrls(){
        return this.storage.getPlayListUrls();
    }

}
