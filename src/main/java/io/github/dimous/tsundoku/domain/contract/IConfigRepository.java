package io.github.dimous.tsundoku.domain.contract;

import io.github.dimous.tsundoku.domain.entity.ConfigEntity;

public interface IConfigRepository {
    ConfigEntity read() throws Exception;

    void write(final ConfigEntity __config_entity) throws Exception;
}
