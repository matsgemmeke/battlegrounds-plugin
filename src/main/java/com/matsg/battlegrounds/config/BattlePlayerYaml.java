package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.AbstractYaml;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.item.BattleLoadout;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.*;

public class BattlePlayerYaml extends AbstractYaml implements StoredPlayer {

    private int deaths, exp, headshots, kills;
    private String name;
    private UUID uuid;

    public BattlePlayerYaml(Battlegrounds plugin, UUID uuid) throws IOException {
        super(plugin, plugin.getDataFolder().getPath() + "/players", uuid.toString() + ".yml", false);
        this.uuid = uuid;
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

    public UUID getUUID() {
        return uuid;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        setAttribute("Stats.All.Deaths", deaths);
    }

    public void setExp(int exp) {
        this.exp = exp;
        setAttribute("Stats.Exp", exp);
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
        setAttribute("Stats.All.Headshots", headshots);
    }

    public void setKills(int kills) {
        this.kills = kills;
        setAttribute("Stats.All.Kills", kills);
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

    private String convertAttachmentsToString(Gun gun) {
        StringBuilder builder = new StringBuilder();
        for (Attachment attachment : gun.getAttachments()) {
            builder.append(attachment.getId());
            if (gun.getAttachments().size() > gun.getAttachments().indexOf(attachment) + 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public void createDefaultAttributes() {
        set("Stats.Exp", 0);
        set("Stats.All.Deaths", 0);
        set("Stats.All.Headshots", 0);
        set("Stats.All.Kills", 0);
        save();
    }

    public int getAttribute(String attribute) {
        for (String string : mapValues().keySet()) {
            if (string.equalsIgnoreCase(attribute)) {
                return mapValues().get(string);
            }
        }
        return 0;
    }

    public Loadout getLoadout(int loadoutNumber) {
        ConfigurationSection section = getConfigurationSection("Loadout." + loadoutNumber);

        Loadout loadout = new BattleLoadout(
                loadoutNumber,
                section.getString("Name"),
                plugin.getFirearmFactory().make(section.getString("Primary.Id")),
                plugin.getFirearmFactory().make(section.getString("Secondary.Id")),
                plugin.getEquipmentFactory().make(section.getString("Equipment")),
                plugin.getMeleeWeaponFactory().make(section.getString("MeleeWeapon"))
        );

        if (loadout.getPrimary() instanceof Gun) {
            String attachmentString = section.getString("Primary.Attachments");

            if (attachmentString != null && !attachmentString.isEmpty()) {
                for (String attachmentId : section.getString("Primary.Attachments").split(", ")) {
                    Attachment attachment = plugin.getAttachmentFactory().make(attachmentId);
                    if (attachment != null) {
                        ((Gun) loadout.getPrimary()).getAttachments().add(attachment);
                    }
                }
            }
        }
        if (loadout.getSecondary() instanceof Gun) {
            String attachmentString = section.getString("Secondary.Attachments");

            if (attachmentString != null && !attachmentString.isEmpty()) {
                for (String attachmentId : section.getString("Secondary.Attachments").split(", ")) {
                    Attachment attachment = plugin.getAttachmentFactory().make(attachmentId);
                    if (attachment != null) {
                        ((Gun) loadout.getSecondary()).getAttachments().add(attachment);
                    }
                }
            }
        }
        return loadout;
    }

    public List<Loadout> getLoadouts() {
        List<Loadout> list = new ArrayList<>();
        for (int i = 1; i <= 5; i ++) {
            list.add(getLoadout(i));
        }
        return list;
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

    public void saveLoadout(int loadoutNumber, Loadout loadout) {
        set("Loadout." + loadoutNumber + ".Name", loadout.getName());
        set("Loadout." + loadoutNumber + ".Primary.Id", loadout.getPrimary() != null ? loadout.getPrimary().getId() : null);
        set("Loadout." + loadoutNumber + ".Secondary.Id", loadout.getSecondary() != null ? loadout.getSecondary().getId() : null);
        set("Loadout." + loadoutNumber + ".Equipment", loadout.getEquipment() != null ? loadout.getEquipment().getId() : null);
        set("Loadout." + loadoutNumber + ".MeleeWeapon", loadout.getMeleeWeapon() != null ? loadout.getMeleeWeapon().getId() : null);

        if (loadout.getPrimary() != null && loadout.getPrimary() instanceof Gun) {
            set("Loadout." + loadoutNumber + ".Primary.Attachments", convertAttachmentsToString((Gun) loadout.getPrimary()));
        }
        if (loadout.getSecondary() != null && loadout.getSecondary() instanceof Gun) {
            set("Loadout." + loadoutNumber + ".Secondary.Attachments", convertAttachmentsToString((Gun) loadout.getSecondary()));
        }

        save();
    }

    private void setAttribute(String attribute, int value) {
        set(attribute, value);
        save();
    }

    public void updateName(String name) {
        set("Name", name);
        save();
    }
}
