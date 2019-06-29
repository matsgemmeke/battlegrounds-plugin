package com.matsg.battlegrounds.mode.shared.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.api.item.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

public class GeneralKillHandler implements EventHandler<GamePlayerKillEntityEvent> {

    private Battlegrounds plugin;
    private Game game;
    private GameMode gameMode;
    private Translator translator;

    public GeneralKillHandler(Battlegrounds plugin, Game game, GameMode gameMode, Translator translator) {
        this.plugin = plugin;
        this.game = game;
        this.gameMode = gameMode;
        this.translator = translator;
    }

    public void handle(GamePlayerKillEntityEvent event) {
        if (event.getGame() != game || !gameMode.isActive() || !(event.getEntity() instanceof GamePlayer)) {
            return;
        }

        GamePlayer gamePlayer = (GamePlayer) event.getEntity();
        GamePlayer killer = event.getGamePlayer();
        Hitbox hitbox = event.getHitbox();
        Weapon weapon = event.getWeapon();

        game.getPlayerManager().broadcastMessage(translator.translate(hitbox.getTranslationKey(),
                new Placeholder("bg_killer", killer.getTeam().getChatColor() + killer.getName() + ChatColor.WHITE),
                new Placeholder("bg_player", gamePlayer.getTeam().getChatColor() + gamePlayer.getName() + ChatColor.WHITE),
                new Placeholder("bg_weapon", weapon.getName()))
        );

        killer.addExp(100);
        killer.setKills(killer.getKills() + 1);
        killer.getTeam().setScore(killer.getTeam().getScore() + 1);

        game.getPlayerManager().updateExpBar(killer);

        GameMode gameMode = game.getGameMode();
        Objective objective = gameMode.getAchievedObjective();

        if (objective != null) {
            game.callEvent(new GameEndEvent(game, objective, gameMode.getTopTeam(), gameMode.getSortedTeams()));
            game.stop();
        }

        Event deathEvent = new GamePlayerDeathEvent(game, gamePlayer, DeathCause.PLAYER_KILL);
        // Handle the event on the event dispatcher so we can reuse the event without calling a listener to it
        plugin.getEventDispatcher().dispatchEvent(deathEvent);
        // Handle the event on the plugin manager so other plugin can listen to this event as well
        plugin.getServer().getPluginManager().callEvent(deathEvent);
    }
}
