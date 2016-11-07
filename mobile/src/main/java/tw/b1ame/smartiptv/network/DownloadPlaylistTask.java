package tw.b1ame.smartiptv.network;


import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadPlaylistTask extends AsyncTask<String, Integer, List<String>> {
    private Context context;
    private File outputFile;
    private boolean success;
    private Network.PlaylistDownloadListener listener;

    public DownloadPlaylistTask(Context context, Network.PlaylistDownloadListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        List<String> playlistStrings = new ArrayList<>();

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            inputStream = connection.getInputStream();

            if (inputStream != null) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    playlistStrings.add(line);
                }
            }

            this.success = true;
            return playlistStrings;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        this.listener.onPlaylistDownloaded(strings);
    }

    public File getOutputFile() {
        return outputFile;
    }
}