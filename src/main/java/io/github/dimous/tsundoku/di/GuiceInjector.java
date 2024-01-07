package io.github.dimous.tsundoku.di;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class GuiceInjector {
    private static Injector
        __injector;

    private GuiceInjector() {}

    public static Injector getInjector() {
        if (null == __injector) {
            __injector = Guice.createInjector(new MainModule());
        }

        return __injector;
    }
}
