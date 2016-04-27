package tw.b1ame.smartiptv.application;

import android.app.Application;

import tw.b1ame.smartiptv.models.Interactor;

public class App extends Application {
    private Interactor interactor;

    @Override
    public void onCreate() {
        super.onCreate();

        this.interactor = new Interactor(this);
    }

    public Interactor getInteractor() {
        return interactor;
    }
}
