package io.github.dimous.tsundoku.application;

import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.dimous.tsundoku.data.service.IPathWatcherService;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;
import org.hibernate.SessionFactory;

public final class DefaultResourceInteractor implements IResourceInteractor {
    @Inject
    @Named("config")
    private IPathWatcherService
        __path_watcher_service_config;

    @Inject
    private LoadingCache<ConfigVO, SessionFactory>
        __loading_cache_session_factory;

    @Override
    public void dispose() {
        this.__path_watcher_service_config.stop();
        this.__loading_cache_session_factory.invalidateAll();
    }
}
