package com.matsg.battlegrounds.player;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.player.OfflineGamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStorage;
import com.matsg.battlegrounds.config.BattlePlayerYaml;
import com.matsg.battlegrounds.config.DefaultLoadouts;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LocalPlayerStorage implements PlayerStorage {

    private Battlegrounds plugin;
    private DefaultLoadouts defaultLoadouts;
    private File folder;
    private List<StoredPlayer> storedPlayers;

    public LocalPlayerStorage(Battlegrounds plugin) throws IOException {
        this.plugin = plugin;
        this.defaultLoadouts = new DefaultLoadouts(plugin);
        this.folder = new File(plugin.getDataFolder().getPath() + "/players");
        this.storedPlayers = new ArrayList<>();

        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (File file : folder.listFiles()) {
            storedPlayers.add(new BattlePlayerYaml(plugin, UUID.fromString(file.getName().substring(0, file.getName().length() - 4))));
        }
    }

    public List<? extends OfflineGamePlayer> getList() {
        return storedPlayers;
    }

    public void addPlayerAttributes(OfflineGamePlayer player) {
        StoredPlayer storedPlayer = getStoredPlayer(player.getUUID());
        if (storedPlayer == null) {
            return;
        }
        storedPlayer.setDeaths(storedPlayer.getDeaths() + player.getDeaths());
        storedPlayer.setExp(storedPlayer.getExp() + player.getExp());
        storedPlayer.setHeadshots(storedPlayer.getHeadshots() + player.getHeadshots());
        storedPlayer.setKills(storedPlayer.getKills() + player.getKills());
    }

    public boolean contains(UUID uuid) {
        return getStoredPlayer(uuid) != null;
    }

    public StoredPlayer getStoredPlayer(UUID uuid) {
        for (StoredPlayer  storedPlayer : storedPlayers) {
            if (storedPlayer.getUUID().equals(uuid)) {
                return storedPlayer;
            }
        }
        return null;
    }

    public List<? extends OfflineGamePlayer> getTopPlayers(int limit) {
        List<? extends OfflineGamePlayer> list = getList();
        Collections.sort(list, new Comparator<OfflineGamePlayer>() {
            public int compare(OfflineGamePlayer o1, OfflineGamePlayer o2) {
                return ((Integer) o2.getExp()).compareTo(o1.getExp()); // Reverse sort
            }
        });
        return list.stream().limit(limit).collect(Collectors.toList());
    }

    public StoredPlayer registerPlayer(UUID uuid, String name) {
        try {
            StoredPlayer storedPlayer = new BattlePlayerYaml(plugin, uuid);
            storedPlayer.createDefaultAttributes();
            for (int i = 1; i <= 5; i ++) {
                Loadout loadout = defaultLoadouts.getList().get(i - 1);
                storedPlayer.saveLoadout(loadout.getId(), loadout);
            }
            storedPlayers.add(storedPlayer);
        } catch (IOException e) {
            return null;
        }
        return getStoredPlayer(uuid);
    }

    public void updatePlayer(Player player) {
        getStoredPlayer(player.getUniqueId()).updateName(player.getName());
    }
}
