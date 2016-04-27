package tw.b1ame.smartiptv.parser;

import java.util.ArrayList;
import java.util.List;

import tw.b1ame.smartiptv.models.Channel;

public class M3Uparser {
    public static List<Channel> parseChannels(List<String> m3uStrings) {
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
