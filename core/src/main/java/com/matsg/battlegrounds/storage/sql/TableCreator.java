package com.matsg.battlegrounds.storage.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class TableCreator {

    private Connection connection;

    public TableCreator(Connection connection) {
        this.connection = connection;
    }

    public void createTables() {
        try {
            DatabaseMetaData db = connection.getMetaData();
            ResultSet tables = db.getTables(null, null, "battlegrounds_player", null);

            if (tables.next()) {
                return;
            }

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE `battlegrounds_player` ("
                    + "player_uuid VARCHAR(36) NOT NULL PRIMARY KEY, "
                    + "player_name VARCHAR(16) NOT NULL, "
                    + "exp INT NOT NULL"
                    + ");"
            );
            statement.execute("CREATE TABLE `battlegrounds_loadout` ("
                    + "loadout_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, "
                    + "player_uuid VARCHAR(36) NOT NULL, "
                    + "loadout_nr INT NOT NULL, "
                    + "loadout_name VARCHAR(255) NOT NULL, "
                    + "primary_firearm VARCHAR(255), "
                    + "primary_attachments VARCHAR(255), "
                    + "secondary_firearm VARCHAR(255), "
                    + "secondary_attachments VARCHAR(255), "
                    + "equipment VARCHAR(255), "
                    + "melee_weapon VARCHAR(255)"
                    + ");"
            );
            statement.execute("CREATE TABLE `battlegrounds_statistic` ("
                    + "statistic_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, "
                    + "player_uuid VARCHAR(36) NOT NULL, "
                    + "game_id INT NOT NULL, "
                    + "gamemode VARCHAR(255) NOT NULL, "
                    + "kills INT NOT NULL, "
                    + "deaths INT NOT NULL, "
                    + "headshots INT NOT NULL"
                    + ");"
            );
            statement.execute("ALTER TABLE `battlegrounds_loadout`"
                    + "ADD CONSTRAINT fk_loadout_player_uuid "
                    + "FOREIGN KEY (player_uuid) "
                    + "REFERENCES battlegrounds_player(player_uuid);"
            );
            statement.execute("ALTER TABLE `battlegrounds_statistic`"
                    + "ADD CONSTRAINT fk_statistic_player_uuid "
                    + "FOREIGN KEY (player_uuid) "
                    + "REFERENCES battlegrounds_player(player_uuid);"
            );
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
