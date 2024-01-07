package io.github.dimous.tsundoku.application;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.domain.contract.IConfigRepository;
import io.github.dimous.tsundoku.domain.entity.ConfigEntity;

import java.util.Optional;

public class DefaultConfigInteractor implements IConfigInteractor {
    @Inject
    private IConfigRepository
        __config_repository;

    @Override
    public Optional<ConfigEntity> get() {
        try {
            return Optional.of(this.__config_repository.read());
        } catch (final Exception __exception) {
            return Optional.empty();
        }
    }
    //---

    @Override
    public void set(final ConfigEntity __config_entity) {
        try {
            this.__config_repository.write(__config_entity);
        } catch (final Exception __exception) {
            // __exception.printStackTrace();
        }
    }
}
