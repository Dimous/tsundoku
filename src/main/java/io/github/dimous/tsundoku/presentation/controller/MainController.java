package io.github.dimous.tsundoku.presentation.controller;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.presentation.view.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public final class MainController implements Initializable {
    @Inject
    private Util
        __util;

    @FXML
    private Tab
        __tab_all;

    @FXML
    private TabPane
        __tab_pane_root;

    @Override
    public void initialize(final URL __u_r_l, final ResourceBundle __resource_bundle) {
        this.__tab_pane_root.getSelectionModel().selectedItemProperty().addListener(
            (__observable_value, __tab_old, __tab_new) -> this.loadTabContent(__tab_new)
        );

        this.loadTabContent(this.__tab_all);
    }
    //---

    private void loadTabContent(final Tab __tab) {
        if (null == __tab.getContent()) {
            try {
                __tab.setContent(
                    this.__util.loadFXML((String) __tab.getUserData())
                );
            } catch (final Exception __exception) {
                // __exception.printStackTrace();
            }
        }
    }
}