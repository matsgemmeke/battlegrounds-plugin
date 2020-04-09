package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.storage.BattleCacheYaml;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombiesConfig extends BattleCacheYaml {

    private boolean hellhoundEnabled;
    private boolean runningMobs;
    private double hellhoundChance;
    private double hellhoundHealth;
    private double mobAttackDamage;
    private double mobFollowRange;
    private double mobMaxHealth;
    private double powerUpChance;
    private double spawnRate;
    private int defaultEquipmentAmount;
    private int defaultPoints;
    private int defaultPrimaryMagazines;
    private int mysteryBoxPrice;
    private int powerUpDuration;
    private int runningMobsRound;
    private int startingRound;
    private int targetUpdateInterval;
    private int waveDelay;
    private int variation;
    private List<String> mysteryBoxWeapons;
    private List<String> powerUpEffects;

    public ZombiesConfig(String filePath, InputStream resource, Server server) throws IOException {
        super("zombies.yml", filePath, resource, server);
        this.defaultEquipmentAmount = getInt("zombies-default-loadout.equipment-amount");
        this.defaultPoints = getInt("zombies-default-loadout.points");
        this.defaultPrimaryMagazines = getInt("zombies-default-loadout.primary-magazines");
        this.hellhoundChance = getDouble("zombies-wave-hellhound-chance");
        this.hellhoundEnabled = getBoolean("zombies-wave-enable-hellhounds");
        this.hellhoundHealth = getDouble("zombies-wave-hellhound-health");
        this.mobAttackDamage = getDouble("zombies-wave-mob-attack-damage");
        this.mobFollowRange = getDouble("zombies-wave-mob-follow-range");
        this.mobMaxHealth = getDouble("zombies-wave-mob-max-health");
        this.mysteryBoxPrice = getInt("zombies-mystery-box-price");
        this.mysteryBoxWeapons = getStringList("zombies-mystery-box-weapons");
        this.powerUpChance = getDouble("zombies-power-up-chance");
        this.powerUpDuration = getInt("zombies-power-up-duration");
        this.powerUpEffects = getStringList("zombies-power-up-effects");
        this.runningMobs = getBoolean("zombies-wave-running-mobs");
        this.runningMobsRound = getInt("zombies-wave-running-mobs-round");
        this.spawnRate = getDouble("zombies-wave-spawn-rate");
        this.startingRound = getInt("zombies-starting-round");
        this.targetUpdateInterval = getInt("zombies-target-update-interval");
        this.waveDelay = getInt("zombies-wave-delay");
        this.variation = getInt("zombies-wave-variation");
    }

    public int getDefaultEquipmentAmount() {
        return defaultEquipmentAmount;
    }

    public int getDefaultPoints() {
        return defaultPoints;
    }

    public int getDefaultPrimaryMagazines() {
        return defaultPrimaryMagazines;
    }

    public double getHellhoundChance() {
        return hellhoundChance;
    }

    public double getHellhoundHealth() {
        return hellhoundHealth;
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

    public List<String> getMysteryBoxWeapons() {
        return mysteryBoxWeapons;
    }

    public double getPowerUpChance() {
        return powerUpChance;
    }

    public int getPowerUpDuration() {
        return powerUpDuration;
    }

    public List<String> getPowerUpEffects() {
        return powerUpEffects;
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

    public int getTargetUpdateInterval() {
        return targetUpdateInterval;
    }

    public int getVariation() {
        return variation;
    }

    public int getWaveDelay() {
        return waveDelay;
    }

    public boolean hasHellhoundEnabled() {
        return hellhoundEnabled;
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

    public double getMobAmount(String entityType) {
        return getDouble("zombies-wave-mob-amount." + entityType);
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
