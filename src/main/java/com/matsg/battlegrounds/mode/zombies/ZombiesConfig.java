package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.storage.BattleCacheYaml;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombiesConfig extends BattleCacheYaml {

    private boolean runningMobs;
    private double mobAttackDamage;
    private double mobFollowRange;
    private double mobMaxHealth;
    private double powerUpChance;
    private double spawnRate;
    private int countdownLength;
    private int defaultMagazines;
    private int defaultPoints;
    private int mysteryBoxPrice;
    private int powerUpDuration;
    private int runningMobsRound;
    private int startingRound;
    private int waveDelay;
    private int variation;
    private List<String> powerUpTypes;

    public ZombiesConfig(Battlegrounds plugin, String path) throws IOException {
        super(plugin, path, "zombies.yml");
        this.countdownLength = getInt("zombies-countdown-length");
        this.defaultMagazines = getInt("zombies-default-loadout.magazines");
        this.defaultPoints = getInt("zombies-default-loadout.points");
        this.mobAttackDamage = getDouble("zombies-wave-mob-attack-damage");
        this.mobFollowRange = getDouble("zombies-wave-mob-follow-range");
        this.mobMaxHealth = getDouble("zombies-wave-mob-max-health");
        this.mysteryBoxPrice = getInt("zombies-mystery-box-price");
        this.powerUpChance = getDouble("zombies-power-up-chance");
        this.powerUpDuration = getInt("zombies-power-up-duration");
        this.powerUpTypes = getStringList("zombies-power-up-types");
        this.runningMobs = getBoolean("zombies-wave-running-mobs");
        this.runningMobsRound = getInt("zombies-wave-running-mobs-round");
        this.spawnRate = getDouble("zombies-wave-spawn-rate");
        this.startingRound = getInt("zombies-starting-round");
        this.waveDelay = getInt("zombies-wave-delay");
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

    public double getMobAttackDamage() {
        return mobAttackDamage;
    }

    public double getMobFollowRange() {
        return mobFollowRange;
    }

    public double getMobMaxHealth() {
        return mobMaxHealth;
    }

    public int getMysteryBoxPrice() {
        return mysteryBoxPrice;
    }

    public double getPowerUpChance() {
        return powerUpChance;
    }

    public int getPowerUpDuration() {
        return powerUpDuration;
    }

    public List<String> getPowerUpTypes() {
        return powerUpTypes;
    }

    public int getRunningMobsRound() {
        return runningMobsRound;
    }

    public double getSpawnRate() {
        return spawnRate;
    }

    public int getStartingRound() {
        return startingRound;
    }

    public int getVariation() {
        return variation;
    }

    public int getWaveDelay() {
        return waveDelay;
    }

    public boolean hasRunningMobs() {
        return runningMobs;
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

    public Map<String, String> getScoreboardLayout() {
        Map<String, String> map = new HashMap<>();
        for (String string : getConfigurationSection("zombies-scoreboard.layout").getKeys(false)) {
            map.put(string, getString("zombies-scoreboard.layout." + string));
        }
        return map;
    }
}
