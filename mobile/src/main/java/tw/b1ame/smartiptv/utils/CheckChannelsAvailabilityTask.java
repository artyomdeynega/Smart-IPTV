package tw.b1ame.smartiptv.utils;

import android.os.AsyncTask;

import java.util.List;

import tw.b1ame.smartiptv.models.Channel;

public class CheckChannelsAvailabilityTask extends AsyncTask<List<Channel>, Void, Void> {
    @Override
    protected Void doInBackground(List<Channel>... lists) {
        for (Channel channel : lists[0]) {
            channel.setOnlineStatus(ChannelUtils.isChannelAlive(channel) ? Channel.OnlineStatus.ONLINE : Channel.OnlineStatus.OFFLINE);
        }
        return null;
    }
}
