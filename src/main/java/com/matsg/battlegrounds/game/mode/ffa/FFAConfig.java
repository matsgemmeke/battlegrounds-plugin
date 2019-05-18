package com.matsg.battlegrounds.game.mode.ffa;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.storage.BattleCacheYaml;
import org.bukkit.Color;
import org.bukkit.World;

import java.io.IOException;
import java.util.*;

public class FFAConfig extends BattleCacheYaml {

    private boolean scoreboardEnabled;
    private double minimumSpawnDistance;
    private int countdownLength, killsToWin, lives, timeLimit;
    private List<String> endMessage;

    public FFAConfig(Battlegrounds plugin, String path) throws IOException {
        super(plugin, path, "ffa.yml");
        this.countdownLength = getInt("ffa-countdown-length");
        this.endMessage = getStringList("ffa-end-message");
        this.killsToWin = getInt("ffa-kills-to-win");
        this.lives = getInt("ffa-lives");
        this.minimumSpawnDistance = getDouble("ffa-minimum-distance-spawn");
        this.scoreboardEnabled = getBoolean("ffa-scoreboard.enabled");
        this.timeLimit = getInt("ffa-time-limit");
    }

    public int getCountdownLength() {
        return countdownLength;
    }

    public List<String> getEndMessage() {
        return endMessage;
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

    public Color getArmorColor() {
        String[] array = config.getString("ffa-armor-color").split(",");
        return Color.fromRGB(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
    }

    public Map<String, String> getScoreboardLayout() {
        Map<String, String> map = new HashMap<>();
        for (String string : getConfigurationSection("ffa-scoreboard.layout").getKeys(false)) {
            map.put(string, getString("ffa-scoreboard.layout." + string));
        }
        return map;
    }

    public List<World> getScoreboardWorlds() {
        List<String> list = Arrays.asList(getString("ffa-scoreboard.worlds").split(","));
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
}
