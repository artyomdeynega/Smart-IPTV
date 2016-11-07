package tw.b1ame.smartiptv.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import tw.b1ame.smartiptv.models.Channel;

public class ChannelUtils {
    private static final int TIME_OUT = 750;

    public static boolean isChannelAlive(Channel channel) {
        try {
            HttpURLConnection.setFollowRedirects(false);

            HttpURLConnection con =
                    (HttpURLConnection) new URL(channel.getUrl()).openConnection();
            con.setConnectTimeout(TIME_OUT);
            con.setReadTimeout(TIME_OUT);
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);    //This channel is online!
        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
            return false;   //This channel is offline
        } catch (IOException e) {
//            e.printStackTrace();
            return true;    //Unexpected end of stream means this channel is online!
        }
    }

}
