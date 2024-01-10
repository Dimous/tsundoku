package io.github.dimous.tsundoku.application;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import io.github.dimous.tsundoku.data.dto.TraversedBookDTO;
import io.github.dimous.tsundoku.domain.contract.IBookRepository;
import io.github.dimous.tsundoku.domain.contract.IConfigRepository;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.domain.entity.ConfigEntity;
import io.github.dimous.tsundoku.presentation.view.dto.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DefaultBookInteractor implements IBookInteractor {
    @Inject
    private IBookRepository
        __book_repository;

    @Inject
    private IConfigRepository
        __config_repository;

    @Override
    public AllDTO getAll() throws Exception {
        return this.getAll(null);
    }

    @Override
    public AllDTO getAll(final String __string_keyword) throws Exception {
        final TreeItem<AllTreeNodeDTO>
            __tree_item_root = new TreeItem<>(null);
        final ConfigEntity
            __config_entity = this.__config_repository.read();
        final int
            __int_total = Math.toIntExact(this.__book_repository.getTotal(__config_entity));
        ///
        ///
        for (final BookEntity __book_entity : null == __string_keyword || __string_keyword.isEmpty() ? this.__book_repository.list(__config_entity, 0, __int_total) : this.__book_repository.search(__config_entity, __string_keyword)) {
            TreeItem<AllTreeNodeDTO>
                __tree_item_current = __tree_item_root;
            final Path
                __path = Paths.get(__book_entity.getPath());
            ///
            ///
            for (int __int_index = 0, __int_count = __path.getNameCount(); __int_index < __int_count; ++__int_index) {
                final String
                    __string_path_chunk = __path.getName(__int_index).toString();
                final ObservableList<TreeItem<AllTreeNodeDTO>>
                    __observable_list_children = __tree_item_current.getChildren();
                final Optional<TreeItem<AllTreeNodeDTO>>
                    __optional_current = __observable_list_children.stream().filter(__tree_item -> __string_path_chunk.equals(__tree_item.getValue().getPathChunk())).findAny();
                ///
                ///
                if (__optional_current.isPresent()) {
                    __tree_item_current = __optional_current.get();
                } else {
                    __observable_list_children.add((__tree_item_current = new TreeItem<>(new AllTreeNodeDTO(__string_path_chunk))));
                }

                if (__string_path_chunk.endsWith(__book_entity.getExtension())) {
                    __tree_item_current.getValue().setBookEntity(__book_entity);
                }

                // __tree_item_current.setExpanded(true);
            }
        }

        return new AllDTO(__tree_item_root, __int_total);
    }
    //---

    @Override
    public DuplicatesDTO getDuplicates() throws Exception {
        final TreeItem<DuplicatesTreeNodeDTO>
            __tree_item_root = new TreeItem<>(null);
        ///
        ///
        this.__book_repository.getDuplicates(this.__config_repository.read()).forEach(
            (__string_key, __set_values) -> {
                final TreeItem<DuplicatesTreeNodeDTO>
                    __tree_item_key = new TreeItem<>(new DuplicatesTreeNodeDTO(__string_key, null));
                ///
                ///
                __tree_item_root.getChildren().add(__tree_item_key);

                __set_values.forEach(
                    __book_entity -> __tree_item_key.getChildren().add(new TreeItem<>(new DuplicatesTreeNodeDTO(null, __book_entity)))
                );
            }
        );

        return new DuplicatesDTO(__tree_item_root);
    }
    //---

    @Override
    public IdenticalsDTO getIdenticals() throws Exception {
        final TreeItem<IdenticalsTreeNodeDTO>
            __tree_item_root = new TreeItem<>(null);
        ///
        ///
        this.__book_repository.getIdenticals(this.__config_repository.read()).forEach(
            (__string_key, __list_values) -> {
                final TreeItem<IdenticalsTreeNodeDTO>
                    __tree_item_key = new TreeItem<>(new IdenticalsTreeNodeDTO(__string_key, null));
                ///
                ///
                __tree_item_root.getChildren().add(__tree_item_key);

                __list_values.forEach(
                    __book_entity -> __tree_item_key.getChildren().add(new TreeItem<>(new IdenticalsTreeNodeDTO(null, __book_entity)))
                );
            }
        );

        return new IdenticalsDTO(__tree_item_root);
    }
    //---

    @Override
    public void open(final BookEntity __book_entity) {
        final File
            __file = new File(__book_entity.getPath());
        ///
        ///
        if (__file.exists()) {
            try {
                Desktop.getDesktop().open(__file);
            } catch (final IOException __exception) {
                // __exception.printStackTrace();
            }
        }
    }
    //---

    @Override
    public void remove(final BookEntity __book_entity) throws Exception {
        this.__book_repository.remove(this.__config_repository.read(), __book_entity);
    }
    //---

    @Override
    public void ingest(Function<TraversedBookDTO, Boolean> __function_on_progress) throws Exception {
        this.__book_repository.ingest(this.__config_repository.read(), __function_on_progress);
    }
    //---

    @Override
    public String check(final String __string_path) throws Exception {
        return Joiner.on(", ").join(this.__book_repository.check(this.__config_repository.read(), Path.of(__string_path)).stream().limit(5).map(BookEntity::getName).collect(Collectors.toList()));
    }
    //---

    @Override
    public List<BookEntity> getMoreLikeThis(final BookEntity __book_entity, final int __int_limit, final int __int_min_doc_freq, final int __int_min_term_freq) throws Exception {
        return this.__book_repository.getMoreLikeThis(this.__config_repository.read(), __book_entity, __int_limit, __int_min_doc_freq, __int_min_term_freq);
    }
}
