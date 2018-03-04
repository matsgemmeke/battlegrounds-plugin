package com.matsg.battlegrounds.api.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.config.AbstractYaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BattlegroundsConfig extends AbstractYaml {

    public final boolean arenaProtection = getBoolean("game-arena-protection");
    public final boolean broadcastChat = getBoolean("game-broadcast-chat");
    public final int explosiveCooldown = getInt("game-explosive-cooldown");
    public final double firearmAccuracy = getDouble("game-firearm-accuracy");
    public final double launcherVelocity = getDouble("game-launcher-velocity");
    public final List<String> pierceableBlocks = getStringList("game-pierceable-blocks");

    public BattlegroundsConfig(Battlegrounds plugin) throws IOException {
        super(plugin, "config.yml", false);
        if (!getString("version").equals(plugin.getDescription().getVersion())) { //Auto update the config.yml when the plugin has updated
            removeFile();
            createFile(plugin.getDataFolder().getPath(), "config.yml");
        }
    }

    public String[] getGameSignLayout() {
        List<String> list = new ArrayList<>();
        for (String string : getConfigurationSection("game-sign-layout").getKeys(false)) {
            list.add(getString("game-sign-layout." + string));
        }
        return list.toArray(new String[list.size()]);
    }

    public String getWeaponDisplayName(String weaponType) {
        return getString("game-display-name." + weaponType);
    }

    public String getWeaponMaterial(String weaponType) {
        return getString("game-material." + weaponType);
    }
}