package io.github.dimous.tsundoku.data.data_source;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.dimous.tsundoku.data.dto.ConfigDTO;
import io.github.dimous.tsundoku.data.service.IConfigService;
import io.github.dimous.tsundoku.data.service.IPathWatcherService;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.util.Map;

public final class DefaultConfigDataSource implements IConfigDataSource {
    private final IConfigService
        __config_service;

    private static ConfigDTO
        __config_d_t_o;

    @Inject
    public DefaultConfigDataSource(final IConfigService __config_service, @Named("config") final IPathWatcherService __path_watcher_service) {
        this.__config_service = __config_service;

        __path_watcher_service.start(
            Path.of("."),
            __watch_event -> {
                if (__watch_event.context().getFileName().toString().equals(__config_service.getDefaultSourceName())) {
                    try {
                        __config_d_t_o = this.read_();
                    } catch (final Exception __exception) {
                        // __exception.printStackTrace();
                    }
                }
            },
            StandardWatchEventKinds.ENTRY_MODIFY
        );
    }
    //---

    public ConfigDTO read() throws Exception {
        if (null == __config_d_t_o) {
            __config_d_t_o = this.read_();
        }

        return __config_d_t_o;
    }
    //---

    public void write(final ConfigDTO __config_d_t_o) throws Exception {
        this.__config_service.write(Map.of("file-system", Map.of("base_path", __config_d_t_o.base_path(), "extensions", __config_d_t_o.extensions()), "database", Map.of("dialect", __config_d_t_o.dialect(), "url", __config_d_t_o.url(), "user", __config_d_t_o.user(), "password", __config_d_t_o.password())));
    }
    //---

    private ConfigDTO read_() throws Exception {
        final Map<String, Map<String, String>>
            __map_config = this.__config_service.read();
        final Map<String, String>
            __map_database = __map_config.get("database");
        final Map<String, String>
            __map_file_system = __map_config.get("file-system");
        ///
        ///
        return new ConfigDTO(__map_database.get("dialect"), __map_database.get("url"), __map_database.get("user"), __map_database.get("password"), __map_file_system.get("base_path"), __map_file_system.get("extensions"));
    }
}
