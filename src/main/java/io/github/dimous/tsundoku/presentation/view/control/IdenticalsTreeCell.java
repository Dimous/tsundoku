package io.github.dimous.tsundoku.presentation.view.control;

import com.google.inject.Inject;
import io.github.dimous.tsundoku.presentation.controller.IdenticalsController;
import io.github.dimous.tsundoku.presentation.view.Util;
import io.github.dimous.tsundoku.presentation.view.dto.IdenticalsTreeNodeDTO;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;

import java.util.ResourceBundle;

public final class IdenticalsTreeCell extends TreeCell<IdenticalsTreeNodeDTO> {
    private final Util
        __util;

    private final MenuItem
        __menu_item_delete;

    @Inject
    public IdenticalsTreeCell(final Util __util, final ResourceBundle __resource_bundle) {
        final ContextMenu
            __context_menu = new ContextMenu();
        ///
        ///
        this.__util = __util;
        this.__menu_item_delete = new MenuItem(
            __resource_bundle.getString("ui.button.delete")
        );

        __context_menu.getItems().addAll(
            this.__menu_item_delete
        );

        this.setContextMenu(__context_menu);
    }
    //---

    @Override
    protected void updateItem(final IdenticalsTreeNodeDTO __identicals_tree_node_d_t_o, final boolean __boolean_is_empty) {
        super.updateItem(__identicals_tree_node_d_t_o, __boolean_is_empty);

        if (__boolean_is_empty || null == __identicals_tree_node_d_t_o) {
            this.setText(null);
            this.setGraphic(null);
        } else {
            if (null == __identicals_tree_node_d_t_o.book()) {
                this.setText(__identicals_tree_node_d_t_o.hash());
            } else {
                this.__menu_item_delete.setOnAction(
                    __event -> {
                        final IdenticalsController
                            __identicals_controller = (IdenticalsController) this.getTreeView().getUserData();
                        ///
                        ///
                        __identicals_controller.onDelete(__identicals_tree_node_d_t_o.book());
                    }
                );

                this.setText(String.format("%s (%d Kb)", __identicals_tree_node_d_t_o.book().getPath(), __identicals_tree_node_d_t_o.book().getSize()));

                // извлекать в нитке
                // this.__util.retrieveCover(__identicals_tree_node_d_t_o.book()).ifPresent(this::setGraphic);
            }
        }
    }
}
