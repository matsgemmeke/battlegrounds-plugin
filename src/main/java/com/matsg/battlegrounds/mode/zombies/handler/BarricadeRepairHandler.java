package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.Barricade;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class BarricadeRepairHandler implements EventHandler<BlockPlaceEvent> {

    private Game game;
    private Zombies zombies;

    public BarricadeRepairHandler(Game game, Zombies zombies) {
        this.game = game;
        this.zombies = zombies;
    }

    public void handle(BlockPlaceEvent event) {
        if (!zombies.isActive()) {
            return;
        }

        Player player = event.getPlayer();
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);

        if (gamePlayer == null) {
            return;
        }

        Block block = event.getBlockPlaced();

        for (Section section : zombies.getSectionContainer().getAll()) {
            for (Barricade barricade : section.getBarricadeContainer().getAll()) {
                if (barricade != null && barricade.onConstruct(gamePlayer, block)) {
                    int repairPoints = 10;
                    game.getPlayerManager().givePoints(gamePlayer, repairPoints);
                    game.updateScoreboard();

                    ItemSlot itemSlot = ItemSlot.MISCELLANEOUS;

                    // Return the placed block to the player
                    player.getInventory().setItem(itemSlot.getSlot(), player.getItemInHand());

                    // Make sure the event is not cancelled otherwise the reparation will not work
                    event.setCancelled(false);
                    break;
                }
            }
        }
    }
}
