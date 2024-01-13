package io.github.dimous.tsundoku.data.data_source;

import io.github.dimous.tsundoku.data.dto.FileDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface IFileDataSource {
    boolean deleteFile(final String __string_path);

    FileDTO getFile(final Path __path) throws IOException;

    Path[] list(final String __string_root_path, final Set<String> __set_extensions);
}
