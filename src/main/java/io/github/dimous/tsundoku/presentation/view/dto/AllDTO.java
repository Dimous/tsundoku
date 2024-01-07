package io.github.dimous.tsundoku.presentation.view.dto;

import javafx.scene.control.TreeItem;

public record AllDTO(TreeItem<AllTreeNodeDTO> root, int total) {}
