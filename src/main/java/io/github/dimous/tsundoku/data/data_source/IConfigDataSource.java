package io.github.dimous.tsundoku.data.data_source;

import io.github.dimous.tsundoku.data.dto.ConfigDTO;

public interface IConfigDataSource {
    ConfigDTO read() throws Exception;

    void write(final ConfigDTO __config_d_t_o) throws Exception;
}
