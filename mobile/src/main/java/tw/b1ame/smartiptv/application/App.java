package tw.b1ame.smartiptv.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import tw.b1ame.smartiptv.models.Interactor;

public class App extends Application {
    private Interactor interactor;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        this.interactor = new Interactor(this);
    }

    public Interactor getInteractor() {
        return interactor;
    }
}
