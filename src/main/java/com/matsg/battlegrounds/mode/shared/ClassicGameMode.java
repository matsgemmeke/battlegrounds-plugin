package com.matsg.battlegrounds.mode.shared;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.mode.AbstractGameMode;
import com.matsg.battlegrounds.mode.Result;
import com.matsg.battlegrounds.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.SelectLoadout;
import com.matsg.battlegrounds.nms.Title;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

/**
 * Abstract class for classic gamemodes which are to be played in a default
 * arena. Contains functions which apply to these gamemodes by default.
 */
public abstract class ClassicGameMode extends AbstractGameMode {

    public ClassicGameMode(Battlegrounds plugin, Game game, Translator translator, SpawningBehavior spawningBehavior) {
        super(plugin, game, translator, spawningBehavior);
    }

    public void onDisable() {
        Arena arena = game.getArena();

        if (arena != null) {
            for (Entity entity : arena.getWorld().getEntitiesByClass(Item.class)) {
                if (arena.contains(entity.getLocation())) {
                    entity.remove();
                }
            }
        }
    }

    public ArenaComponent getComponent(int id) {
        return null;
    }

    public ArenaComponent getComponent(Location location) {
        return null;
    }

    public Collection<ArenaComponent> getComponents() {
        return Collections.EMPTY_SET;
    }

    public int getComponentCount() {
        return 0;
    }

    public GamePlayer[] getNearbyEntities(Location location, Team team, double range) {
        return game.getPlayerManager().getNearbyEnemyPlayers(team, location, range);
    }

    public void preparePlayer(GamePlayer gamePlayer) {
        super.preparePlayer(gamePlayer);

        Player player = gamePlayer.getPlayer();
        player.getInventory().setArmorContents(new ItemStack[] {
                null,
                null,
                new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                        .addItemFlags(ItemFlag.values())
                        .setColor(gamePlayer.getTeam().getArmorColor())
                        .setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.ARMOR_VEST))
                        .setUnbreakable(true)
                        .build(),
                new ItemStackBuilder(Material.LEATHER_HELMET)
                        .addItemFlags(ItemFlag.values())
                        .setColor(gamePlayer.getTeam().getArmorColor())
                        .setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.ARMOR_HELMET))
                        .setUnbreakable(true)
                        .build()
        });

        SelectLoadout selectLoadout = new SelectLoadout(plugin, game, translator);
        game.getItemRegistry().addItem(selectLoadout);
        gamePlayer.getItems().add(selectLoadout);
        player.getInventory().setItem(ItemSlot.MISCELLANEOUS.getSlot(), selectLoadout.getItemStack());
    }

    public void startCountdown() {
        game.setState(game.getState().next());
        game.updateSign();

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            if (gamePlayer.getLoadout() == null) {
                gamePlayer.getPlayer().openInventory(new SelectLoadoutView(plugin, translator, game, gamePlayer).getInventory());
            }
        }
    }

    public void stop() {
        Objective objective = getAchievedObjective();

        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    String resultText = translator.translate(result.getTranslationKey());

                    Title title = objective.getTitle().clone();
                    title.setTitleText(translator.createSimpleMessage(title.getTitleText(), new Placeholder("bg_result", resultText)));
                    title.setSubText(translator.createSimpleMessage(title.getSubText(), new Placeholder("bg_result", resultText)));
                    title.send(gamePlayer.getPlayer());
                }
            }
        }
    }

    public void tick() {
        GameScoreboard scoreboard = getScoreboard();

        if (scoreboard != null) {
            scoreboard.display(game);
        }

        for (Objective objective : objectives) {
            if (objective.isAchieved()) {
                game.callEvent(new GameEndEvent(game, objective, null, getSortedTeams()));
                game.stop();
                break;
            }
        }
    }
}
