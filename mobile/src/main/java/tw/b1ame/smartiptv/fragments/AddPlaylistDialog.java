package tw.b1ame.smartiptv.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.b1ame.smartiptv.R;

public class AddPlaylistDialog extends DialogFragment {
    @BindView(R.id.playlist_name)
    EditText playlistName;
    @BindView(R.id.playlist_url)
    EditText playlistURL;

    public AddPlaylistDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.fragment_add_playlist, null);
        ButterKnife.bind(this, contentView);

        return new AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle("Add playlist")
                .setPositiveButton("Ok",
                        (dialog, which) -> {

                            ((AddPlaylistListener) getActivity()).onUserAddedPlaylist(playlistName.getText().toString(), playlistURL.getText().toString());
                        }
                )
                .setNegativeButton("Cancel",
                        (dialog1, which1) -> {
                        }
                )
                .setView(contentView)
                .create();
    }

    public interface AddPlaylistListener {
        public void onUserAddedPlaylist(String name, String url);
    }

}
