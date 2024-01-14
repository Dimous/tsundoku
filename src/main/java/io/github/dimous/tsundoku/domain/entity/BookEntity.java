package io.github.dimous.tsundoku.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.TermVector;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.type.NumericBooleanConverter;

import java.util.Objects;
import java.util.Set;

@Entity
@Indexed
@SoftDelete(
    columnName = "is_deleted",
    converter = NumericBooleanConverter.class
)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"path", "hash"}
        )
    }
)
public final class BookEntity {
    private long
        __long_id,
        __long_size;

    private String
        __string_path,
        __string_name,
        __string_hash,
        __string_content,
        __string_extension;

    private Set<NumberEntity>
        __set_numbers;

    // name должно хранить название книги из базы isbn, но до пост-обработки будет храниться имя файла
    // https://isbnsearch.org/isbn/..., https://isbndb.com/book/...
    // могут быть теги, которые определит mahout

    public BookEntity(final String __string_path, final String __string_name, final String __string_extension, final long __long_size, final String __string_hash, final Set<NumberEntity> __set_numbers, final String __string_content) {
        this.__long_size = __long_size;
        this.__string_path = __string_path;
        this.__string_name = __string_name;
        this.__string_hash = __string_hash;
        this.__set_numbers = __set_numbers;
        this.__string_content = __string_content;
        this.__string_extension = __string_extension;
    }
    //---

    public BookEntity() {}
    //---

    @Id
    @Column(
        name = "id",
        nullable = false,
        updatable = false
    )
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    public long getId() {
        return this.__long_id;
    }

    public void setId(final long __long_id) {
        this.__long_id = __long_id;
    }
    //---

    @Column(
        name = "path"
    )
    public String getPath() {
        return this.__string_path;
    }

    public void setPath(final String __string_path) {
        this.__string_path = __string_path;
    }
    //---

    @Column(
        name = "name"
    )
    public String getName() {
        return this.__string_name;
    }

    public void setName(final String __string_name) {
        this.__string_name = __string_name;
    }
    //---

    @Column(
        name = "extension"
    )
    public String getExtension() {
        return this.__string_extension;
    }

    public void setExtension(final String __string_extension) {
        this.__string_extension = __string_extension;
    }
    //---

    @Column(
        name = "size"
    )
    @GenericField(
        aggregable = Aggregable.YES
    )
    public long getSize() {
        return this.__long_size;
    }

    public void setSize(final long __long_size) {
        this.__long_size = __long_size;
    }
    //---

    @Column(
        name = "hash"
    )
    public String getHash() {
        return this.__string_hash;
    }

    public void setHash(final String __string_hash) {
        this.__string_hash = __string_hash;
    }
    //---

    @IndexedEmbedded
    @Column(
        name = "numbers"
    )
    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    public Set<NumberEntity> getNumbers() {
        return this.__set_numbers;
    }

    public void setNumbers(final Set<NumberEntity> __set_numbers) {
        this.__set_numbers = __set_numbers;
    }
    //---

    @Column(
        updatable = false,
        insertable = false
    )
    @FullTextField(
        termVector = TermVector.WITH_POSITIONS_OFFSETS_PAYLOADS
    )
    public String getContent() {
        return this.__string_content;
    }

    public void setContent(final String __string_content) {
        this.__string_content = __string_content;
    }
    //---

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
    //---

    @Override
    public boolean equals(final Object __object_target) {
        return null != __object_target && (this == __object_target || __object_target instanceof BookEntity __book_entity_target && Objects.equals(this.getId(), __book_entity_target.getId()));
    }
}
