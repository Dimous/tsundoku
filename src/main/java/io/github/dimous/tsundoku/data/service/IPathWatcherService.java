package io.github.dimous.tsundoku.data.service;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.function.Consumer;

public interface IPathWatcherService {
    void stop();

    void start(final Path __path, final Consumer<WatchEvent<Path>> __consumer, final WatchEvent.Kind<Path>... __kinds);
}
