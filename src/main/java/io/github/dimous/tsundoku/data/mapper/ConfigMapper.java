package io.github.dimous.tsundoku.data.mapper;

import io.github.dimous.tsundoku.data.dto.ConfigDTO;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;

public final class ConfigMapper {
    public static ConfigVO to(final ConfigDTO __config_d_t_o) {
        return new ConfigVO(__config_d_t_o.dialect(), __config_d_t_o.url(), __config_d_t_o.user(), __config_d_t_o.password(), __config_d_t_o.base_path(), __config_d_t_o.extensions());
    }
    //---

    public static ConfigDTO from(final ConfigVO __config_v_o) {
        return new ConfigDTO(__config_v_o.getDialect(), __config_v_o.getUrl(), __config_v_o.getUser(), __config_v_o.getPassword(), __config_v_o.getBasePath(), String.join(",", __config_v_o.getExtensions()));
    }
}
