package tw.b1ame.smartiptv.models;

import java.util.List;

public class Playlist {
    private List<Channel> channelList;
    private String name;
    private String url;
    private boolean isFavoritesPlaylist = false;

    public Playlist(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavoritesPlaylist() {
        return isFavoritesPlaylist;
    }

    public void setFavoritesPlaylist(boolean favoritesPlaylist) {
        isFavoritesPlaylist = favoritesPlaylist;
    }
}
