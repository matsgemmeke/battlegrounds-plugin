package com.matsg.battlegrounds.storage.local;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.api.storage.StatisticContext;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class PlayerYaml extends AbstractYaml implements StoredPlayer {

    private int deaths, exp, headshots, kills;
    private String name;
    private UUID uuid;

    public PlayerYaml(Battlegrounds plugin, UUID uuid) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/players", uuid.toString() + ".yml", false);
        this.uuid = uuid;
        this.deaths = getInt("Stats.Deaths");
        this.exp = getInt("Exp");
        this.headshots = getInt("Stats.Headshots");
        this.kills = getInt("Stats.Kills");
        this.name = getString("Name");
    }

    public int getDeaths() {
        return deaths;
    }

    public int getExp() {
        return exp;
    }

    public int getHeadshots() {
        return headshots;
    }

    public int getKills() {
        return kills;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public StoredPlayer addStatisticAttributes(StatisticContext context) {
        OfflineGamePlayer player = context.getPlayer();
        set("Exp", exp + player.getExp());
        set("Stats.Deaths", deaths + player.getDeaths());
        set("Stats.Headhots", headshots + player.getHeadshots());
        set("Stats.Kills", kills + player.getKills());
        save();
        return this;
    }

    public int compareTo(StoredPlayer o) {
        if (exp != o.getExp()) {
            return o.getExp() - exp;
        }
        if (kills != o.getKills()) {
            return o.getKills() - kills;
        }
        return name.compareTo(o.getName());
    }

    public void createDefaultAttributes(Player player) {
        set("Exp", 0);
        set("Name", player.getName());
        set("Stats.Deaths", 0);
        set("Stats.Headshots", 0);
        set("Stats.Kills", 0);
        save();
    }

    public Map<String, String> getLoadoutSetup(int loadoutNr) {
        ConfigurationSection section = getConfigurationSection("Loadout." + loadoutNr);
        Map<String, String> loadoutSetup = new HashMap<>();

        loadoutSetup.put("loadout_nr", String.valueOf(loadoutNr));
        loadoutSetup.put("loadout_name", section.getString("Name"));
        loadoutSetup.put("primary", section.getString("Primary"));
        loadoutSetup.put("primary_attachments", section.getString("Primary.Attachments"));
        loadoutSetup.put("secondary", section.getString("Secondary"));
        loadoutSetup.put("secondary_attachments", section.getString("Secondary.Attachments"));
        loadoutSetup.put("equipment", section.getString("Equipment"));
        loadoutSetup.put("melee_weapon", section.getString("MeleeWeapon"));

        return loadoutSetup;
    }

    public List<Map<String, String>> getLoadoutSetups() {
        List<Map<String, String>> loadoutSetups = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            loadoutSetups.add(getLoadoutSetup(i));
        }
        return loadoutSetups;
    }

    public int getStatisticAttribute(StatisticContext context) {
        return 0;
    }

    public boolean isOnline() {
        return false;
    }

    public void saveLoadout(int loadoutNumber, Map<String, String> loadoutSetup) {
        set("Loadout." + loadoutNumber + ".Name", loadoutSetup.get("loadout_name"));
        set("Loadout." + loadoutNumber + ".Primary.Id", loadoutSetup.get("primary"));
        set("Loadout." + loadoutNumber + ".Secondary.Id", loadoutSetup.get("secondary"));
        set("Loadout." + loadoutNumber + ".Equipment", loadoutSetup.get("equipment"));
        set("Loadout." + loadoutNumber + ".MeleeWeapon", loadoutSetup.get("melee_weapon"));

        String primaryAttachments = loadoutSetup.get("primary_attachments");
        String secondaryAttachments = loadoutSetup.get("secondary_attachments");

        if (primaryAttachments != null) {
            set("Loadout." + loadoutNumber + ".Primary.Attachments", primaryAttachments);
        }
        if (secondaryAttachments != null) {
            set("Loadout." + loadoutNumber + ".Secondary.Attachments", secondaryAttachments);
        }

        save();
    }

    public void updateName(String name) {
        if (this.name.equals(name)) {
            return;
        }
        this.name = name;
        set("Name", name);
        save();
    }
}
