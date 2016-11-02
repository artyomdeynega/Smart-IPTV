package tw.b1ame.smartiptv.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tw.b1ame.smartiptv.models.Playlist;

public class Storage {
    private static final String PLAY_LISTS_STORAGE = "PLAY_LISTS_STORAGE";
    private static final String PLAY_LISTS_URLS_KEY = "PLAY_LISTS_URLS_KEY";

    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public void storeNewPlaylist(Playlist playlist) {
        Set<String> urls = getPlayListUrls();
        urls.add(playlist.getUrl());
        cachePlayListsUrls(urls);
    }

    private void cachePlayListsUrls(Set<String> urls) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putStringSet(Storage.PLAY_LISTS_URLS_KEY, urls).apply();
    }

    private void cachePlayLists(Collection<Playlist> playListsToStore) {
        List<String> playListsUrls = new ArrayList<>();

        for (Playlist playlist : playListsToStore) {
            playListsUrls.add(playlist.getUrl());
        }

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putStringSet(Storage.PLAY_LISTS_URLS_KEY, new HashSet<>(playListsUrls)).apply();
    }

    public Set<String> getPlayListUrls() {
        return getPreferences().getStringSet(Storage.PLAY_LISTS_URLS_KEY, new HashSet<>());
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(Storage.PLAY_LISTS_STORAGE, Context.MODE_PRIVATE);
    }
}
