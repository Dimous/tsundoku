package io.github.dimous.tsundoku.application;

import io.github.dimous.tsundoku.domain.vo.ConfigVO;

import java.util.Optional;

public interface IConfigInteractor {
    Optional<ConfigVO> get();

    void set(final ConfigVO __config_v_o);
}
