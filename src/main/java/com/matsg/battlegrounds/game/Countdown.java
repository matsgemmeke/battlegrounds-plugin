package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Countdown extends BattleRunnable {

    private Game game;
    private int id, time;
    private List<Integer> display;

    public Countdown(Game game, int time, int... display) {
        this.game = game;
        this.time = time;
        this.display = new ArrayList<>();

        for (int i : display) {
            this.display.add(i);
        }
    }

    public void cancel() {
        plugin.getServer().getScheduler().cancelTask(id);
    }

    public void run() {
        id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int countdown = time;
            public void run() {
                if (game.getArena() == null || game.getPlayerManager().getPlayers().size() <= 0) {
                    game.setState(GameState.WAITING);
                    game.updateSign();
                    cancel();
                    return;
                }
                if (countdown == 0) {
                    for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                        Player player = gamePlayer.getPlayer();
                        player.setFoodLevel(20);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setHealth(player.getMaxHealth());
                        player.setSaturation((float) 10);
                    }

                    game.getGameMode().spawnPlayers(game.getPlayerManager().getPlayers().toArray(new GamePlayer[game.getPlayerManager().getPlayers().size()]));
                    game.setState(GameState.IN_GAME);
                    game.updateSign();

                    cancel();
                    return;
                }
                if (display.contains(countdown)) {
                    game.broadcastMessage(EnumMessage.COUNTDOWN_NOTE.getMessage(new Placeholder("bg_countdown", countdown)));
                    BattleSound.COUNTDOWN_NOTE.play(game);
                }
                if (countdown <= 5) {
                    for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                        Title.COUNTDOWN.send(gamePlayer.getPlayer(), new Placeholder("bg_countdown", countdown));
                    }
                    BattleSound.COUNTDOWN_NOTE.play(game);
                }
                countdown --;
            }
        }, 0, 20);
    }
}