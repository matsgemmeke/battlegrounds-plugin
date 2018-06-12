package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.PlayerYaml;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.item.BattleLoadout;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.*;

public class BattlePlayerYaml extends AbstractYaml implements PlayerYaml {

    private UUID uuid;

    public BattlePlayerYaml(Battlegrounds plugin, UUID uuid) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/players", uuid.toString() + ".yml", false);
        this.uuid = uuid;
    }

    public void createDefaultAttributes() {
        set("Stats.Exp", 0);
        set("Stats.All.Deaths", 0);
        set("Stats.All.Headshots", 0);
        set("Stats.All.Kills", 0);
        save();
    }

    public Loadout getLoadout(int loadoutId) {
        ConfigurationSection section = getConfigurationSection("Loadout." + loadoutId);
        return new BattleLoadout(
                loadoutId,
                section.getString("Name"),
                plugin.getFireArmConfig().get(section.getString("Primary")),
                plugin.getFireArmConfig().get(section.getString("Secondary")),
                plugin.getEquipmentConfig().get(section.getString("Equipment")),
                plugin.getKnifeConfig().get(section.getString("Knife")));
    }

    public List<Loadout> getLoadouts() {
        List<Loadout> list = new ArrayList<>();
        for (int i = 1; i <= 5; i ++) {
            list.add(getLoadout(i));
        }
        return list;
    }

    public StoredPlayer getStoredPlayer() {
        return new LocalStoredPlayer(uuid, this);
    }

    public void saveLoadout(Loadout loadout) {
        set("Loadout." + loadout.getId() + ".Name", loadout.getName());
        set("Loadout." + loadout.getId() + ".Primary", loadout.getPrimary().getName());
        set("Loadout." + loadout.getId() + ".Secondary", loadout.getSecondary().getName());
        set("Loadout." + loadout.getId() + ".Equipment", loadout.getEquipment().getName());
        set("Loadout." + loadout.getId() + ".Knife", loadout.getKnife().getName());
        save();
    }

    public void updateName(String name) {
        set("Name", name);
        save();
    }

    private class LocalStoredPlayer implements StoredPlayer {

        private int deaths, exp, headshots, kills;
        private PlayerYaml playerYaml;
        private String name;
        private UUID uuid;

        private LocalStoredPlayer(UUID uuid, PlayerYaml playerYaml) {
            this.uuid = uuid;
            this.playerYaml = playerYaml;
            this.deaths = getInt("Stats.All.Deaths");
            this.exp = getInt("Stats.Exp");
            this.headshots = getInt("Stats.All.Headshots");
            this.kills = getInt("Stats.All.Kills");
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

        public PlayerYaml getPlayerYaml() {
            return playerYaml;
        }

        public UUID getUUID() {
            return uuid;
        }

        public void setDeaths(int deaths) {
            this.deaths = deaths;
            set("Stats.All.Deaths", deaths);
        }

        public void setExp(int exp) {
            this.exp = exp;
            set("Stats.Exp", exp);
        }

        public void setHeadshots(int headshots) {
            this.headshots = headshots;
            set("Stats.All.Headshots", headshots);
        }

        public void setKills(int kills) {
            this.kills = kills;
            set("Stats.All.Kills", kills);
        }

        public int getAttribute(String attribute) {
            for (String string : mapValues().keySet()) {
                if (string.equalsIgnoreCase(attribute)) {
                    return mapValues().get(string);
                }
            }
            return 0;
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

        public boolean isOnline() {
            return false;
        }

        private Map<String, Integer> mapValues() {
            Map<String, Integer> map = new HashMap<>();
            map.put("deaths", deaths);
            map.put("exp", exp);
            map.put("headshots", headshots);
            map.put("kills", kills);
            return map;
        }
    }
}