package com.matsg.battlegrounds.api.storage;

import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BattlegroundsConfig extends AbstractYaml {

    public boolean arenaProtection = getBoolean("game-arena-protection");
    public boolean broadcastChat = getBoolean("game-broadcast-chat");
    public boolean displayBloodEffect = getBoolean("game-display-blood-effect");
    public ConfigurationSection lobbyScoreboard = getConfigurationSection("game-scoreboard.lobby");
    public int firearmDamageModifer = getInt("game-firearm-damage-modifier");
    public int loadoutCreationLevel = getInt("loadout-creation-level");
    public double firearmAccuracy = getDouble("game-firearm-accuracy");
    public List<String> allowedCommands = getStringList("game-allowed-commands");
    public List<String> joinableGamestates = getStringList("game-joinable-states");
    public List<String> pierceableBlocks = getStringList("game-pierceable-blocks");

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

    public String getGameSignState(String gameState) {
        return ChatColor.translateAlternateColorCodes('&', getString("game-sign-state." + gameState));
    }

    public Map<String, String> getLobbyScoreboardLayout() {
        Map<String, String> map = new HashMap<>();
        for (String string : getConfigurationSection("game-scoreboard.lobby.layout").getKeys(false)) {
            map.put(string, getString("game-scoreboard.lobby.layout." + string));
        }
        return map;
    }

    public String getWeaponDisplayName(String weaponType) {
        return getString("game-display-name." + weaponType);
    }
}
