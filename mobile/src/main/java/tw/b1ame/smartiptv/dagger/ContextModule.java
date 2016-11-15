package tw.b1ame.smartiptv.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import tw.b1ame.smartiptv.application.App;

@Module
public class ContextModule {
    App app;

    public ContextModule(App app) {
        this.app = app;
    }

    @Provides
    Context provideContext() {
        return app;
    }
}
