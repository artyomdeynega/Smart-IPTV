package tw.b1ame.smartiptv.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ChannelUtils {
    private static final int TIME_OUT = 5000;

    public static boolean isChannelAlive(String channelUrl) {
        try {
            HttpURLConnection.setFollowRedirects(false);

            HttpURLConnection con =
                    (HttpURLConnection) new URL(channelUrl).openConnection();
            con.setConnectTimeout(TIME_OUT);
            con.setReadTimeout(TIME_OUT);
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);    //This channel is online!
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return false;   //This channel is offline
        } catch (IOException e) {
            e.printStackTrace();
            return true;    //Unexpected end of stream means this channel is online!
        }
    }
}
