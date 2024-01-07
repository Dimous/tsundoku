package io.github.dimous.tsundoku.data.data_source;

import io.github.dimous.tsundoku.data.dto.BookDTO;
import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface IBookDataSource {
    Optional<BookDTO> getBook(final Path __path);

    void traverseFileSystem(final String __string_root_path, final Set<String> __set_extensions, final Consumer<TraversedBookDTO> __consumer);
}
