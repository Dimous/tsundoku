package io.github.dimous.tsundoku.application;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.domain.contract.IConfigRepository;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;

import java.util.Optional;

public final class DefaultConfigInteractor implements IConfigInteractor {
    @Inject
    private IConfigRepository
        __config_repository;

    @Override
    public Optional<ConfigVO> get() {
        try {
            return Optional.of(this.__config_repository.read());
        } catch (final Exception __exception) {
            return Optional.empty();
        }
    }
    //---

    @Override
    public void set(final ConfigVO __config_v_o) {
        try {
            this.__config_repository.write(__config_v_o);
        } catch (final Exception __exception) {
            // __exception.printStackTrace();
        }
    }
}
