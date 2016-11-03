package tw.b1ame.smartiptv.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import tw.b1ame.smartiptv.models.Playlist;

public class Storage {
    private static final String PLAY_LISTS_STORAGE = "PLAY_LISTS_STORAGE";

    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void storeNewPlaylist(Playlist playlist) {
        getPreferences().edit().putString(playlist.getName(), playlist.getUrl()).apply();
    }

    public Map<String, String> getPlayListUrls() {
        Map<String, ?> nameToUrlCached = getPreferences().getAll();
        Map<String, String> nameToUrlMap = new HashMap<>();

        for (Map.Entry<String, ?> entry : nameToUrlCached.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            nameToUrlMap.put(entry.getKey(), entry.getValue().toString());
        }

        return nameToUrlMap;
    }

    public void removePlaylist(Playlist playlist) {
        getPreferences().edit().remove(playlist.getName()).apply();
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(Storage.PLAY_LISTS_STORAGE, Context.MODE_PRIVATE);
    }
}
