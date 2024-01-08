package io.github.dimous.tsundoku.data.repository;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.data.data_source.IBookDataSource;
import io.github.dimous.tsundoku.data.data_source.IFileDataSource;
import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;
import io.github.dimous.tsundoku.data.service.IISBNRetrieverService;
import io.github.dimous.tsundoku.domain.contract.IBookRepository;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.domain.entity.ConfigEntity;
import io.github.dimous.tsundoku.domain.entity.NumberEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.nio.file.Path;
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

    @Override
    public Map<String, List<BookEntity>> getIdenticals(final ConfigEntity __config_entity) {
        final AtomicReference<Map<String, List<BookEntity>>>
            __atomic_reference_result = new AtomicReference<>(Map.of());
        ///
        ///
        this.withSession(
            __config_entity,
            __session -> __atomic_reference_result.set(
                __session.createSelectionQuery("FROM BookEntity WHERE hash IN (SELECT hash FROM BookEntity GROUP BY hash HAVING COUNT(hash) > 1) ORDER BY hash", BookEntity.class).getResultList().stream().collect(Collectors.groupingBy(BookEntity::getHash))
            )
        );

        return __atomic_reference_result.get();
    }
    //---

    @Override
    public Map<String, Set<BookEntity>> getDuplicates(final ConfigEntity __config_entity) {
        final AtomicReference<Map<String, Set<BookEntity>>>
            __atomic_reference_result = new AtomicReference<>(Map.of());
        ///
        ///
        this.withSession(
            __config_entity,
            __session -> __atomic_reference_result.set(
                __session.createSelectionQuery("SELECT n FROM NumberEntity n JOIN n.books b GROUP BY n HAVING COUNT(b) > 1 ORDER BY n", NumberEntity.class).getResultList().stream().collect(Collectors.toMap(NumberEntity::getValue, NumberEntity::getBooks))
            )
        );

        return __atomic_reference_result.get();
    }
    //---

    @Override
    public long getTotal(final ConfigEntity __config_entity) {
        return this.getTotal(__config_entity, null);
    }

    @Override
    public long getTotal(final ConfigEntity __config_entity, final Consumer<GetTotalConsumerDTO> __consumer) {
        final AtomicReference<Long>
            __atomic_reference_result = new AtomicReference<>(0L);
        ///
        ///
        this.withSession(
            __config_entity,
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
    public void remove(final ConfigEntity __config_entity, final BookEntity __book_entity) {
        this.withSession(
            __config_entity,
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
    public void ingest(final ConfigEntity __config_entity, final Function<TraversedBookDTO, Boolean> __function_on_progress) {
        this.__book_data_source.traverseFileSystem(
            __config_entity.getBasePath(),
            __config_entity.getExtensions(),
            __traversed_book_d_t_o -> {
                // пакетный сброс сессии с gc не срабатывал, OOM на ~5600 элементе, покэтому -- сессия/элемент
                this.withSession(
                    __config_entity,
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
    public List<BookEntity> list(final ConfigEntity __config_entity, final int __int_offset, final int __int_limit) {
        return this.list(__config_entity, __int_offset, __int_limit, null);
    }

    @Override
    public List<BookEntity> list(final ConfigEntity __config_entity, final int __int_offset, final int __int_limit, final Consumer<ListConsumerDTO> __consumer) {
        final AtomicReference<List<BookEntity>>
            __atomic_reference_result = new AtomicReference<>(List.of());
        ///
        ///
        this.withSession(
            __config_entity,
            __session -> {
                final CriteriaBuilder
                    __criteria_builder = __session.getCriteriaBuilder();
                final CriteriaQuery<BookEntity>
                    __criteria_query = __criteria_builder.createQuery(BookEntity.class);
                final Root<BookEntity>
                    __root = __criteria_query.from(BookEntity.class);

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
    public List<BookEntity> search(final ConfigEntity __config_entity, final String __string_keyword) {
        final AtomicReference<List<BookEntity>>
            __atomic_reference_result = new AtomicReference<>(List.of());
        ///
        ///
        this.withSession(
            __config_entity,
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
    public List<BookEntity> check(final ConfigEntity __config_entity, final Path __path) {
        final AtomicReference<List<BookEntity>>
            __atomic_reference_result = new AtomicReference<>(List.of());
        ///
        ///
        this.__book_data_source.getBook(__path).ifPresent(
            __book_d_t_o -> this.withSession(
                __config_entity,
                __session -> __atomic_reference_result.set(
                    __session.createSelectionQuery("SELECT b FROM BookEntity b JOIN b.numbers n WHERE b.hash = :hash OR n.value IN (:numbers)", BookEntity.class).setParameter("hash", __book_d_t_o.hash()).setParameter("numbers", this.__i_s_b_n_retriever_service.retrieve(__book_d_t_o.content())).getResultList()
                )
            )
        );

        return __atomic_reference_result.get();
    }
    //---

    // MoreLikeThisQuery напрямую -- https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#_retrieving_a_lucene_indexreader
    // https://hibernate.atlassian.net/browse/HSEARCH-3272
    // https://stackoverflow.com/a/70694647
    //---

    private void withSession(final ConfigEntity __config_entity, final Consumer<Session> __consumer) {
        try (
            final SessionFactory
                __session_factory = new Configuration().addAnnotatedClass(BookEntity.class).addAnnotatedClass(NumberEntity.class).setProperty(JdbcSettings.URL, __config_entity.getUrl()).setProperty(JdbcSettings.USER, __config_entity.getUser()).setProperty(JdbcSettings.PASS, __config_entity.getPassword()).setProperty(JdbcSettings.DIALECT, __config_entity.getDialect()).setProperty("hibernate.hbm2ddl.auto", "update").buildSessionFactory()
        ) {
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
