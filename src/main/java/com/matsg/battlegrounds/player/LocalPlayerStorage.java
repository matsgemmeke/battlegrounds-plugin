package com.matsg.battlegrounds.player;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.PlayerYaml;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.player.OfflineGamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStorage;
import com.matsg.battlegrounds.config.BattlePlayerYaml;
import com.matsg.battlegrounds.config.DefaultClasses;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LocalPlayerStorage implements PlayerStorage {

    private Battlegrounds plugin;
    private DefaultClasses defaultClasses;
    private File folder;
    private List<PlayerYaml> playerYamls;

    public LocalPlayerStorage(Battlegrounds plugin) throws IOException {
        this.plugin = plugin;
        this.defaultClasses = new DefaultClasses(plugin);
        this.folder = new File(plugin.getDataFolder().getPath() + "/players");
        this.playerYamls = new ArrayList<>();

        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (File file : folder.listFiles()) {
            playerYamls.add(new BattlePlayerYaml(plugin, UUID.fromString(file.getName().substring(0, file.getName().length() - 4))));
        }
    }

    public void addPlayerAttributes(OfflineGamePlayer player) {
        PlayerYaml playerYaml = getPlayerYaml(player.getUUID());
        if (playerYaml == null) {
            return;
        }
        StoredPlayer storedPlayer = playerYaml.getStoredPlayer();
        storedPlayer.setDeaths(storedPlayer.getDeaths() + player.getDeaths());
        storedPlayer.setExp(storedPlayer.getExp() + player.getExp());
        storedPlayer.setHeadshots(storedPlayer.getHeadshots() + player.getHeadshots());
        storedPlayer.setKills(storedPlayer.getKills() + player.getKills());
        playerYaml.save();
    }

    public boolean contains(UUID uuid) {
        return getPlayerYaml(uuid) != null;
    }

    public List<? extends OfflineGamePlayer> getList() {
        List<StoredPlayer> list = new ArrayList<>();
        for (PlayerYaml playerYaml : playerYamls) {
            list.add(playerYaml.getStoredPlayer());
        }
        return list;
    }

    private PlayerYaml getPlayerYaml(UUID uuid) {
        for (PlayerYaml playerYaml : playerYamls) {
            if (playerYaml.getStoredPlayer().getUUID().equals(uuid)) {
                return playerYaml;
            }
        }
        return null;
    }

    public StoredPlayer getStoredPlayer(UUID uuid) {
        if (!contains(uuid)) {
            return null;
        }
        return getPlayerYaml(uuid).getStoredPlayer();
    }

    public List<? extends OfflineGamePlayer> getTopPlayers(int limit) {
        List<? extends OfflineGamePlayer> list = getList();
        Collections.sort(list, new Comparator<OfflineGamePlayer>() {
            public int compare(OfflineGamePlayer o1, OfflineGamePlayer o2) {
                return ((Integer) o2.getExp()).compareTo(o1.getExp()); // Reverse sort
            }
        });
        return list;
    }

    public StoredPlayer registerPlayer(UUID uuid, String name) {
        try {
            PlayerYaml playerYaml = new BattlePlayerYaml(plugin, uuid);
            playerYaml.createDefaultAttributes();
            for (int i = 1; i <= 5; i ++) {
                playerYaml.saveLoadout(defaultClasses.getList().get(i - 1));
            }
            playerYamls.add(playerYaml);
        } catch (IOException e) {
            return null;
        }
        return getStoredPlayer(uuid);
    }

    public void updatePlayer(Player player) {
        getPlayerYaml(player.getUniqueId()).updateName(player.getName());
    }
}