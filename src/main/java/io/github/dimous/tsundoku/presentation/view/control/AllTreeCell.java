package io.github.dimous.tsundoku.presentation.view.control;

import io.github.dimous.tsundoku.domain.entity.BookEntity;
import io.github.dimous.tsundoku.presentation.view.dto.AllTreeNodeDTO;
import javafx.scene.control.TreeCell;

public final class AllTreeCell extends TreeCell<AllTreeNodeDTO> {
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
                this.setText(__book_entity.getName());
            }
        }
    }
}
