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

public class DownloadTask extends AsyncTask<String, Integer, List<String>> {
    private Context context;
    private File outputFile;
    private boolean success;
    private Network.PlaylistDownloadListener listener;

    public DownloadTask(Context context, Network.PlaylistDownloadListener listener) {
        this.context = context;
        this.listener = listener;
    }

//    @Override
//    protected String doInBackground(String... sUrl) {
//        InputStream input = null;
//        OutputStream outputStream = null;
//        HttpURLConnection connection = null;
//        try {
//            URL url = new URL(sUrl[0]);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//
//            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                return "Server returned HTTP " + connection.getResponseCode()
//                        + " " + connection.getResponseMessage();
//            }
//
//            // this will be useful to display download percentage
//            // might be -1: server did not report the length
//            int fileLength = connection.getContentLength();
//
//            // download the file
//            input = connection.getInputStream();
//            this.outputFile = new File(context.getDir("playlists", Context.MODE_PRIVATE), "testplaylist.mp3u");
//            outputStream = new FileOutputStream(this.outputFile);
//
//            byte data[] = new byte[4096];
//            long total = 0;
//            int count;
//            while ((count = input.read(data)) != -1) {
//                // allow canceling with back button
//                if (isCancelled()) {
//                    input.close();
//                    return null;
//                }
//                total += count;
//                // publishing the progress....
//                if (fileLength > 0) // only if total length is known
//                    publishProgress((int) (total * 100 / fileLength));
//                outputStream.write(data, 0, count);
//            }
//
//            this.success = true;
//        } catch (Exception e) {
//            return e.toString();
//        } finally {
//            try {
//                if (outputStream != null)
//                    outputStream.close();
//                if (input != null)
//                    input.close();
//            } catch (IOException ignored) {
//            }
//
//            if (connection != null)
//                connection.disconnect();
//        }
//        return null;
//    }


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