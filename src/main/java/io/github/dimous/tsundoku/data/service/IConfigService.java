package io.github.dimous.tsundoku.data.service;

import java.util.Map;

public interface IConfigService {
    String getDefaultSourceName();

    Map<String, Map<String, String>> read() throws Exception;

    void write(Map<String, Map<String, String>> __map_data) throws Exception;
}
