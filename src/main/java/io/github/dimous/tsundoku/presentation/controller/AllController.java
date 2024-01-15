package io.github.dimous.tsundoku.presentation.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.github.dimous.tsundoku.application.IBookInteractor;
import io.github.dimous.tsundoku.di.GuiceInjector;
import io.github.dimous.tsundoku.presentation.view.Util;
import io.github.dimous.tsundoku.presentation.view.control.AllTreeCell;
import io.github.dimous.tsundoku.presentation.view.dto.AllDTO;
import io.github.dimous.tsundoku.presentation.view.dto.AllTreeNodeDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class AllController implements Initializable {
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
    private VBox
        __v_box_root;

    @FXML
    private TreeView<AllTreeNodeDTO>
        __tree_view;

    @FXML
    private StatusBar
        __status_bar;

    @FXML
    private Button
        __button_search,
        __button_settings;

    @FXML
    private TextField
        __text_field_keyword;

    @FXML
    private ProgressIndicator
        __progress_indicator;

    @FXML
    private ToggleButton
        __toggle_button_ingest;

    private final AllState
        __all_state = new AllState(
        new SimpleStringProperty(),
        new SimpleStringProperty(),
        new SimpleBooleanProperty(),
        new SimpleBooleanProperty(),
        new SimpleObjectProperty<>()
    );

    @Override
    public void initialize(final URL __u_r_l, final ResourceBundle __resource_bundle) {
        this.__v_box_root.setOnDragOver(
            __drag_event -> {
                if (__drag_event.getDragboard().hasFiles()) {
                    __drag_event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                __drag_event.consume();
            }
        );
        this.__v_box_root.setOnDragDropped(
            __drag_event -> {
                boolean
                    __boolean_is_success = false;
                final Dragboard
                    __dragboard = __drag_event.getDragboard();
                ///
                ///
                if (__dragboard.hasFiles()) {
                    __dragboard.getFiles().stream().map(File::getAbsolutePath).forEach(this::check);

                    __boolean_is_success = true;
                }

                __drag_event.setDropCompleted(__boolean_is_success);
                __drag_event.consume();
            }
        );

        this.__tree_view.setShowRoot(false);
        this.__tree_view.setCellFactory(
            __tree_view -> this.__injector.getInstance(AllTreeCell.class)
        );
        // https://github.com/cerebrosoft/treeview-dnd-example
        this.__tree_view.getSelectionModel().selectedItemProperty().addListener(
            (__observable_value, __tree_item_old, __tree_item_new) -> {
                System.out.println(__tree_item_new.getValue().getBookEntity());
            }
        );

        this.__status_bar.textProperty().bindBidirectional(this.__all_state.status);
        this.__text_field_keyword.textProperty().bindBidirectional(this.__all_state.keyword);
        this.__button_search.disableProperty().bindBidirectional(this.__all_state.is_in_progress);
        this.__button_settings.disableProperty().bindBidirectional(this.__all_state.is_in_progress);
        this.__progress_indicator.visibleProperty().bindBidirectional(this.__all_state.is_in_progress);
        this.__text_field_keyword.disableProperty().bindBidirectional(this.__all_state.is_in_progress);
        this.__toggle_button_ingest.selectedProperty().bindBidirectional(this.__all_state.is_ingesting);

        this.__all_state.is_in_progress.addListener(
            (__observable_value, __boolean_old, __boolean_new) -> this.__toggle_button_ingest.setDisable(__boolean_new && !this.__all_state.is_ingesting.get())
        );
        this.__all_state.tree_view_root.addListener(
            (__observable_value, __tree_item_old, __tree_item_new) -> {
                if (__tree_item_new.isLeaf()) {
                    this.__util.showAlert(Alert.AlertType.INFORMATION, this.__resource_bundle.getString("ui.alert.all_empty_info_title"), this.__resource_bundle.getString("ui.alert.all_empty_info_header"), this.__resource_bundle.getString("ui.alert.all_empty_info_content"));
                } else {
                    this.__tree_view.setRoot(__tree_item_new);
                }
            }
        );

        // todo вызывать реактивно на изменение данных в таблице Books
        // https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#events-events
        this.load();
    }
    //---

    public void onIngestToggleButtonClick() {
        final Thread
            __thread;
        final Task<Void>
            __task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(
                    () -> __all_state.is_in_progress.set(true)
                );

                if (__all_state.is_ingesting.get()) {
                    __book_interactor.ingest(
                        __traversed_book_d_t_o -> {
                            Platform.runLater(
                                () -> __all_state.status.set(
                                    MessageFormat.format(__resource_bundle.getString("ui.status_bar.all_index"), __traversed_book_d_t_o.step(), __traversed_book_d_t_o.total())
                                )
                            );

                            return !__all_state.is_ingesting.get();
                        }
                    );
                }

                return null;
            }
        };
        ///
        ///
        __task.setOnFailed(
            __worker_state_event -> {
                this.__all_state.is_ingesting.set(false);
                this.__all_state.is_in_progress.set(false);
            }
        );

        __task.setOnSucceeded(
            __worker_state_event -> {
                this.__all_state.is_ingesting.set(false);
                this.__all_state.is_in_progress.set(false);
            }
        );

        __thread = new Thread(__task);

        __thread.start();
    }
    //---

    public void onSearchButtonClick() {
        this.load(this.__all_state.keyword.get());
    }
    //---

    public void onSettingsButtonClick() {
        this.__util.showModal("config", this.__resource_bundle.getString("ui.button.settings"), 300, 300);
    }
    //---

    private void load() {
        this.load(null);
    }

    private void load(final String __string_keyword) {
        final Thread
            __thread;
        final Task<AllDTO>
            __task = new Task<>() {
            @Override
            protected AllDTO call() throws Exception {
                Platform.runLater(
                    () -> __all_state.is_in_progress.set(true)
                );

                return __book_interactor.getAll(__string_keyword);
            }
        };
        ///
        ///
        __task.setOnFailed(
            __worker_state_event -> {
                this.__all_state.is_in_progress.set(false);

                this.__util.showAlert(Alert.AlertType.ERROR, this.__resource_bundle.getString("ui.alert.all_error_title"), this.__resource_bundle.getString("ui.alert.all_error_header"), this.__resource_bundle.getString("ui.alert.all_error_content"));
            }
        );

        __task.setOnSucceeded(
            __worker_state_event -> {
                final AllDTO
                    __all_d_t_o = __task.getValue();
                ///
                ///
                this.__all_state.is_in_progress.set(false);

                this.__all_state.status.set(
                    MessageFormat.format(this.__resource_bundle.getString("ui.status_bar.all_total"), __all_d_t_o.total())
                );

                this.__all_state.tree_view_root.set(__all_d_t_o.root());
            }
        );

        __thread = new Thread(__task);

        __thread.start();
    }
    //---

    private void check(final String __string_path) {
        final Thread
            __thread;
        final Task<String>
            __task = new Task<>() {
            @Override
            protected String call() throws Exception {
                Platform.runLater(
                    () -> __all_state.is_in_progress.set(true)
                );

                return __book_interactor.check(__string_path);
            }
        };
        ///
        ///
        __task.setOnFailed(
            __worker_state_event -> this.__all_state.is_in_progress.set(false)
        );

        __task.setOnSucceeded(
            __worker_state_event -> {
                final String
                    __string_found_books = __task.getValue();
                ///
                ///
                this.__all_state.is_in_progress.set(false);

                if (__string_found_books.isEmpty()) {
                    this.__util.showAlert(Alert.AlertType.INFORMATION, this.__resource_bundle.getString("ui.alert.check_not_found_title"), this.__resource_bundle.getString("ui.alert.check_not_found_header"), this.__resource_bundle.getString("ui.alert.check_not_found_content"));
                } else {
                    this.__util.showAlert(Alert.AlertType.INFORMATION, this.__resource_bundle.getString("ui.alert.check_found_title"), this.__resource_bundle.getString("ui.alert.check_found_header"), MessageFormat.format(this.__resource_bundle.getString("ui.alert.check_found_content"), __string_found_books));
                }
            }
        );

        __thread = new Thread(__task);

        __thread.start();
    }
    //---

    private record AllState(
        SimpleStringProperty status,
        SimpleStringProperty keyword,
        SimpleBooleanProperty is_ingesting,
        SimpleBooleanProperty is_in_progress,
        SimpleObjectProperty<TreeItem<AllTreeNodeDTO>> tree_view_root
    ) {
    }
}
