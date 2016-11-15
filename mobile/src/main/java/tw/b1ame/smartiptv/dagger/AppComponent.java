package tw.b1ame.smartiptv.dagger;

import javax.inject.Singleton;

import dagger.Component;
import tw.b1ame.smartiptv.activity.MainActivity;
import tw.b1ame.smartiptv.fragments.PlaylistFragment;
import tw.b1ame.smartiptv.interaction.Interactor;

@Singleton
@Component(modules = {ContextModule.class, UtilsModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(PlaylistFragment playlistFragment);

    void inject(Interactor interactor);
}
