package com.matsg.battlegrounds.game.mode.tdm;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.storage.BattleCacheYaml;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.*;

public class TDMConfig extends BattleCacheYaml {

    private boolean scoreboardEnabled;
    private double minimumSpawnDistance;
    private int killsToWin, lives, timeLimit;

    public TDMConfig(Battlegrounds plugin, String path) throws IOException {
        super(plugin, path, "tdm.yml");
        this.killsToWin = getInt("kills-to-win");
        this.lives = getInt("lives");
        this.minimumSpawnDistance = getDouble("minimum-distance-spawn");
        this.scoreboardEnabled = getBoolean("scoreboard.enabled");
        this.timeLimit = getInt("time-limit");
    }

    public int getKillsToWin() {
        return killsToWin;
    }

    public int getLives() {
        return lives;
    }

    public double getMinimumSpawnDistance() {
        return minimumSpawnDistance;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public boolean isScoreboardEnabled() {
        return scoreboardEnabled;
    }

    public Map<String, String> getScoreboardLayout() {
        Map<String, String> map = new HashMap<>();
        for (String string : getConfigurationSection("scoreboard.layout").getKeys(false)) {
            map.put(string, getString("scoreboard.layout." + string));
        }
        return map;
    }

    public List<World> getScoreboardWorlds() {
        List<String> list = Arrays.asList(getString("scoreboard.worlds").split(","));
        List<World> worlds = new ArrayList<>();

        if (list.contains("*")) {
            worlds.addAll(plugin.getServer().getWorlds());
        } else {
            for (String world : list) {
                worlds.add(plugin.getServer().getWorld(world));
            }
        }

        return worlds;
    }

    public List<Team> getTeams() {
        List<Team> list = new ArrayList<>();

        for (String teamId : config.getConfigurationSection("teams").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("teams." + teamId);
            String[] array = section.getString("armor-color").split(",");
            Color color = Color.fromRGB(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));

            list.add(new BattleTeam(Integer.parseInt(teamId), section.getString("name"), color, ChatColor.getByChar(section.getString("chatcolor").charAt(0))));
        }

        return list;
    }
}
