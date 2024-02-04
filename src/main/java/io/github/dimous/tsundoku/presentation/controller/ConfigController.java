package io.github.dimous.tsundoku.presentation.controller;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import io.github.dimous.tsundoku.application.IConfigInteractor;
import io.github.dimous.tsundoku.domain.vo.ConfigVO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public final class ConfigController implements Initializable {
    @Inject
    private IConfigInteractor
        __config_interactor;

    @FXML
    private Button
        __button_save;

    @FXML
    private TextField
        __text_field_url,
        __text_field_user,
        __text_field_driver,
        __text_field_dialect,
        __text_field_password,
        __text_field_base_path,
        __text_field_extensions;

    private final ConfigState
        __config_state = new ConfigState(
        new SimpleStringProperty(),
        new SimpleStringProperty(),
        new SimpleStringProperty(),
        new SimpleStringProperty(),
        new SimpleStringProperty(),
        new SimpleStringProperty(),
        new SimpleStringProperty()
    );

    @Override
    public void initialize(final URL __u_r_l, final ResourceBundle __resource_bundle) {
        this.__text_field_url.textProperty().bindBidirectional(this.__config_state.url);
        this.__text_field_user.textProperty().bindBidirectional(this.__config_state.user);
        this.__text_field_driver.textProperty().bindBidirectional(this.__config_state.driver);
        this.__text_field_dialect.textProperty().bindBidirectional(this.__config_state.dialect);
        this.__text_field_password.textProperty().bindBidirectional(this.__config_state.password);
        this.__text_field_base_path.textProperty().bindBidirectional(this.__config_state.base_path);
        this.__text_field_extensions.textProperty().bindBidirectional(this.__config_state.extensions);

        this.load();
    }
    //---

    public void onSaveButtonClick() {
        final Task<Void>
            __task = new Task<>() {
            @Override
            protected Void call() {
                __config_interactor.set(
                    new ConfigVO(__config_state.driver.get(), __config_state.dialect.get(), __config_state.url.get(), __config_state.user.get(), __config_state.password.get(), __config_state.base_path.get(), __config_state.extensions.get())
                );

                return null;
            }
        };
        final Thread
            __thread = new Thread(__task);
        ///
        ///
        __thread.start();
    }
    //---

    private void load() {
        final Task<Void>
            __task = new Task<>() {
            @Override
            protected Void call() {
                __config_interactor.get().ifPresent(
                    __config_v_o -> Platform.runLater(
                        () -> {
                            __config_state.url.set(__config_v_o.getUrl());
                            __config_state.user.set(__config_v_o.getUser());
                            __config_state.driver.set(__config_v_o.getDriver());
                            __config_state.dialect.set(__config_v_o.getDialect());
                            __config_state.password.set(__config_v_o.getPassword());
                            __config_state.base_path.set(__config_v_o.getBasePath());
                            __config_state.extensions.set(
                                Joiner.on(", ").join(__config_v_o.getExtensions())
                            );
                        }
                    )
                );

                return null;
            }
        };
        final Thread
            __thread = new Thread(__task);
        ///
        ///
        __thread.start();
    }
    //---

    private record ConfigState(
        SimpleStringProperty url,
        SimpleStringProperty user,
        SimpleStringProperty driver,
        SimpleStringProperty dialect,
        SimpleStringProperty password,
        SimpleStringProperty base_path,
        SimpleStringProperty extensions
    ) {
    }
}
