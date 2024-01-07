package io.github.dimous.tsundoku.domain.contract;

import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;
import io.github.dimous.tsundoku.data.repository.DefaultBookRepository;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.domain.entity.ConfigEntity;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IBookRepository {
    long getTotal(final ConfigEntity __config_entity);

    long getTotal(final ConfigEntity __config_entity, final Consumer<DefaultBookRepository.GetTotalConsumerDTO> __consumer);

    void remove(final ConfigEntity __config_entity, final BookEntity __book_entity);

    Map<String, Set<BookEntity>> getDuplicates(final ConfigEntity __config_entity);

    Map<String, List<BookEntity>> getIdenticals(final ConfigEntity __config_entity);

    List<BookEntity> list(final ConfigEntity __config_entity, final int __int_offset, final int __int_limit);

    List<BookEntity> list(final ConfigEntity __config_entity, final int __int_offset, final int __int_limit, final Consumer<DefaultBookRepository.ListConsumerDTO> __consumer);

    List<BookEntity> search(final ConfigEntity __config_entity, final String __string_keyword);

    void ingest(final ConfigEntity __config_entity, final Function<TraversedBookDTO, Boolean> __function_on_progress);

    List<BookEntity> check(final ConfigEntity __config_entity, final Path __path);
}
