package io.github.dimous.tsundoku;

import io.github.dimous.tsundoku.di.GuiceInjector;
import io.github.dimous.tsundoku.di.MainModule;
import io.github.dimous.tsundoku.presentation.view.Util;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class MainApplication extends Application {
    public static void main(String[] __array_arguments) {
        MainApplication.launch(__array_arguments);
    }
    //---

    @Override
    public void start(final Stage __stage) throws IOException {
        __stage.setTitle("積ん読");
        __stage.setOnCloseRequest(
            __window_event -> {
                GuiceInjector.getInjector().getInstance(MainModule.SESSION_FACTORY_CACHE).invalidateAll();
            }
        );
        __stage.setScene(
            new Scene(GuiceInjector.getInjector().getInstance(Util.class).loadFXML("main"), 1024, 768)
        );
        __stage.show();
    }
}