package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.entity.Item;

public enum IgnitionType {

    // TODO More fitting names?
    AGGRESSIVE(0) {
        public void handleIgnition(Equipment equipment, Item item) {
            new BattleRunnable() {
                Game game = equipment.getGame();
                GamePlayer gamePlayer = equipment.getGamePlayer();

                public void run() {
                    if (game == null || gamePlayer == null) {
                        return;
                    }
                    for (Sound sound : equipment.getIgnitionSound()) {
                        sound.play(game, item.getLocation());
                    }
                    equipment.ignite(item);
                    equipment.getDroppedItems().remove(item);
                    item.remove();
                }
            }.runTaskLater(equipment.getIgnitionTime());
        }
    },
    PASSIVE(1) {
        public void handleIgnition(Equipment equipment, Item item) {
            new BattleRunnable() {
                Game game = equipment.getGame();
                GamePlayer gamePlayer = equipment.getGamePlayer();

                public void run() {
                    if (game == null || gamePlayer == null || !equipment.getDroppedItems().contains(item)) {
                        return;
                    }
                    if (game.getPlayerManager().getNearbyEnemyPlayers(game.getGameMode().getTeam(gamePlayer), item.getLocation(), equipment.getLongRange()).length >= 1) {
                        for (Sound sound : equipment.getIgnitionSound()) {
                            sound.play(game, item.getLocation());
                        }
                        equipment.ignite(item);
                        equipment.getDroppedItems().remove(item);
                        item.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(0, 20);
        }
    };

    private int id;

    IgnitionType(int id) {
        this.id = id;
    }

    public static IgnitionType valueOf(int id) {
        for (IgnitionType ignitionType : values()) {
            if (ignitionType.id == id) {
                return ignitionType;
            }
        }
        throw new IllegalArgumentException();
    }

    public abstract void handleIgnition(Equipment equipment, Item item);
}
