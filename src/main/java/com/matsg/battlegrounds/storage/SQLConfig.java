package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.storage.AbstractYaml;

import java.io.IOException;

public class SQLConfig extends AbstractYaml {

    public SQLConfig(Battlegrounds plugin) throws IOException {
        super(plugin, "sql_config.yml", true);
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
