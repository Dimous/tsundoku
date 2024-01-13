package io.github.dimous.tsundoku.data.repository;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.data.data_source.IConfigDataSource;
import io.github.dimous.tsundoku.data.mapper.ConfigMapper;
import io.github.dimous.tsundoku.domain.contract.IConfigRepository;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;

public final class DefaultConfigRepository implements IConfigRepository {
    @Inject
    private IConfigDataSource
        __config_data_source;

    @Override
    public ConfigVO read() throws Exception {
        return ConfigMapper.to(this.__config_data_source.read());
    }
    //---

    @Override
    public void write(final ConfigVO __config_v_o) throws Exception {
        this.__config_data_source.write(ConfigMapper.from(__config_v_o));
    }
}
