package io.github.dimous.tsundoku.domain.contract;

import io.github.dimous.tsundoku.domain.vo.ConfigVO;

public interface IConfigRepository {
    ConfigVO read() throws Exception;

    void write(final ConfigVO __config_v_o) throws Exception;
}
