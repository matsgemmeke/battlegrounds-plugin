package com.matsg.battlegrounds.game.mode;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import org.bukkit.Location;
import org.bukkit.entity.Item;

import java.util.Collection;
import java.util.Collections;

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

        if (weapon != null && weapon instanceof Firearm) {
            Location location = gamePlayer.getLocation();
            Item item = location.getWorld().dropItem(location, weapon.getItemStack());
            ((Firearm) weapon).onDrop(gamePlayer, item);
        }
    }

    public void start() {
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            if (gamePlayer.getLoadout() == null) {
                gamePlayer.getPlayer().openInventory(new SelectLoadoutView(plugin, game, gamePlayer).getInventory());
            }
        }

        for (Spawn spawn : game.getArena().getSpawnContainer().getAll()) {
            spawn.setGamePlayer(null);
        }
    }

    public void tick() {
        GameScoreboard scoreboard = getScoreboard();

        if (scoreboard != null) {
            scoreboard.display(game);
        }

        for (Objective objective : objectives) {
            if (objective.isAchieved(game)) {
                game.callEvent(new GameEndEvent(game, objective, null, getSortedTeams()));
                game.stop();
                break;
            }
        }
    }
}
