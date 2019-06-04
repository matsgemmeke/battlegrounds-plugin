package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.storage.BattleCacheYaml;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombiesConfig extends BattleCacheYaml {

    private int countdownLength;
    private int defaultMagazines;
    private int defaultPoints;
    private int variation;

    public ZombiesConfig(Battlegrounds plugin, String path) throws IOException {
        super(plugin, path, "zombies.yml");
        this.countdownLength = getInt("zombies-countdown-length");
        this.defaultMagazines = getInt("zombies-default-loadout.magazines");
        this.defaultPoints = getInt("zombies-default-loadout.points");
        this.variation = getInt("zombies-wave-variation");
    }

    public int getCountdownLength() {
        return countdownLength;
    }

    public int getDefaultMagazines() {
        return defaultMagazines;
    }

    public int getDefaultPoints() {
        return defaultPoints;
    }

    public int getVariation() {
        return variation;
    }

    public Map<String, String> getDefaultLoadout() {
        ConfigurationSection section = getConfigurationSection("zombies-default-loadout");
        Map<String, String> loadout = new HashMap<>();

        loadout.put("primary", section.getString("primary"));
        loadout.put("secondary", section.getString("secondary"));
        loadout.put("equipment", section.getString("equipment"));
        loadout.put("melee-weapon", section.getString("melee-weapon"));

        return loadout;
    }

    public int getMaxMobs(int players) {
        return getInt("zombies-wave-max-mobs." + players);
    }

    public double getMobMultiplier(int players) {
        return getDouble("zombies-wave-mob-multiplier." + players);
    }

    public String[] getPerkSignLayout() {
        List<String> list = new ArrayList<>();
        for (String string : getConfigurationSection("zombies-perk-sign-layout").getKeys(false)) {
            list.add(getString("zombies-perk-sign-layout." + string));
        }
        return list.toArray(new String[list.size()]);
    }
}
