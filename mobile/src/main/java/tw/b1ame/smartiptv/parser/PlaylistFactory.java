package tw.b1ame.smartiptv.parser;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.b1ame.smartiptv.models.Channel;
import tw.b1ame.smartiptv.models.GetPlaylistListener;
import tw.b1ame.smartiptv.models.Playlist;
import tw.b1ame.smartiptv.network.Network;

public class PlaylistFactory {
    public static void getPlayList(String url, String name, Network network, GetPlaylistListener getPlaylistListener) {
        Playlist playlist = new Playlist(name, url);

        network.downloadPlaylistAsStrings(url, playListStrings -> {
            ParseChannelsTask parseChannelsTask = new ParseChannelsTask() {
                @Override
                protected void onPostExecute(List<Channel> channels) {
                    super.onPostExecute(channels);
                    playlist.setChannelList(channels);
                    getPlaylistListener.onGotPlaylist(playlist);
                }
            };

            parseChannelsTask.execute(playListStrings);
        });
    }

    public static void getPlaylists(Map<String, String> namesToUrls, Network network, GetAllPlaylistsListener getAllPlaylistsListener) {
        List<Playlist> playlists = new ArrayList<>();

        for (Map.Entry<String, String> entry : namesToUrls.entrySet()) {
            PlaylistFactory.getPlayList(entry.getValue(), entry.getKey(), network, playlist -> {
                playlists.add(playlist);

                if (playlists.size() == namesToUrls.size()) {
                    getAllPlaylistsListener.onGotAllPlaylists(playlists);
                }
            });
        }
    }

    private static class ParseChannelsTask extends AsyncTask<List<String>, Void, List<Channel>> {
        @Override
        protected List<Channel> doInBackground(List<String>... m3uStrings) {
            return parseChannels(m3uStrings[0]);
        }

        private List<Channel> parseChannels(List<String> m3uStrings) {
            List<Channel> channelList = new ArrayList<>();

            for (int i = 0; i < m3uStrings.size(); i++) {
                String currentString = m3uStrings.get(i);

                if (currentString.startsWith("#EXTINF:")) {
                    String channelName = currentString.substring(currentString.indexOf(",") + 1);
                    String channelURL = m3uStrings.get(i + 1);
                    Channel channel = new Channel(channelName, channelURL);
                    channelList.add(channel);
                }

            }
            return channelList;
        }
    }

    public interface GetAllPlaylistsListener {
        public void onGotAllPlaylists(List<Playlist> playlists);
    }
}
