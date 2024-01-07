package io.github.dimous.tsundoku.domain.entity;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;

public final class ConfigEntity {
    private Set<String>
        __set_extensions;
    private String
        __string_url,
        __string_user,
        __string_dialect,
        __string_password,
        __string_base_path;

    public ConfigEntity(final String __string_dialect, final String __string_url, final String __string_user, final String __string_password, final String __string_base_path, final String __string_extensions) {
        this(__string_dialect, __string_url, __string_user, __string_password, __string_base_path, Sets.newHashSet(Splitter.on(",").trimResults().split(__string_extensions)));
    }

    public ConfigEntity(final String __string_dialect, final String __string_url, final String __string_user, final String __string_password, final String __string_base_path, final Set<String> __set_extensions) {
        this.setUrl(__string_url);
        this.setUser(__string_user);
        this.setDialect(__string_dialect);
        this.setPassword(__string_password);
        this.setBasePath(__string_base_path);
        this.setExtensions(__set_extensions);
    }

    public long getId() {
        return 1;
    }
    //---

    public Set<String> getExtensions() {
        return this.__set_extensions;
    }

    public void setExtensions(final Set<String> __set_extensions) {
        this.__set_extensions = __set_extensions;
    }
    //---

    public String getUrl() {
        return this.__string_url;
    }

    public void setUrl(final String __string_url) {
        this.__string_url = __string_url;
    }
    //---

    public String getUser() {
        return this.__string_user;
    }

    public void setUser(final String __string_user) {
        this.__string_user = __string_user;
    }
    //---

    public String getDialect() {
        return __string_dialect;
    }

    public void setDialect(final String __string_dialect) {
        this.__string_dialect = __string_dialect;
    }
    //---

    public String getPassword() {
        return this.__string_password;
    }

    public void setPassword(final String __string_password) {
        this.__string_password = __string_password;
    }
    //---

    public String getBasePath() {
        return this.__string_base_path;
    }

    public void setBasePath(final String __string_base_path) {
        this.__string_base_path = __string_base_path;
    }
    //---

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
    //---

    @Override
    public boolean equals(final Object __object_target) {
        return null != __object_target && (this == __object_target || __object_target instanceof ConfigEntity && Objects.equals(this.getId(), ((ConfigEntity) __object_target).getId()));
    }

}
