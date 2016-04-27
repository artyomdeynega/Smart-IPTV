package tw.b1ame.smartiptv.models;

import android.content.Context;

import java.util.List;

import tw.b1ame.smartiptv.network.Network;
import tw.b1ame.smartiptv.parser.M3Uparser;

public class Interactor {
    private Network network;

    public Interactor(Context context) {
        this.network = new Network(context);
    }

    public void getPlayList(String url, GetPlaylistListener getPlaylistListener) {
        this.network.downloadPlaylist(url, playListStrings -> {
            Playlist playlist = parsePlayList(playListStrings);
            getPlaylistListener.onGotPlaylist(playlist);
        });
    }

    private Playlist parsePlayList(List<String> stringList) {
        List<Channel> channelList = M3Uparser.parseChannels(stringList);
        Playlist playlist = new Playlist(channelList, "");
        return playlist;
    }
}
