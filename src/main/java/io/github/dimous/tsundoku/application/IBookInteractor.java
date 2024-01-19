package io.github.dimous.tsundoku.application;

import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.presentation.view.dto.AllDTO;
import io.github.dimous.tsundoku.presentation.view.dto.DuplicatesDTO;
import io.github.dimous.tsundoku.presentation.view.dto.IdenticalsDTO;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface IBookInteractor {
    AllDTO getAll() throws Exception;

    AllDTO getAll(final String __string_keyword) throws Exception;

    DuplicatesDTO getDuplicates() throws Exception;

    IdenticalsDTO getIdenticals() throws Exception;

    void open(final BookEntity __book_entity);

    void remove(final BookEntity __book_entity) throws Exception;

    void ingest(final Function<TraversedBookDTO, Boolean> __function_on_progress) throws Exception;

    String check(final String __string_path) throws Exception;

    List<BookEntity> getMoreLikeThis(final BookEntity __book_entity, final int __int_limit, final int __int_min_doc_freq, final int __int_min_term_freq, final int __int_min_word_len, final Set<?> __set_stop_words) throws Exception;
}
