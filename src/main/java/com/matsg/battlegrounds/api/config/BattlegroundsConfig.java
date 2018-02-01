package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.config.AbstractYaml;

import java.io.IOException;

public final class BattlegroundsConfig extends AbstractYaml {

    public BattlegroundsConfig(Battlegrounds plugin) throws IOException {
        super(plugin, "config.yml", false);
        if (!getString("version").equals(plugin.getDescription().getVersion())) { //Auto update the config.yml when the plugin has updated
            removeFile();
            createFile(plugin.getDataFolder().getPath(), "config.yml");
        }
    }
}