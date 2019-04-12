package com.matsg.battlegrounds.storage.sql;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.storage.StatisticContext;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class SQLPlayerRecord implements StoredPlayer {

    private Battlegrounds plugin;
    private Connection connection;
    private int deaths, exp, headshots, kills;
    private LoadoutFactory loadoutFactory;
    private String name;
    private UUID uuid;

    public SQLPlayerRecord(Battlegrounds plugin, Connection connection, UUID uuid) {
        this.plugin = plugin;
        this.connection = connection;
        this.uuid = uuid;
        this.loadoutFactory = new LoadoutFactory();

        try {
            fetchInfo();
            fetchStatistics();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHeadshots() {
        return headshots;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isOnline() {
        return false;
    }

    public StoredPlayer addStatisticAttributes(StatisticContext context) {
        Game game = context.getGame();
        OfflineGamePlayer player = context.getPlayer();

        this.deaths += player.getDeaths();
        this.exp += player.getExp();
        this.headshots += player.getHeadshots();
        this.kills += player.getKills();

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `battlegrounds_statistic`(`player_uuid`, `game_id`, `gamemode`, `kills`, `deaths`, `headshots`) VALUES(?, ?, ?, ?, ?, ?)");
            ps.setString(1, player.getUUID().toString());
            ps.setInt(2, game.getId());
            ps.setString(3, game.getGameMode().getShortName());
            ps.setInt(4, player.getKills());
            ps.setInt(5, player.getDeaths());
            ps.setInt(6, player.getHeadshots());
            ps.execute();
            ps.close();

            ps = connection.prepareStatement("UPDATE `battlegrounds_player` SET exp = exp + ? WHERE player_uuid = ?");
            ps.setInt(1, player.getExp());
            ps.setString(2, player.getUUID().toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        this.uuid = player.getUniqueId();
        this.name = player.getName();

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `battlegrounds_player` VALUES(?, ?, ?);");
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            ps.setInt(3, 0);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Loadout getLoadout(int loadoutNr) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM `battlegrounds_loadout` " +
                    "WHERE player_uuid = ? AND loadout_nr = ?"
            );
            ps.setString(1, uuid.toString());
            ps.setInt(2, loadoutNr);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                rs.close();
                ps.close();
                return null;
            }

            List<Attachment> primaryAttachments = new ArrayList<>();
            List<Attachment> secondaryAttachments = new ArrayList<>();
            String attachmentString;

            if ((attachmentString = rs.getString("primary_attachments")) != null && !attachmentString.isEmpty()) {
                for (String attachmentId : attachmentString.split(", ")) {
                    primaryAttachments.add(plugin.getAttachmentFactory().make(attachmentId));
                }
            }

            if ((attachmentString = rs.getString("secondary_attachments")) != null && !attachmentString.isEmpty()) {
                for (String attachmentId : attachmentString.split(", ")) {
                    secondaryAttachments.add(plugin.getAttachmentFactory().make(attachmentId));
                }
            }

            Loadout loadout = loadoutFactory.make(
                    loadoutNr,
                    rs.getString("loadout_name"),
                    plugin.getFirearmFactory().make(rs.getString("primary_firearm")),
                    plugin.getFirearmFactory().make(rs.getString("secondary_firearm")),
                    plugin.getEquipmentFactory().make(rs.getString("equipment")),
                    plugin.getMeleeWeaponFactory().make(rs.getString("melee_weapon")),
                    primaryAttachments.toArray(new Attachment[primaryAttachments.size()]),
                    secondaryAttachments.toArray(new Attachment[secondaryAttachments.size()])
            );

            rs.close();
            ps.close();

            return loadout;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<Loadout> getLoadouts() {
        List<Loadout> list = new ArrayList<>();
        for (int i = 1; i <= 5; i ++) {
            list.add(getLoadout(i));
        }
        return list;
    }

    public int getStatisticAttribute(StatisticContext context) {
        int result = 0;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * " +
                    "FROM `battlegrounds_statistic` " +
                    "WHERE game_id = ? AND gamemode = ? " +
                    "GROUP BY player_uuid " +
                    "HAVING player_uuid = ?"
            );
            ps.setString(3, uuid.toString());

            if (context != null && context.getGame() != null) {
                ps.setInt(1, context.getGame().getId());
                ps.setString(2, context.getGame().getGameMode().getShortName());
            } else {
                ps.setNull(1, Types.INTEGER);
                ps.setNull(2, Types.VARCHAR);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getInt(context.getStatisticName());
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveLoadout(int loadoutNr, Loadout loadout) {
        PreparedStatement ps;

        try {
            ps = connection.prepareStatement("SELECT * " +
                    "FROM `battlegrounds_loadout` " +
                    "WHERE player_uuid = ? AND loadout_nr = ?;"
            );
            ps.setString(1, uuid.toString());
            ps.setInt(2, loadoutNr);

            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();

            rs.close();
            ps.close();

            if (!exists) {
                ps = connection.prepareStatement("INSERT INTO `battlegrounds_loadout` (player_uuid, loadout_nr, loadout_name, primary_firearm, primary_attachments, secondary_firearm, secondary_attachments, equipment, melee_weapon)" +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                ps.setString(1, uuid.toString());
                ps.setInt(2, loadoutNr);
                ps.setString(3, loadout.getName());
                ps.setString(4, loadout.getPrimary() != null ? loadout.getPrimary().getId() : null);
                ps.setString(5, loadout.getPrimary() instanceof Gun ? convertAttachmentsToString((Gun) loadout.getPrimary()) : null);
                ps.setString(6, loadout.getSecondary() != null ? loadout.getSecondary().getId() : null);
                ps.setString(7, loadout.getSecondary() instanceof Gun ? convertAttachmentsToString((Gun) loadout.getSecondary()) : null);
                ps.setString(8, loadout.getEquipment() != null ? loadout.getEquipment().getId() : null);
                ps.setString(9, loadout.getMeleeWeapon() != null ? loadout.getMeleeWeapon().getId() : null);
                ps.execute();
            } else {
                ps = connection.prepareStatement("UPDATE `battlegrounds_loadout` " +
                                "SET loadout_name = ?, primary_firearm = ?, primary_attachments = ?, secondary_firearm = ?, secondary_attachments = ?, equipment = ?, melee_weapon = ? " +
                                "WHERE player_uuid = ? AND loadout_nr = ?"
                );
                ps.setString(1, loadout.getName());
                ps.setString(2, loadout.getPrimary() != null ? loadout.getPrimary().getId() : null);
                ps.setString(3, loadout.getPrimary() instanceof Gun ? convertAttachmentsToString((Gun) loadout.getPrimary()) : null);
                ps.setString(4, loadout.getSecondary() != null ? loadout.getSecondary().getId() : null);
                ps.setString(5, loadout.getSecondary() instanceof Gun ? convertAttachmentsToString((Gun) loadout.getSecondary()) : null);
                ps.setString(6, loadout.getEquipment() != null ? loadout.getEquipment().getId() : null);
                ps.setString(7, loadout.getMeleeWeapon() != null ? loadout.getMeleeWeapon().getId() : null);
                ps.setString(8, uuid.toString());
                ps.setInt(9, loadoutNr);
                ps.executeUpdate();
            }

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateName(String name) {
        this.name = name;

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE `battlegrounds_player`" +
                    "SET `player_name` = ?" +
                    "WHERE `player_uuid` = ?"
            );
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private void fetchInfo() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT *" +
                "FROM `battlegrounds_player`" +
                "WHERE player_uuid = ?"
        );
        ps.setString(1, uuid.toString());

        ResultSet rs = ps.executeQuery();

        // Check if the player has an exisiting player record.
        if (rs.next()) {
            this.exp = rs.getInt("exp");
            this.name = rs.getString("player_name");
        }

        rs.close();
        ps.close();
    }

    private void fetchStatistics() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT `battlegrounds_player`.player_uuid, `battlegrounds_player`.exp AS exp, SUM(`battlegrounds_statistic`.kills) AS kills, SUM(`battlegrounds_statistic`.deaths) AS deaths, SUM(`battlegrounds_statistic`.headshots) AS headshots " +
                "FROM `battlegrounds_player` " +
                "JOIN `battlegrounds_statistic` " +
                "ON `battlegrounds_player`.player_uuid = `battlegrounds_statistic`.player_uuid " +
                "WHERE `battlegrounds_player`.player_uuid = ? " +
                "GROUP BY `battlegrounds_player`.player_uuid"
        );
        ps.setString(1, uuid.toString());

        ResultSet rs = ps.executeQuery();

        // Check if the player has existing statistic records.
        if (rs.next()) {
            this.deaths = rs.getInt("deaths");
            this.headshots = rs.getInt("headshots");
            this.kills = rs.getInt("kills");
        }

        rs.close();
        ps.close();
    }
}
