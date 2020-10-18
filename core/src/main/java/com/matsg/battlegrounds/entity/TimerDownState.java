package com.matsg.battlegrounds.entity;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.DownState;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TimerDownState implements DownState {

    private static final long TIMER_DELAY = 0;
    private static final long TIMER_PERIOD = 1;

    @Nullable
    private Consumer<GamePlayer> onPlayerKill;
    private double maxRevivingDistance;
    @NotNull
    private Game game;
    @NotNull
    private GamePlayer gamePlayer;
    @Nullable
    private GamePlayer reviver;
    @Nullable
    private Hologram hologram;
    private int points;
    @NotNull
    private InternalsProvider internals;
    @NotNull
    private Location location;
    private long downDuration;
    private long downTime;
    private long reviveDuration;
    private long reviveTime;
    @Nullable
    private Sound sound;
    @NotNull
    private TaskRunner taskRunner;
    @NotNull
    private Translator translator;

    public TimerDownState(
            @NotNull Game game,
            @NotNull GamePlayer gamePlayer,
            @NotNull Location location,
            @NotNull InternalsProvider internals,
            @NotNull TaskRunner taskRunner,
            @NotNull Translator translator,
            long downDuration,
            long reviveDuration
    ) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.location = location;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.downDuration = downDuration;
        this.reviveDuration = reviveDuration;
        this.downTime = 0;
        this.reviveTime = 0;
    }

    @NotNull
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    public double getMaxRevivingDistance() {
        return maxRevivingDistance;
    }

    public void setMaxRevivingDistance(double maxRevivingDistance) {
        this.maxRevivingDistance = maxRevivingDistance;
    }

    @Nullable
    public Consumer<GamePlayer> getOnPlayerKill() {
        return onPlayerKill;
    }

    public void setOnPlayerKill(@Nullable Consumer<GamePlayer> onPlayerKill) {
        this.onPlayerKill = onPlayerKill;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Nullable
    public GamePlayer getReviver() {
        return reviver;
    }

    public void setReviver(@Nullable GamePlayer reviver) {
        this.reviver = reviver;
    }

    @Nullable
    public Sound getSound() {
        return sound;
    }

    public void setSound(@Nullable Sound sound) {
        this.sound = sound;
    }

    public void dispose() {
        if (hologram != null) {
            hologram.remove();
        }
    }

    public void run() {
        hologram = new Hologram(location);

        taskRunner.runTaskTimer(new BukkitRunnable() {
            public void run() {
                if (!game.getState().isInProgress()) {
                    cancel();
                }

                Location hologramLocation = location.clone().add(0, 0.5, 0);
                String hologramText = getHologramText(downTime, downDuration);

                hologram.moveTo(hologramLocation);
                hologram.setGravity(false);
                hologram.setText(hologramText);
                hologram.update();

                // If the reviver has stood close to the downed player for a period of time, revive the downed player
                if (reviver != null && ++reviveTime > reviveDuration) {
                    cancel();
                    revive(gamePlayer);
                    return;
                }

                // If the reviver moves too far away from the downed player, reset the reviving status
                if (reviver != null && reviver.getLocation().distance(gamePlayer.getLocation()) > maxRevivingDistance) {
                    // Send empty titles to clear the previous title
                    internals.sendTitle(gamePlayer.getPlayer(), "", "", 0, 0, 0);
                    internals.sendTitle(reviver.getPlayer(), "", "", 0, 0, 0);

                    reviver = null;
                    reviveTime = 0;
                }

                // If the reviver abandoned the downed player, reset the revive timer
                if (reviver == null && reviveTime > 0) {
                    reviveTime = 0;
                }

                // If the downed player was not revived after a period of time, kill the downed player
                if (reviver == null && ++downTime > downDuration) {
                    cancel();
                    gamePlayer.getPlayer().getInventory().clear();
                    gamePlayer.setState(PlayerState.SPECTATING);
                    gamePlayer.getState().apply(game, gamePlayer);
                    hologram.remove();

                    if (onPlayerKill != null) {
                        onPlayerKill.accept(gamePlayer);
                    }
                }
            }
        }, TIMER_DELAY, TIMER_PERIOD);
    }

    private String getHologramText(long downTime, long downDuration) {
        double percentage = (double) downTime / (double) downDuration;

        String hologramMessage = translator.translate(TranslationKey.HOLOGRAM_REVIVE.getPath());
        String hologramText;

        if (reviver != null) {
            hologramText = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + hologramMessage;
        } else if (percentage <= 0.3) {
            hologramText = ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + hologramMessage;
        } else if (percentage > 0.3 && percentage <= 0.6) {
            hologramText = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + hologramMessage;
        } else {
            hologramText = ChatColor.RED.toString() + ChatColor.BOLD.toString() + hologramMessage;
        }

        return hologramText;
    }

    private void revive(GamePlayer gamePlayer) {
        gamePlayer.setDownState(null);
        gamePlayer.setState(PlayerState.ACTIVE);
        gamePlayer.getState().apply(game, gamePlayer);

        if (sound != null) {
            sound.play(game, location);
        }

        String actionBar = translator.translate(TranslationKey.ACTIONBAR_POINTS_INCREASE.getPath(), new Placeholder("bg_points", points));
        internals.sendActionBar(reviver.getPlayer(), actionBar);

        reviver.setPoints(reviver.getPoints() + points);
        game.updateScoreboard();
        hologram.remove();
    }
}
