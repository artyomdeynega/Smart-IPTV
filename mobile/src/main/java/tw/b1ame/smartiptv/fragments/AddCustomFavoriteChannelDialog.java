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

public class AddCustomFavoriteChannelDialog extends DialogFragment {
    @BindView(R.id.playlist_name)
    EditText name;
    @BindView(R.id.playlist_url)
    EditText url;

    public AddCustomFavoriteChannelDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.fragment_add_playlist, null);
        ButterKnife.bind(this, contentView);

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.add_to_favorites)
                .setTitle("Add favorite channel")
                .setPositiveButton("Ok",
                        (dialog, which) -> {
                            ((AddCustomFavoriteChannelListener) getActivity()).onUserAddedCustomFavChannel(name.getText().toString(), url.getText().toString());
                        }
                )
                .setNegativeButton("Cancel",
                        (dialog1, which1) -> {
                        }
                )
                .setView(contentView)
                .create();
    }

    public interface AddCustomFavoriteChannelListener {
        public void onUserAddedCustomFavChannel(String name, String url);
    }

}
