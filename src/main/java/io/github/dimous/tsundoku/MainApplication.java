package io.github.dimous.tsundoku;

import io.github.dimous.tsundoku.di.GuiceInjector;
import io.github.dimous.tsundoku.presentation.view.Util;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public final class MainApplication extends Application {
    public static void main(String[] __array_arguments) {
        MainApplication.launch(__array_arguments);
    }
    //---

    @Override
    public void start(final Stage __stage) throws IOException {
        GuiceInjector.getInjector().getInstance(Util.class).start(__stage, "main", "積ん読", 1024, 768);
    }
}