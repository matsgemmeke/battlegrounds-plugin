package com.matsg.battlegrounds.mode.zombies.handler;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.event.EventHandler;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.entity.TimerDownState;
import com.matsg.battlegrounds.entity.state.DownedPlayerState;
import com.matsg.battlegrounds.mode.zombies.PerkManager;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import com.matsg.battlegrounds.util.EnumTitle;
import org.bukkit.Location;

public class GamePlayerDeathEventHandler implements EventHandler<GamePlayerDeathEvent> {

    private Game game;
    private InternalsProvider internals;
    private PerkManager perkManager;
    private TaskRunner taskRunner;
    private Translator translator;
    private Zombies zombies;

    public GamePlayerDeathEventHandler(
            Game game,
            Zombies zombies,
            PerkManager perkManager,
            InternalsProvider internals,
            TaskRunner taskRunner,
            Translator translator
    ) {
        this.game = game;
        this.zombies = zombies;
        this.perkManager = perkManager;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
    }

    public void handle(GamePlayerDeathEvent event) {
        if (!zombies.isActive()) {
            return;
        }

        GamePlayer gamePlayer = event.getGamePlayer();

        if (!gamePlayer.getState().isAlive() || !gamePlayer.getState().canInteract()) {
            return;
        }

        int downDuration = zombies.getConfig().getDownDuration();
        int reviveDuration;

        if (perkManager.hasPerkEffect(gamePlayer, PerkEffectType.QUICK_REVIVE)) {
            reviveDuration = zombies.getConfig().getQuickReviveDuration();
        } else {
            reviveDuration = zombies.getConfig().getReviveDuration();
        }

        perkManager.removePerks(gamePlayer);

        // Skip the respawn screen
        gamePlayer.getPlayer().spigot().respawn();

        // Stop the game when the  player is the last one alive
        if (game.getPlayerManager().getActivePlayers().length <= 1) {
            game.stop();
            return;
        }

        // Announce the player down
        for (GamePlayer g : game.getPlayerManager().getPlayers()) {
            EnumTitle.PLAYER_DOWNED.send(g.getPlayer(), new Placeholder("player_name", gamePlayer.getName()));
        }

        // Remove the target assignment from the mobs
        for (Mob mob : game.getMobManager().getMobs()) {
            mob.resetDefaultPathfinderGoals();
        }

        double maxRevivingDistance = zombies.getConfig().getReviveMaxDistance();
        // Round the points to the nearest 10
        int points = (int) (Math.round((double) gamePlayer.getPoints() / (double) 100) * 10);
        Location location = gamePlayer.getLocation();

        internals.sendActionBar(gamePlayer.getPlayer(), translator.translate(TranslationKey.ACTIONBAR_POINTS_DECREASE.getPath(), new Placeholder("bg_points", points)));

        TimerDownState downState = new TimerDownState(game, gamePlayer, location, internals, taskRunner, translator, downDuration, reviveDuration);
        downState.setMaxRevivingDistance(maxRevivingDistance);
        downState.setOnPlayerKill(this::onPlayerKill);
        downState.setPoints(points);
        downState.run();

        PlayerState playerState = new DownedPlayerState(gamePlayer, gamePlayer.getPlayer().getWalkSpeed());
        gamePlayer.changeState(playerState);

        gamePlayer.setDownState(downState);
        gamePlayer.setPoints(gamePlayer.getPoints() - points);

        game.updateScoreboard();
    }

    private void onPlayerKill(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().getInventory().clear();

        // Remove the player's current loadout from the game
        for (Weapon weapon : gamePlayer.getLoadout().getWeapons()) {
            game.getItemRegistry().removeItem(weapon);
        }

        // Reset the player state and restore default items
        zombies.prepareDefaultLoadout(gamePlayer);
    }
}
