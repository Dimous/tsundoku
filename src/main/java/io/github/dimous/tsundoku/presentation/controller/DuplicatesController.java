package io.github.dimous.tsundoku.presentation.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.github.dimous.tsundoku.application.IBookInteractor;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.presentation.view.Util;
import io.github.dimous.tsundoku.presentation.view.control.DuplicatesTreeCell;
import io.github.dimous.tsundoku.presentation.view.dto.DuplicatesDTO;
import io.github.dimous.tsundoku.presentation.view.dto.DuplicatesTreeNodeDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class DuplicatesController implements Initializable {
    @Inject
    private Util
        __util;

    @Inject
    private Injector
        __injector;

    @Inject
    private IBookInteractor
        __book_interactor;

    @Inject
    private ResourceBundle
        __resource_bundle;

    @FXML
    private TreeView<DuplicatesTreeNodeDTO>
        __tree_view;

    @FXML
    private ProgressIndicator
        __progress_indicator;

    private final DuplicatesState
        __duplicates_state = new DuplicatesState(
        new SimpleBooleanProperty(),
        new SimpleObjectProperty<>()
    );

    @Override
    public void initialize(final URL __u_r_l, final ResourceBundle __resource_bundle) {
        this.__tree_view.setUserData(this);
        this.__tree_view.setShowRoot(false);
        this.__tree_view.setCellFactory(
            __tree_view -> this.__injector.getInstance(DuplicatesTreeCell.class)
        );

        this.__progress_indicator.visibleProperty().bindBidirectional(this.__duplicates_state.is_in_progress);

        this.__duplicates_state.tree_view_root.addListener(
            (__observable_value, __tree_item_old, __tree_item_new) -> {
                if (__tree_item_new.isLeaf()) {
                    this.__util.showAlert(Alert.AlertType.INFORMATION, this.__resource_bundle.getString("ui.alert.duplicates_empty_info_title"), this.__resource_bundle.getString("ui.alert.duplicates_empty_info_header"), this.__resource_bundle.getString("ui.alert.duplicates_empty_info_content"));
                } else {
                    this.__tree_view.setRoot(__tree_item_new);
                }
            }
        );

        this.load();
    }
    //---

    public void onOpen(final BookEntity __book_entity) {
        this.__book_interactor.open(__book_entity);
    }
    //---

    public void onDelete(final BookEntity __book_entity) {
        this.__util.showAlert(Alert.AlertType.CONFIRMATION, this.__resource_bundle.getString("ui.alert.removal_confirmation_title"), this.__resource_bundle.getString("ui.alert.removal_confirmation_header"), MessageFormat.format(this.__resource_bundle.getString("ui.alert.removal_confirmation_content"), __book_entity.getName())).ifPresent(
            __button_type -> {
                if (ButtonType.OK == __button_type) {
                    try {
                        this.__book_interactor.remove(__book_entity);
                    } catch (final Exception __exception) {
                        // __exception.printStackTrace();
                    }

                    this.load();
                }
            }
        );
    }
    //---

    private void load() {
        final Thread
            __thread;
        final Task<DuplicatesDTO>
            __task = new Task<>() {
            @Override
            protected DuplicatesDTO call() throws Exception {
                Platform.runLater(
                    () -> __duplicates_state.is_in_progress.set(true)
                );

                return __book_interactor.getDuplicates();
            }
        };
        ///
        ///
        __task.setOnFailed(
            __worker_state_event -> {
                this.__duplicates_state.is_in_progress.set(false);

                this.__util.showAlert(Alert.AlertType.ERROR, this.__resource_bundle.getString("ui.alert.duplicates_error_title"), this.__resource_bundle.getString("ui.alert.duplicates_error_header"), this.__resource_bundle.getString("ui.alert.duplicates_error_content"));
            }
        );

        __task.setOnSucceeded(
            __worker_state_event -> {
                this.__duplicates_state.is_in_progress.set(false);
                this.__duplicates_state.tree_view_root.set(__task.getValue().root());
            }
        );

        __thread = new Thread(__task);

        __thread.start();
    }
    //---

    private record DuplicatesState(
        SimpleBooleanProperty is_in_progress,
        SimpleObjectProperty<TreeItem<DuplicatesTreeNodeDTO>> tree_view_root
    ) {
    }
}
