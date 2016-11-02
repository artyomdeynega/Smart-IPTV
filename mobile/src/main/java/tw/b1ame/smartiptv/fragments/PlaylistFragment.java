package tw.b1ame.smartiptv.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.b1ame.smartiptv.R;
import tw.b1ame.smartiptv.models.Channel;
import tw.b1ame.smartiptv.models.Playlist;


public class PlaylistFragment extends Fragment {
    private Playlist playlist;

    @BindView(R.id.playlist)
    ListView listView;

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        this.listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return playlist.getChannelList().size();
            }

            @Override
            public Channel getItem(int position) {
                return playlist.getChannelList().get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
                    TextView textView = ButterKnife.findById(convertView, android.R.id.text1);
                    textView.setText(getItem(position).getName());
                }
                return convertView;
            }
        });

        this.listView.setOnItemClickListener((parent, view1, position, id) -> {
            Channel channel = playlist.getChannelList().get(position);
            playVideo(channel.getUrl());
        });
    }

    private void playVideo(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setDataAndType(Uri.parse(url), "video/*");
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_playlist, container, false);
        return contentView;
    }
}
