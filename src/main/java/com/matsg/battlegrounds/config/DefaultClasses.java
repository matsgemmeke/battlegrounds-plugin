package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;

import java.io.IOException;

public class DefaultClasses extends AbstractYaml {

    public DefaultClasses(Battlegrounds plugin) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/class", "default_classes.yml", true);
    }


}