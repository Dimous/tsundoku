package io.github.dimous.tsundoku.presentation.view.dto;

import io.github.dimous.tsundoku.domain.entity.BookEntity;

public record IdenticalsTreeNodeDTO(String hash, BookEntity book) {}
