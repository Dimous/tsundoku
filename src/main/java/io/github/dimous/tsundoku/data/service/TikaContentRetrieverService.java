package io.github.dimous.tsundoku.data.service;

import org.apache.tika.Tika;

import java.io.InputStream;

public final class TikaContentRetrieverService implements IContentRetrieverService {
    private final Tika
        __tika = new Tika();

    public String retrieve(final InputStream __input_stream) {
        // this.__tika.setMaxStringLength(5000); // isbn обычно на первых страницах, но ещё и поисковый индекс строится

        try {
            return this.__tika.parseToString(__input_stream);
        } catch (final Exception __exception) {
            return "";
        }
    }
}