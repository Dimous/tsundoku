package io.github.dimous.tsundoku.data.data_source;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.data.dto.BookDTO;
import io.github.dimous.tsundoku.data.dto.FileDTO;
import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;
import io.github.dimous.tsundoku.data.service.IContentRetrieverService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public final class DefaultBookDataSource implements IBookDataSource {
    @Inject
    private IFileDataSource
        __file_data_source;
    @Inject
    private IContentRetrieverService
        __content_retriever_service;

    public Optional<BookDTO> getBook(final Path __path) {
        try {
            final FileDTO
                __file_d_t_o = this.__file_data_source.getFile(__path);
            ///
            ///
            try (
                final InputStream
                    __input_stream = __file_d_t_o.source().openBufferedStream()
            ) {
                return Optional.of(new BookDTO(__file_d_t_o.path(), __file_d_t_o.name(), __file_d_t_o.extension(), __file_d_t_o.hash(), __file_d_t_o.size(), this.__content_retriever_service.retrieve(__input_stream)));
            } catch (final Exception __exception) {
                // __exception.printStackTrace();
            }
        } catch (final IOException __exception) {
            // __exception.printStackTrace();
        }

        return Optional.empty();
    }
    //---

    public void traverseFileSystem(final String __string_root_path, final Set<String> __set_extensions, final Consumer<TraversedBookDTO> __consumer) {
        final Path[]
            __array_paths = this.__file_data_source.list(__string_root_path, __set_extensions);
        ///
        ///
        for (int __int_index = 0, __int_total = __array_paths.length; __int_index < __int_total; ++__int_index) {
            final int
                __int_step = 1 + __int_index;
            final CompletableFuture<Boolean>
                __completable_future_stop = new CompletableFuture<>();
            ///
            ///
            this.getBook(__array_paths[__int_index]).ifPresent(
                __book_d_t_o -> __consumer.accept(new TraversedBookDTO(__int_step, __int_total, __completable_future_stop, __book_d_t_o.path(), __book_d_t_o.name(), __book_d_t_o.extension(), __book_d_t_o.hash(), __book_d_t_o.size(), __book_d_t_o.content()))
            );

            try {
                if (!__completable_future_stop.isDone()) {
                    __completable_future_stop.complete(false);
                }

                if (__completable_future_stop.get()) {
                    break;
                }
            } catch (final InterruptedException | ExecutionException __exception) {
                // __exception.printStackTrace();
            }
        }
    }
}