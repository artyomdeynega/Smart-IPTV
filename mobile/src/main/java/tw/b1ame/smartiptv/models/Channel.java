package tw.b1ame.smartiptv.models;


public class Channel {
    private String name;
    private String url;

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
}
