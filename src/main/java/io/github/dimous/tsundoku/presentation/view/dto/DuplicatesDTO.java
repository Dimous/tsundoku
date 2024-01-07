package io.github.dimous.tsundoku.presentation.view.dto;

import javafx.scene.control.TreeItem;

public record DuplicatesDTO(TreeItem<DuplicatesTreeNodeDTO> root) {}
