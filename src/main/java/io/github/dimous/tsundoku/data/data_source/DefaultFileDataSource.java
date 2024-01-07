package io.github.dimous.tsundoku.data.data_source;

import com.google.common.collect.Iterables;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.MoreFiles;
import io.github.dimous.tsundoku.data.dto.FileDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;

public final class DefaultFileDataSource implements IFileDataSource {
    @Override
    public boolean deleteFile(final String __string_path) {
        final File
            __file = new File(__string_path);
        ///
        ///
        // Desktop.getDesktop().moveToTrash()
        return __file.exists() && __file.delete();
    }
    //---

    public Path[] list(final String __string_root_path, final Set<String> __set_extensions) {
        // нельзя предотвратить попытку доступа к System Volume Information, поэтому книги должны быть в подпапке
        return Arrays.stream(Iterables.toArray(MoreFiles.fileTraverser().breadthFirst(Paths.get(__string_root_path)), Path.class)).filter(__path -> __set_extensions.contains(MoreFiles.getFileExtension(__path))).toArray(Path[]::new);
    }
    //---

    public FileDTO getFile(final Path __path) throws IOException {
        final ByteSource
            __byte_source = MoreFiles.asByteSource(__path);
        ///
        ///
        return new FileDTO(__path.toString(), MoreFiles.getNameWithoutExtension(__path), MoreFiles.getFileExtension(__path), __byte_source.hash(Hashing.sha256()).toString(), __byte_source.size() / 1024, __byte_source);
    }
}
