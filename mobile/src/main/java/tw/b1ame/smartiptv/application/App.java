package tw.b1ame.smartiptv.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import tw.b1ame.smartiptv.dagger.AppComponent;
import tw.b1ame.smartiptv.dagger.ContextModule;
import tw.b1ame.smartiptv.dagger.DaggerAppComponent;

public class App extends Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpDagger();
        Fabric.with(this, new Crashlytics());
    }

    private void setUpDagger() {
        ContextModule contextModule = new ContextModule(this);
        this.appComponent = DaggerAppComponent.builder().contextModule(contextModule).build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
