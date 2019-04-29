package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.storage.BattleCacheYaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZombiesConfig extends BattleCacheYaml {

    public ZombiesConfig(Battlegrounds plugin, String path) throws IOException {
        super(plugin, path, "zombies.yml");
    }

    public String[] getPerkSignLayout() {
        List<String> list = new ArrayList<>();
        for (String string : getConfigurationSection("perk-sign-layout").getKeys(false)) {
            list.add(getString("perk-sign-layout." + string));
        }
        return list.toArray(new String[list.size()]);
    }
}
