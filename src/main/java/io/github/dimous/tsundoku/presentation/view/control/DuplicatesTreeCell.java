package io.github.dimous.tsundoku.presentation.view.control;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.presentation.controller.DuplicatesController;
import io.github.dimous.tsundoku.presentation.view.Util;
import io.github.dimous.tsundoku.presentation.view.dto.DuplicatesTreeNodeDTO;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeCell;

import java.util.ResourceBundle;

public final class DuplicatesTreeCell extends TreeCell<DuplicatesTreeNodeDTO> {
    private final Util
        __util;

    private final MenuItem
        __menu_item_open,
        __menu_item_delete;

    @Inject
    public DuplicatesTreeCell(final Util __util, final ResourceBundle __resource_bundle) {
        final ContextMenu
            __context_menu = new ContextMenu();
        ///
        ///
        this.__util = __util;
        this.__menu_item_open = new MenuItem(
            __resource_bundle.getString("ui.button.open")
        );
        this.__menu_item_delete = new MenuItem(
            __resource_bundle.getString("ui.button.delete")
        );

        __context_menu.getItems().addAll(
            this.__menu_item_open,
            new SeparatorMenuItem(),
            this.__menu_item_delete
        );

        this.setContextMenu(__context_menu);
    }
    //---

    @Override
    protected void updateItem(final DuplicatesTreeNodeDTO __duplicates_tree_node_d_t_o, final boolean __boolean_is_empty) {
        super.updateItem(__duplicates_tree_node_d_t_o, __boolean_is_empty);

        if (__boolean_is_empty || null == __duplicates_tree_node_d_t_o) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            if (null == __duplicates_tree_node_d_t_o.book()) {
                this.setText(__duplicates_tree_node_d_t_o.number());
            } else {
                this.__menu_item_open.setOnAction(
                    __event -> {
                        final DuplicatesController
                            __duplicates_controller = (DuplicatesController) this.getTreeView().getUserData();
                        ///
                        ///
                        __duplicates_controller.onOpen(__duplicates_tree_node_d_t_o.book());
                    }
                );
                this.__menu_item_delete.setOnAction(
                    __event -> {
                        final DuplicatesController
                            __duplicates_controller = (DuplicatesController) this.getTreeView().getUserData();
                        ///
                        ///
                        __duplicates_controller.onDelete(__duplicates_tree_node_d_t_o.book());
                    }
                );

                this.setText(String.format("%s (%d Kb)", __duplicates_tree_node_d_t_o.book().getPath(), __duplicates_tree_node_d_t_o.book().getSize()));

                // извлекать в нитке
                // this.__util.retrieveCover(__duplicates_tree_node_d_t_o.book()).ifPresent(this::setGraphic);
            }
        }
    }
}
