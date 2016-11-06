package tw.b1ame.smartiptv.models;


public class Channel {
    public enum OnlineStatus {
        ONLINE, OFFLINE, UNKNOWN
    }

    private String name;
    private String url;
    private OnlineStatus onlineStatus = OnlineStatus.UNKNOWN;

    public Channel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(OnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Channel) && (((Channel) obj).url.equals(this.url));
    }
}
