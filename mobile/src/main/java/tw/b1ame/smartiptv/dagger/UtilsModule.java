package tw.b1ame.smartiptv.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tw.b1ame.smartiptv.interaction.Interactor;
import tw.b1ame.smartiptv.network.Network;
import tw.b1ame.smartiptv.persistence.Storage;

@Module(includes = {ContextModule.class})
public class UtilsModule {
    @Singleton
    @Provides
    Network provideNetwork(Context context) {
        return new Network(context);
    }

    @Singleton
    @Provides
    Interactor provideInteractor() {
        return new Interactor();
    }

    @Singleton
    @Provides
    Storage provideStorage(Context context) {
        return new Storage(context);
    }
}
