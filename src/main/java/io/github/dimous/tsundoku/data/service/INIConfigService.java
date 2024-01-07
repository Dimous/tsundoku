package io.github.dimous.tsundoku.data.service;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class INIConfigService implements IConfigService {
    private final File
        __file = new File("settings.ini");

    public Map<String, Map<String, String>> read() throws IOException {
        return this.read(this.__file);
    }

    public Map<String, Map<String, String>> read(final File __file) throws IOException {
        final Map<String, Map<String, String>>
            __map_data = new HashMap<>();
        String
            __string_current_section = null;
        Map<String, String>
            __map_current_section_data = null;
        ///
        ///
        for (String __string_line : Splitter.on(CharMatcher.anyOf("\r\n")).split(Files.asCharSource(__file, StandardCharsets.UTF_8).read())) {
            __string_line = __string_line.trim();

            if (__string_line.startsWith("[") && __string_line.endsWith("]")) {
                __map_current_section_data = new HashMap<>();
                __string_current_section = __string_line.substring(1, __string_line.length() - 1);

                __map_data.put(__string_current_section, __map_current_section_data);
            } else if (null != __string_current_section && __string_line.contains("=")) {
                final int
                    __int_equals_index = __string_line.indexOf("=");
                ///
                ///
                __map_current_section_data.put(__string_line.substring(0, __int_equals_index).trim(), __string_line.substring(1 + __int_equals_index).trim());
            }
        }

        return __map_data;
    }
    //---

    public void write(final Map<String, Map<String, String>> __map_data) throws IOException {
        this.write(this.__file, __map_data);
    }

    public void write(final File __file, final Map<String, Map<String, String>> __map_data) throws IOException {
        final StringBuilder
            __string_builder_contents = new StringBuilder();
        ///
        ///
        for (final Map.Entry<String, Map<String, String>> __entry_section : __map_data.entrySet()) {
            final Map<String, String>
                __map_section_data = __entry_section.getValue();
            ///
            ///
            __string_builder_contents.append("[").append(__entry_section.getKey()).append("]\n");

            for (final Map.Entry<String, String> __entry_key_value : __map_section_data.entrySet()) {
                __string_builder_contents.append(__entry_key_value.getKey()).append("=").append(__entry_key_value.getValue()).append("\n");
            }

            __string_builder_contents.append("\n");
        }

        Files.asCharSink(__file, StandardCharsets.UTF_8).write(__string_builder_contents.toString());
    }
}
