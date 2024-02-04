package io.github.dimous.tsundoku.domain.vo;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;

public final class ConfigVO {
    private Set<String>
        __set_extensions;

    private String
        __string_url,
        __string_user,
        __string_driver,
        __string_dialect,
        __string_password,
        __string_base_path;

    public ConfigVO(final String __string_driver, final String __string_dialect, final String __string_url, final String __string_user, final String __string_password, final String __string_base_path, final String __string_extensions) {
        this(__string_driver, __string_dialect, __string_url, __string_user, __string_password, __string_base_path, Sets.newHashSet(Splitter.on(",").trimResults().split(__string_extensions)));
    }

    public ConfigVO(final String __string_driver, final String __string_dialect, final String __string_url, final String __string_user, final String __string_password, final String __string_base_path, final Set<String> __set_extensions) {
        this.setUrl(__string_url);
        this.setUser(__string_user);
        this.setDriver(__string_driver);
        this.setDialect(__string_dialect);
        this.setPassword(__string_password);
        this.setBasePath(__string_base_path);
        this.setExtensions(__set_extensions);
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

    public String getDriver() {
        return __string_driver;
    }

    public void setDriver(final String __string_driver) {
        this.__string_driver = __string_driver;
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

    public Set<String> getExtensions() {
        return this.__set_extensions;
    }

    public void setExtensions(final Set<String> __set_extensions) {
        this.__set_extensions = __set_extensions;
    }
    //---

    @Override
    public int hashCode() {
        return Objects.hash(this.getUrl(), this.getUser(), this.getDriver(), this.getDialect(), this.getPassword(), this.getBasePath(), this.getExtensions());
    }
    //---

    @Override
    public boolean equals(final Object __object_target) {
        if (null == __object_target) {
            return false;
        }

        if (this == __object_target) {
            return true;
        }

        if (__object_target instanceof ConfigVO __config_v_o_target) {
            return Objects.equals(this.getUrl(), __config_v_o_target.getUrl()) && Objects.equals(this.getUser(), __config_v_o_target.getUser()) && Objects.equals(this.getDriver(), __config_v_o_target.getDriver()) && Objects.equals(this.getDialect(), __config_v_o_target.getDialect()) && Objects.equals(this.getPassword(), __config_v_o_target.getPassword()) && Objects.equals(this.getBasePath(), __config_v_o_target.getBasePath()) && Objects.equals(this.getExtensions(), __config_v_o_target.getExtensions());
        }

        return false;
    }
}
