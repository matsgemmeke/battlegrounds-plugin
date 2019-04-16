package com.matsg.battlegrounds.storage.sql;

import com.matsg.battlegrounds.StartupFailedException;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.api.entity.OfflineGamePlayer;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.storage.DefaultLoadouts;
import com.matsg.battlegrounds.storage.SQLConfig;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.UUID;

public class SQLPlayerStorage implements PlayerStorage {

    private Battlegrounds plugin;
    private Connection connection;
    private DefaultLoadouts defaultLoadouts;
    private String address, database, password, username;

    public SQLPlayerStorage(Battlegrounds plugin, SQLConfig sqlConfig) throws IOException, StartupFailedException {
        this.plugin = plugin;
        this.defaultLoadouts = new DefaultLoadouts(plugin);
        this.address = sqlConfig.getAddress();
        this.database = sqlConfig.getDatabase();
        this.password = sqlConfig.getPassword();
        this.username = sqlConfig.getUsername();

        try {
            this.connection = openConnection();

            plugin.getLogger().info("Connection to database established.");

            TableCreator tableCreator = new TableCreator(connection);
            tableCreator.createTables();
        } catch (Exception e) {
            throw new StartupFailedException("Could not connect to the SQL database! Have you configured everything correctly?");
        } finally {
            closeConnection();
        }
    }

    public boolean contains(UUID uuid) {
        try {
            connection = openConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `battlegrounds_player` WHERE player_uuid = ?;");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();
            boolean contains = rs.next();

            rs.close();
            ps.close();

            return contains;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    public List<? extends OfflineGamePlayer> getList() {
        return null;
    }

    public StoredPlayer getStoredPlayer(UUID uuid) {
        try {
            return new SQLPlayerRecord(plugin, openConnection(), uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<? extends OfflineGamePlayer> getTopPlayers(int limit) {
        return null;
    }

    public StoredPlayer registerPlayer(Player player) {
        try {
            connection = openConnection();

            StoredPlayer storedPlayer = new SQLPlayerRecord(plugin, connection, player.getUniqueId());
            storedPlayer.createDefaultAttributes(player);

            for (int i = 1; i <= 5; i ++) {
                Loadout loadout = defaultLoadouts.getList().get(i - 1);
                storedPlayer.saveLoadout(loadout.getLoadoutNr(), loadout);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return getStoredPlayer(player.getUniqueId());
    }

    public StoredPlayer updatePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        StoredPlayer storedPlayer = getStoredPlayer(uuid);
        storedPlayer.updateName(player.getName());
        return storedPlayer;
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

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + address + ":3306/" + database + "?useSSL=false", username, password);
    }
}
