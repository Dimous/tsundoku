package io.github.dimous.tsundoku.data.mapper;

import io.github.dimous.tsundoku.data.dto.ConfigDTO;
import io.github.dimous.tsundoku.domain.entity.ConfigEntity;

public final class ConfigMapper {
    public static ConfigEntity to(final ConfigDTO __config_d_t_o) {
        return new ConfigEntity(__config_d_t_o.dialect(), __config_d_t_o.url(), __config_d_t_o.user(), __config_d_t_o.password(), __config_d_t_o.base_path(), __config_d_t_o.extensions());
    }
    //---

    public static ConfigDTO from(final ConfigEntity __config_entity) {
        return new ConfigDTO(__config_entity.getDialect(), __config_entity.getUrl(), __config_entity.getUser(), __config_entity.getPassword(), __config_entity.getBasePath(), String.join(",", __config_entity.getExtensions()));
    }
}
