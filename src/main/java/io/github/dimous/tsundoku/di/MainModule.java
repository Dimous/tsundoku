package io.github.dimous.tsundoku.di;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.github.dimous.tsundoku.application.*;
import io.github.dimous.tsundoku.data.data_source.*;
import io.github.dimous.tsundoku.data.repository.DefaultBookRepository;
import io.github.dimous.tsundoku.data.repository.DefaultConfigRepository;
import io.github.dimous.tsundoku.data.service.*;
import io.github.dimous.tsundoku.domain.contract.IBookRepository;
import io.github.dimous.tsundoku.domain.contract.IConfigRepository;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.domain.entity.NumberEntity;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;
import io.github.dimous.tsundoku.presentation.view.Util;
import io.github.dimous.tsundoku.presentation.view.control.DuplicatesTreeCell;
import io.github.dimous.tsundoku.presentation.view.control.IdenticalsTreeCell;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;

import java.util.ResourceBundle;

public final class MainModule extends AbstractModule {
    /**
     * @deprecated для получения кеша напрямую
     */
    public final static Key<LoadingCache<ConfigVO, SessionFactory>>
        SESSION_FACTORY_CACHE = new Key<>() {
    };

    @Override
    protected void configure() {
        this.bind(IdenticalsTreeCell.class);
        this.bind(DuplicatesTreeCell.class);
        this.bind(Util.class).in(Scopes.SINGLETON);
        this.bind(ResourceBundle.class).toInstance(ResourceBundle.getBundle("strings"));
        this.bind(IConfigService.class).to(INIConfigService.class).in(Scopes.SINGLETON);
        this.bind(IBookInteractor.class).to(DefaultBookInteractor.class).in(Scopes.SINGLETON);
        this.bind(IBookDataSource.class).to(DefaultBookDataSource.class).in(Scopes.SINGLETON);
        this.bind(IFileDataSource.class).to(DefaultFileDataSource.class).in(Scopes.SINGLETON);
        this.bind(IBookRepository.class).to(DefaultBookRepository.class).in(Scopes.SINGLETON);
        this.bind(IConfigRepository.class).to(DefaultConfigRepository.class).in(Scopes.SINGLETON);
        this.bind(IConfigDataSource.class).to(DefaultConfigDataSource.class).in(Scopes.SINGLETON);
        this.bind(IConfigInteractor.class).to(DefaultConfigInteractor.class).in(Scopes.SINGLETON);
        this.bind(IResourceInteractor.class).to(DefaultResourceInteractor.class).in(Scopes.SINGLETON);
        this.bind(IISBNRetrieverService.class).to(DefaultISBNRetrieverService.class).in(Scopes.SINGLETON);
        this.bind(IContentRetrieverService.class).to(TikaContentRetrieverService.class).in(Scopes.SINGLETON);
        this.bind(IPathWatcherService.class).annotatedWith(Names.named("config")).to(DefaultPathWatcherService.class).in(Scopes.SINGLETON);
        this.bind(
            new TypeLiteral<LoadingCache<ConfigVO, SessionFactory>>() {
            }
        ).toInstance(
            CacheBuilder
                .newBuilder()
                .maximumSize(1)
                .removalListener(
                    (RemovalListener<ConfigVO, SessionFactory>) __removal_notification -> {
                        final SessionFactory
                            __session_factory = __removal_notification.getValue();
                        ///
                        ///
                        if (null != __session_factory) {
                            __session_factory.close();
                        }
                    }
                )
                .build(
                    new CacheLoader<>() {
                        @Override
                        public SessionFactory load(final ConfigVO __config_v_o) {
                            return new Configuration().addAnnotatedClass(BookEntity.class).addAnnotatedClass(NumberEntity.class).setProperty(JdbcSettings.URL, __config_v_o.getUrl()).setProperty(JdbcSettings.USER, __config_v_o.getUser()).setProperty(JdbcSettings.PASS, __config_v_o.getPassword()).setProperty(JdbcSettings.DRIVER, __config_v_o.getDriver()).setProperty(JdbcSettings.DIALECT, __config_v_o.getDialect()).setProperty(SchemaToolingSettings.HBM2DDL_AUTO, "create").setProperty(JdbcSettings.SHOW_SQL, "true").setProperty(JdbcSettings.FORMAT_SQL, "true").setProperty(JdbcSettings.USE_SQL_COMMENTS, "true").buildSessionFactory();
                        }
                    }
                )
        );
    }
}
