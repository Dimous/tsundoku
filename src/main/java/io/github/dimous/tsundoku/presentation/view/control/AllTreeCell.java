package io.github.dimous.tsundoku.presentation.view.control;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.presentation.controller.AllController;
import io.github.dimous.tsundoku.presentation.view.Util;
import io.github.dimous.tsundoku.presentation.view.dto.AllTreeNodeDTO;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeCell;

import java.util.ResourceBundle;

public final class AllTreeCell extends TreeCell<AllTreeNodeDTO> {
    private final Util
        __util;

    private final MenuItem
        __menu_item_open,
        __menu_item_delete;

    @Inject
    public AllTreeCell(final Util __util, final ResourceBundle __resource_bundle) {
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
    protected void updateItem(final AllTreeNodeDTO __all_tree_node_d_t_o, final boolean __boolean_is_empty) {
        final BookEntity
            __book_entity;
        ///
        ///
        super.updateItem(__all_tree_node_d_t_o, __boolean_is_empty);

        if (__boolean_is_empty || null == __all_tree_node_d_t_o) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            __book_entity = __all_tree_node_d_t_o.getBookEntity();

            if (null == __book_entity) {
                this.setText(__all_tree_node_d_t_o.getPathChunk());
            } else {
                this.__menu_item_open.setOnAction(
                    __event -> {
                        final AllController
                            __all_controller = (AllController) this.getTreeView().getUserData();
                        ///
                        ///
                        __all_controller.onOpen(__book_entity);
                    }
                );
                this.__menu_item_delete.setOnAction(
                    __event -> {
                        final AllController
                            __all_controller = (AllController) this.getTreeView().getUserData();
                        ///
                        ///
                        __all_controller.onDelete(__book_entity);
                    }
                );

                this.setText(__book_entity.getName());
            }
        }
    }
}
