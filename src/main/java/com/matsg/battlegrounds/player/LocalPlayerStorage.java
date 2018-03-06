package com.matsg.battlegrounds.player;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.PlayerYaml;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.player.OfflineGamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStorage;
import com.matsg.battlegrounds.config.BattlePlayerYaml;
import com.matsg.battlegrounds.config.DefaultClasses;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LocalPlayerStorage implements PlayerStorage {

    private Battlegrounds plugin;
    private DefaultClasses defaultClasses;
    private File folder;

    public LocalPlayerStorage(Battlegrounds plugin) throws IOException {
        this.plugin = plugin;
        this.defaultClasses = new DefaultClasses(plugin);
        this.folder = new File(plugin.getDataFolder().getPath() + "/players");
    }

    public void addPlayerAttributes(StoredPlayer player) {

    }

    public boolean contains(UUID uuid) {
        return new File(folder, uuid.toString() + ".yml").exists();
    }

    public List<? extends OfflineGamePlayer> getList() {
        return null;
    }

    public StoredPlayer getStoredPlayer(UUID uuid) {
        return null;
    }

    public List<? extends OfflineGamePlayer> getTopPlayers(int limit) {
        return null;
    }

    public StoredPlayer registerPlayer(UUID uuid, String name) {
        try {
            PlayerYaml playerYaml = new BattlePlayerYaml(plugin, uuid);
            for (int i = 1; i <= 5; i ++) {
                playerYaml.saveLoadoutClass(i, defaultClasses.getList().get(i - 1));
            }
        } catch (IOException e) {
            return null;
        }
        return getStoredPlayer(uuid);
    }
}