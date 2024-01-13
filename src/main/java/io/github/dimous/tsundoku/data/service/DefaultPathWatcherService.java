package io.github.dimous.tsundoku.data.service;

import java.nio.file.*;
import java.util.function.Consumer;

public class DefaultPathWatcherService implements IPathWatcherService {
    private Thread
        __thread;

    @Override
    public void stop() {
        if (null != this.__thread) {
            this.__thread.interrupt();
        }
    }
    //---

    @Override
    public void start(final Path __path, final Consumer<WatchEvent<Path>> __consumer, final WatchEvent.Kind<Path>... __kinds) {
        this.stop();

        this.__thread = new Thread(
            () -> {
                WatchKey
                    __watch_key;
                ///
                ///
                try (
                    final WatchService
                        __watch_service = FileSystems.getDefault().newWatchService()
                ) {
                    __path.register(__watch_service, __kinds);

                    while (!this.__thread.isInterrupted()) {
                        try {
                            __watch_key = __watch_service.take();
                        } catch (final InterruptedException __exception) {
                            // __exception.printStackTrace();

                            break;
                        }

                        for (final WatchEvent<?> __watch_event : __watch_key.pollEvents()) {
                            __consumer.accept((WatchEvent<Path>) __watch_event);
                        }

                        if (!__watch_key.reset()) {
                            break;
                        }
                    }
                } catch (final Exception __exception) {
                    // __exception.printStackTrace();

                    Thread.currentThread().interrupt();
                }
            }
        );

        this.__thread.start();
    }
}
