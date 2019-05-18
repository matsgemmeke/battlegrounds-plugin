package com.matsg.battlegrounds.storage.local;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.api.storage.StatisticContext;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class PlayerYaml extends AbstractYaml implements StoredPlayer {

    private int deaths, exp, headshots, kills;
    private LoadoutFactory loadoutFactory;
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
        this.loadoutFactory = new LoadoutFactory();
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

    public void createDefaultAttributes(Player player) {
        set("Exp", 0);
        set("Name", player.getName());
        set("Stats.Deaths", 0);
        set("Stats.Headshots", 0);
        set("Stats.Kills", 0);
        save();
    }

    public Loadout getLoadout(int loadoutNumber) {
        ConfigurationSection section = getConfigurationSection("Loadout." + loadoutNumber);
        List<Attachment> primaryAttachments = new ArrayList<>();
        List<Attachment> secondaryAttachments = new ArrayList<>();
        String attachmentString;

        if ((attachmentString = section.getString("Primary.Attachments")) != null && !attachmentString.isEmpty()) {
            for (String attachmentId : attachmentString.split(", ")) {
                Attachment attachment = plugin.getAttachmentFactory().make(attachmentId);
                if (attachment != null) {
                    primaryAttachments.add(attachment);
                }
            }
        }

        if ((attachmentString = section.getString("Secondary.Attachments")) != null && !attachmentString.isEmpty()) {
            for (String attachmentId : attachmentString.split(", ")) {
                Attachment attachment = plugin.getAttachmentFactory().make(attachmentId);
                if (attachment != null) {
                    secondaryAttachments.add(attachment);
                }
            }
        }

        return loadoutFactory.make(
                loadoutNumber,
                section.getString("Name"),
                plugin.getFirearmFactory().make(section.getString("Primary.Id")),
                plugin.getFirearmFactory().make(section.getString("Secondary.Id")),
                plugin.getEquipmentFactory().make(section.getString("Equipment")),
                plugin.getMeleeWeaponFactory().make(section.getString("MeleeWeapon")),
                primaryAttachments.toArray(new Attachment[primaryAttachments.size()]),
                secondaryAttachments.toArray(new Attachment[secondaryAttachments.size()]),
                null,
                null
        );
    }

    public List<Loadout> getLoadouts() {
        List<Loadout> list = new ArrayList<>();
        for (int i = 1; i <= 5; i ++) {
            list.add(getLoadout(i));
        }
        return list;
    }

    public int getStatisticAttribute(StatisticContext context) {
        return 0;
    }

    public boolean isOnline() {
        return false;
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

    public void updateName(String name) {
        if (this.name.equals(name)) {
            return;
        }
        this.name = name;
        set("Name", name);
        save();
    }
}
