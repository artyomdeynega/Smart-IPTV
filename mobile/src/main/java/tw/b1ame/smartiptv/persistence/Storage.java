package tw.b1ame.smartiptv.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.b1ame.smartiptv.models.Channel;
import tw.b1ame.smartiptv.models.Playlist;

public class Storage {
    private static final String PLAY_LISTS_STORAGE = "PLAY_LISTS_STORAGE";
    private static final String FAV_CHANNELS_STORAGE = "FAV_CHANNELS_STORAGE";

    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void storeNewPlaylist(Playlist playlist) {
        getPreferences(Storage.PLAY_LISTS_STORAGE).edit().putString(playlist.getName(), playlist.getUrl()).apply();
    }

    public Map<String, String> getPlayListUrls() {
        Map<String, ?> nameToUrlCached = getPreferences(Storage.PLAY_LISTS_STORAGE).getAll();
        Map<String, String> nameToUrlMap = new HashMap<>();

        for (Map.Entry<String, ?> entry : nameToUrlCached.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            nameToUrlMap.put(entry.getKey(), entry.getValue().toString());
        }

        return nameToUrlMap;
    }

    public void removePlaylist(Playlist playlist) {
        getPreferences(Storage.PLAY_LISTS_STORAGE).edit().remove(playlist.getName()).apply();
    }

    private SharedPreferences getPreferences(String key) {
        return context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public List<Channel> getFavoriteChannels() {
        Map<String, ?> favChannels = getPreferences(Storage.FAV_CHANNELS_STORAGE).getAll();
        List<Channel> channels = new ArrayList<>();

        for (Map.Entry<String, ?> entry : favChannels.entrySet()) {
            Channel channel = new Channel(entry.getKey(), entry.getValue().toString());
            channels.add(channel);
        }

        return channels;
    }

    public void addFavoriteChannel(Channel channel){
        getPreferences(Storage.FAV_CHANNELS_STORAGE).edit().putString(channel.getName(), channel.getUrl()).apply();
    }

    public void delFavoriteChannel(Channel channel){
        getPreferences(Storage.FAV_CHANNELS_STORAGE).edit().remove(channel.getName()).apply();
    }
}
