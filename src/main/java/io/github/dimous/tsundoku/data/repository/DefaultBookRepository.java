package io.github.dimous.tsundoku.data.repository;

import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import io.github.dimous.tsundoku.data.data_source.IBookDataSource;
import io.github.dimous.tsundoku.data.data_source.IFileDataSource;
import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;
import io.github.dimous.tsundoku.data.service.IISBNRetrieverService;
import io.github.dimous.tsundoku.domain.contract.IBookRepository;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.domain.entity.NumberEntity;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DefaultBookRepository implements IBookRepository {
    @Inject
    private IBookDataSource
        __book_data_source;

    @Inject
    private IFileDataSource
        __file_data_source;

    @Inject
    private IISBNRetrieverService
        __i_s_b_n_retriever_service;

    @Inject
    private LoadingCache<ConfigVO, SessionFactory>
        __loading_cache_session_factory;

    @Override
    public Map<String, List<BookEntity>> getIdenticals(final ConfigVO __config_v_o) {
        final AtomicReference<Map<String, List<BookEntity>>>
            __atomic_reference_result = new AtomicReference<>(Map.of());
        ///
        ///
        this.withSession(
            __config_v_o,
            __session -> __atomic_reference_result.set(
                __session.createSelectionQuery("FROM BookEntity WHERE hash IN (SELECT hash FROM BookEntity GROUP BY hash HAVING COUNT(hash) > 1) ORDER BY hash", BookEntity.class).getResultList().stream().collect(Collectors.groupingBy(BookEntity::getHash))
            )
        );

        return __atomic_reference_result.get();
    }
    //---

    @Override
    public Map<String, Set<BookEntity>> getDuplicates(final ConfigVO __config_v_o) {
        final AtomicReference<Map<String, Set<BookEntity>>>
            __atomic_reference_result = new AtomicReference<>(Map.of());
        ///
        ///
        this.withSession(
            __config_v_o,
            __session -> __atomic_reference_result.set(
                __session.createSelectionQuery("SELECT n FROM NumberEntity n JOIN n.books b GROUP BY n HAVING COUNT(b) > 1 ORDER BY n", NumberEntity.class).getResultList().stream().collect(Collectors.toMap(NumberEntity::getValue, NumberEntity::getBooks))
            )
        );

        return __atomic_reference_result.get();
    }
    //---

    @Override
    public long getTotal(final ConfigVO __config_v_o) {
        return this.getTotal(__config_v_o, null);
    }

    @Override
    public long getTotal(final ConfigVO __config_v_o, final Consumer<GetTotalConsumerDTO> __consumer) {
        final AtomicReference<Long>
            __atomic_reference_result = new AtomicReference<>(0L);
        ///
        ///
        this.withSession(
            __config_v_o,
            __session -> {
                final CriteriaBuilder
                    __criteria_builder = __session.getCriteriaBuilder();
                final CriteriaQuery<Long>
                    __criteria_query = __criteria_builder.createQuery(Long.class);
                final Root<BookEntity>
                    __root = __criteria_query.from(BookEntity.class);
                ///
                ///
                __criteria_query.select(__criteria_builder.count(__root.get("id")));

                if (null != __consumer) {
                    __consumer.accept(
                        new GetTotalConsumerDTO(__criteria_builder, __criteria_query, __root)
                    );
                }

                __atomic_reference_result.set(
                    __session.createQuery(__criteria_query).getSingleResult()
                );
            }
        );

        return __atomic_reference_result.get();
    }
    //---

    @Override
    public void remove(final ConfigVO __config_v_o, final BookEntity __book_entity) {
        this.withSession(
            __config_v_o,
            __session -> {
                __session.remove(__book_entity);

                if (!this.__file_data_source.deleteFile(__book_entity.getPath())) {
                    __session.getTransaction().rollback();
                }
            }
        );
    }
    //---

    @Override
    public void ingest(final ConfigVO __config_v_o, final Function<TraversedBookDTO, Boolean> __function_on_progress) {
        this.__book_data_source.traverseFileSystem(
            __config_v_o.getBasePath(),
            __config_v_o.getExtensions(),
            __traversed_book_d_t_o -> {
                // пакетный сброс сессии с gc не срабатывал, OOM на ~5600 элементе, покэтому -- сессия/элемент
                this.withSession(
                    __config_v_o,
                    __session -> __session.merge(
                        new BookEntity(__traversed_book_d_t_o.path(), __traversed_book_d_t_o.name(), __traversed_book_d_t_o.extension(), __traversed_book_d_t_o.size(), __traversed_book_d_t_o.hash(), this.__i_s_b_n_retriever_service.retrieve(__traversed_book_d_t_o.content()).stream().map(NumberEntity::new).collect(Collectors.toSet()), __traversed_book_d_t_o.content())
                    )
                );

                __traversed_book_d_t_o.stop(
                    __function_on_progress.apply(__traversed_book_d_t_o)
                );
            }
        );
    }
    //---

    @Override
    public List<BookEntity> list(final ConfigVO __config_v_o, final int __int_offset, final int __int_limit) {
        return this.list(__config_v_o, __int_offset, __int_limit, null);
    }

    @Override
    public List<BookEntity> list(final ConfigVO __config_v_o, final int __int_offset, final int __int_limit, final Consumer<ListConsumerDTO> __consumer) {
        final AtomicReference<List<BookEntity>>
            __atomic_reference_result = new AtomicReference<>(List.of());
        ///
        ///
        this.withSession(
            __config_v_o,
            __session -> {
                final CriteriaBuilder
                    __criteria_builder = __session.getCriteriaBuilder();
                final CriteriaQuery<BookEntity>
                    __criteria_query = __criteria_builder.createQuery(BookEntity.class);
                final Root<BookEntity>
                    __root = __criteria_query.from(BookEntity.class);
                ///
                ///
                if (null != __consumer) {
                    __consumer.accept(
                        new ListConsumerDTO(__criteria_builder, __criteria_query, __root)
                    );
                }

                __atomic_reference_result.set(
                    __session.createQuery(__criteria_query).setMaxResults(__int_limit).setFirstResult(__int_offset).getResultList()
                );
            }
        );

        return __atomic_reference_result.get();
    }
    //---

    @Override
    public List<BookEntity> search(final ConfigVO __config_v_o, final String __string_keyword) {
        final AtomicReference<List<BookEntity>>
            __atomic_reference_result = new AtomicReference<>(List.of());
        ///
        ///
        this.withSession(
            __config_v_o,
            __session -> {
                final SearchSession
                    __search_session = Search.session(__session);
                final SearchScope<BookEntity>
                    __search_scope = __search_session.scope(BookEntity.class);
                ///
                ///
                __atomic_reference_result.set(
                    __search_session.search(__search_scope).where(__search_scope.predicate().match().fields("content").matching(__string_keyword).toPredicate()).fetchAll().hits()
                );
            }
        );

        return __atomic_reference_result.get();
    }
    //---

    @Override
    public List<BookEntity> check(final ConfigVO __config_v_o, final Path __path) {
        final AtomicReference<List<BookEntity>>
            __atomic_reference_result = new AtomicReference<>(List.of());
        ///
        ///
        this.__book_data_source.getBook(__path).ifPresent(
            __book_d_t_o -> this.withSession(
                __config_v_o,
                __session -> __atomic_reference_result.set(
                    __session.createSelectionQuery("SELECT b FROM BookEntity b LEFT JOIN b.numbers n WHERE b.hash = :hash OR n.value IN (:numbers)", BookEntity.class).setParameter("hash", __book_d_t_o.hash()).setParameterList("numbers", this.__i_s_b_n_retriever_service.retrieve(__book_d_t_o.content())).getResultList()
                )
            )
        );

        return __atomic_reference_result.get();
    }
    //---

    public List<BookEntity> getMoreLikeThis(final ConfigVO __config_v_o, final BookEntity __book_entity, final int __int_limit, final int __int_min_doc_freq, final int __int_min_term_freq, final int __int_min_word_len, final Set<?> __set_stop_words) {
        final AtomicReference<List<BookEntity>>
            __atomic_reference_result = new AtomicReference<>(List.of());
        ///
        ///
        this.withSession(
            __config_v_o,
            __session -> {
                try (
                    final IndexReader
                        __index_reader = Search.mapping(__session.getEntityManagerFactory()).scope(BookEntity.class).extension(LuceneExtension.get()).openIndexReader()
                ) {
                    final MoreLikeThis
                        __more_like_this = new MoreLikeThis(__index_reader);
                    final IndexSearcher
                        __index_searcher = new IndexSearcher(__index_reader);
                    ///
                    ///
                    __more_like_this.setAnalyzer(
                        new StandardAnalyzer()
                    );
                    __more_like_this.setFieldNames(
                        new String[]{
                            "content"
                        }
                    );
                    __more_like_this.setStopWords(__set_stop_words);
                    __more_like_this.setMinWordLen(__int_min_word_len);
                    __more_like_this.setMinDocFreq(__int_min_doc_freq);
                    __more_like_this.setMinTermFreq(__int_min_term_freq);

                    __atomic_reference_result.set(
                        __session.byMultipleIds(BookEntity.class).multiLoad(Arrays.stream(__index_searcher.search(__more_like_this.like(Math.toIntExact(__book_entity.getId())), __int_limit).scoreDocs).mapToInt(__score_doc -> __score_doc.doc).filter(__int_id -> __int_id != __book_entity.getId()).boxed().collect(Collectors.toList()))
                    );
                } catch (final IOException __exception) {
                    // __exception.printStackTrace();
                }
            }
        );

        return __atomic_reference_result.get();
    }
    //---

    private void withSession(final ConfigVO __config_v_o, final Consumer<Session> __consumer) {
        try {
            final SessionFactory
                __session_factory = this.__loading_cache_session_factory.get(__config_v_o);
            ///
            ///
            __session_factory.getSchemaManager().exportMappedObjects(true);
            __session_factory.inTransaction(__consumer);
        } catch (final Exception __exception) {
            // __exception.printStackTrace();
        }
    }
    //---

    public record GetTotalConsumerDTO(CriteriaBuilder criteria_builder, CriteriaQuery<Long> criteria_query, Root<BookEntity> root) {
    }
    //---

    public record ListConsumerDTO(CriteriaBuilder criteria_builder, CriteriaQuery<BookEntity> criteria_query, Root<BookEntity> root) {
    }
}
