package io.github.dimous.tsundoku.application;

import io.github.dimous.tsundoku.domain.entity.ConfigEntity;

import java.util.Optional;

public interface IConfigInteractor {
    Optional<ConfigEntity> get();

    void set(final ConfigEntity __config_entity);
}
