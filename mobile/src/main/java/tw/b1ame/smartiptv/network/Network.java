package tw.b1ame.smartiptv.network;

import android.content.Context;

import java.util.List;

public class Network {
    private Context appContext;

    public Network(Context appContext) {
        this.appContext = appContext;
    }

    public void downloadPlaylistAsStrings(String url, final PlaylistDownloadListener listener) {
        DownloadTask downloadTask = new DownloadTask(appContext, listener::onPlaylistDownloaded);
        downloadTask.execute(url);
    }

    public interface PlaylistDownloadListener {
        public void onPlaylistDownloaded(List<String> playListStrings);
    }
}
