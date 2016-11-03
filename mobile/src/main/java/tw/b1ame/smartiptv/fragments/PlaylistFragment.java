package tw.b1ame.smartiptv.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
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

//    @BindView(R.id.videoview)
//    VideoView videoView;

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
                }

                TextView textView = ButterKnife.findById(convertView, android.R.id.text1);
                textView.setText(getItem(position).getName());

                return convertView;
            }
        });

        this.listView.setOnItemClickListener((parent, view1, position, id) -> {
            Channel channel = playlist.getChannelList().get(position);
            playVideo(channel.getUrl());
        });

        this.listView.requestFocus();
        this.listView.setSelection(0);

//        this.listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Channel channel = playlist.getChannelList().get(i);
//                playVideoPreview(channel.getUrl());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        this.listView.setItemsCanFocus(true);
    }

    public void proceedKeyEvent(KeyEvent keyEvent){
        this.listView.requestFocus();
    }

//    private void playVideoPreview(String url) {
//        MediaController mediaController = new MediaController(getActivity());
//        mediaController.setAnchorView(videoView);
//
//        Uri video = Uri.parse(url);
//        videoView.setMediaController(null);
//        videoView.setVideoURI(video);
//        videoView.requestFocus();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            public void onPrepared(MediaPlayer mp) {
//                videoView.start();
//            }
//        });
//    }

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
