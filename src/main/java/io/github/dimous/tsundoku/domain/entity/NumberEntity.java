package io.github.dimous.tsundoku.domain.entity;

import jakarta.persistence.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"value", "book_id"}
        )
    }
)
public final class NumberEntity {
    private String
        __string_value;

    private Set<BookEntity>
        __set_books;

    public NumberEntity() {
    }

    public NumberEntity(final String __string_value) {
        this.setValue(__string_value);
    }

    @Id
    @KeywordField
    @Column(
        name = "id"
    ) // value -- ключевое слово, которое не экранируется
    public String getValue() {
        return this.__string_value;
    }

    public void setValue(final String __string_value) {
        this.__string_value = __string_value;
    }
    //---

    @Column(
        name = "books"
    )
    @ManyToMany(
        mappedBy = "numbers",
        fetch = FetchType.EAGER // или @Transactional
    )
    public Set<BookEntity> getBooks() {
        return this.__set_books;
    }

    public void setBooks(final Set<BookEntity> __set_books) {
        this.__set_books = __set_books;
    }
    //---

    @Override
    public int hashCode() {
        return Objects.hash(this.getValue());
    }
    //---

    @Override
    public boolean equals(final Object __object_target) {
        return null != __object_target && (this == __object_target || __object_target instanceof NumberEntity __number_entity_target && Objects.equals(this.getValue(), __number_entity_target.getValue()));
    }
}
