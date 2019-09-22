package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.storage.AbstractYaml;

import java.io.IOException;
import java.io.InputStream;

public class SQLConfig extends AbstractYaml {

    public SQLConfig(String filePath, InputStream resource) throws IOException {
        super("sql_config.yml", filePath, resource, true);
    }

    public String getAddress() {
        return getString("sql.ip");
    }

    public String getDatabase() {
        return getString("sql.database");
    }

    public String getPassword() {
        return getString("sql.password");
    }

    public String getUsername() {
        return getString("sql.username");
    }

    public boolean isEnabled() {
        return getBoolean("sql.enabled");
    }
}
