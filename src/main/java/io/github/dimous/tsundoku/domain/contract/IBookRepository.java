package io.github.dimous.tsundoku.domain.contract;

import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;
import io.github.dimous.tsundoku.data.repository.DefaultBookRepository;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IBookRepository {
    long getTotal(final ConfigVO __config_v_o);

    long getTotal(final ConfigVO __config_v_o, final Consumer<DefaultBookRepository.GetTotalConsumerDTO> __consumer);

    void remove(final ConfigVO __config_v_o, final BookEntity __book_entity);

    Map<String, Set<BookEntity>> getDuplicates(final ConfigVO __config_v_o);

    Map<String, List<BookEntity>> getIdenticals(final ConfigVO __config_v_o);

    List<BookEntity> list(final ConfigVO __config_v_o, final int __int_offset, final int __int_limit);

    List<BookEntity> list(final ConfigVO __config_v_o, final int __int_offset, final int __int_limit, final Consumer<DefaultBookRepository.ListConsumerDTO> __consumer);

    List<BookEntity> search(final ConfigVO __config_v_o, final String __string_keyword);

    void ingest(final ConfigVO __config_v_o, final Function<TraversedBookDTO, Boolean> __function_on_progress);

    List<BookEntity> check(final ConfigVO __config_v_o, final Path __path);

    List<BookEntity> getMoreLikeThis(final ConfigVO __config_v_o, final BookEntity __book_entity, final int __int_limit, final int __int_min_doc_freq, final int __int_min_term_freq);
}
