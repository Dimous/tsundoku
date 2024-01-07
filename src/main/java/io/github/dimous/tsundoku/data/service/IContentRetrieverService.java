package io.github.dimous.tsundoku.data.service;

import java.io.InputStream;

public interface IContentRetrieverService {
    String retrieve(final InputStream __input_stream);
}