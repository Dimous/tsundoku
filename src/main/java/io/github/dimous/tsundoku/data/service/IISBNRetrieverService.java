package io.github.dimous.tsundoku.data.service;

import java.util.Set;

public interface IISBNRetrieverService {
    Set<String> retrieve(final String __string_contents);
}
