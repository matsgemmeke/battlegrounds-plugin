package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
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

    private static final int DEFAULT_REPAIR_POINTS = 10;

    private Game game;
    private InternalsProvider internals;
    private Translator translator;
    private Zombies zombies;

    public BarricadeRepairHandler(Game game, Zombies zombies, InternalsProvider internals, Translator translator) {
        this.game = game;
        this.zombies = zombies;
        this.internals = internals;
        this.translator = translator;
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
                    int repairPoints = zombies.getPowerUpManager().getPowerUpPoints(DEFAULT_REPAIR_POINTS);

                    String actionBar = translator.translate(TranslationKey.ACTIONBAR_POINTS_INCREASE.getPath(), new Placeholder("bg_points", repairPoints));
                    internals.sendActionBar(gamePlayer.getPlayer(), actionBar);

                    gamePlayer.setPoints(gamePlayer.getPoints() + repairPoints);
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
