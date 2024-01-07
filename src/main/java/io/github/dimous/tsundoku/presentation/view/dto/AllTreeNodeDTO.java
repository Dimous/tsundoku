package io.github.dimous.tsundoku.presentation.view.dto;

import io.github.dimous.tsundoku.domain.entity.BookEntity;

public final class AllTreeNodeDTO {
    private BookEntity
        __book_entity;
    private final String
        __string_path_chunk;

    public AllTreeNodeDTO(final String __string_path_chunk) {
        this.__string_path_chunk = __string_path_chunk;
    }
    //---

    public String getPathChunk() {
        return this.__string_path_chunk;
    }

    //---

    public BookEntity getBookEntity() {
        return this.__book_entity;
    }

    public void setBookEntity(final BookEntity __book_entity) {
        this.__book_entity = __book_entity;
    }
}
