package com.matsg.battlegrounds.game.mode;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.SelectLoadout;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

/**
 * Abstract gamemode class for gamemodes which are to be played in a default
 * arena. Contains functions which apply to these gamemodes by default.
 */
public abstract class ArenaGameMode extends AbstractGameMode {

    public ArenaGameMode(Battlegrounds plugin, Game game) {
        super(plugin, game);
    }

    public ArenaComponent getComponent(int id) {
        return null;
    }

    public Collection<ArenaComponent> getComponents() {
        return Collections.EMPTY_SET;
    }

    public int getComponentCount() {
        return 0;
    }

    protected TranslationKey getKillMessageKey(Hitbox hitbox) {
        switch (hitbox) {
            case HEAD:
                return TranslationKey.DEATH_HEADSHOT;
            case LEG:
                return TranslationKey.DEATH_PLAYER_KILL;
            case TORSO:
                return TranslationKey.DEATH_PLAYER_KILL;
            default:
                return null;
        }
    }

    public void handleDeath(GamePlayer gamePlayer) {
        gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
        gamePlayer.setLives(gamePlayer.getLives() - 1);

        if (gamePlayer.getLives() <= 0) {
            gamePlayer.setState(PlayerState.SPECTATING);
            gamePlayer.getState().apply(game, gamePlayer);
        }

        Weapon weapon = gamePlayer.getLoadout().getWeapon(gamePlayer.getPlayer().getItemInHand());

        if (weapon instanceof Firearm) {
            Location location = gamePlayer.getLocation();
            Item item = location.getWorld().dropItem(location, weapon.getItemStack());
            ((Firearm) weapon).onDrop(gamePlayer, item);
        }
    }

    public void preparePlayer(GamePlayer gamePlayer) {
        super.preparePlayer(gamePlayer);

        Player player = gamePlayer.getPlayer();
        player.getInventory().setArmorContents(new ItemStack[] {
                null,
                null,
                new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                        .addItemFlags(ItemFlag.values())
                        .setColor(game.getGameMode().getTeam(gamePlayer).getColor())
                        .setDisplayName(ChatColor.WHITE + messageHelper.create(TranslationKey.ARMOR_VEST))
                        .setUnbreakable(true)
                        .build(),
                new ItemStackBuilder(Material.LEATHER_HELMET)
                        .addItemFlags(ItemFlag.values())
                        .setColor(game.getGameMode().getTeam(gamePlayer).getColor())
                        .setDisplayName(ChatColor.WHITE + messageHelper.create(TranslationKey.ARMOR_HELMET))
                        .setUnbreakable(true)
                        .build()
        });

        SelectLoadout selectLoadout = new SelectLoadout(game);
        game.getItemRegistry().addItem(selectLoadout);
        gamePlayer.getHeldItems().add(selectLoadout);
        player.getInventory().setItem(ItemSlot.MISCELLANEOUS.getSlot(), selectLoadout.getItemStack());
    }

    public void start() {
        super.start();
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            if (gamePlayer.getLoadout() == null) {
                gamePlayer.getPlayer().openInventory(new SelectLoadoutView(plugin, game, gamePlayer).getInventory());
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
