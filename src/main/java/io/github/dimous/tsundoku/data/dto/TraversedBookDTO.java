package io.github.dimous.tsundoku.data.dto;

import java.util.concurrent.CompletableFuture;

public record TraversedBookDTO(int step, int total, CompletableFuture<Boolean> stop, String path, String name, String extension, String hash, long size, String content) {
    public void stop(final boolean __boolean_stop) {
        this.stop.complete(__boolean_stop);
    }
}