package tw.b1ame.smartiptv.models;

import java.util.List;

public class Playlist {
    private List<Channel> channelList;
    private String name;

    public Playlist(List<Channel> channelList, String name) {
        this.channelList = channelList;
        this.name = name;
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
}
