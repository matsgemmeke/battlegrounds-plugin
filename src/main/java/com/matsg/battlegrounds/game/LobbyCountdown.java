package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LobbyCountdown extends BattleRunnable {

    private Game game;
    private int id, time;
    private List<Integer> display;
    private LobbyScoreboard scoreboard;

    public LobbyCountdown(Game game, int time, int... display) {
        this.game = game;
        this.time = time;
        this.display = new ArrayList<>();
        this.scoreboard = new LobbyScoreboard(plugin, game);

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
                if (countdown <= 0) {
                    for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                        Player player = gamePlayer.getPlayer();
                        player.setFoodLevel(20);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setHealth(player.getMaxHealth());
                        player.setSaturation((float) 10);

                        gamePlayer.getPlayer().getInventory().setArmorContents(new ItemStack[] {
                                null,
                                null,
                                new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                                        .addItemFlags(ItemFlag.values())
                                        .setColor(game.getGameMode().getTeam(gamePlayer).getColor())
                                        .setDisplayName(ChatColor.WHITE + EnumMessage.ARMOR_VEST.getMessage())
                                        .build(),
                                new ItemStackBuilder(Material.LEATHER_HELMET)
                                        .addItemFlags(ItemFlag.values())
                                        .setColor(game.getGameMode().getTeam(gamePlayer).getColor())
                                        .setDisplayName(ChatColor.WHITE + EnumMessage.ARMOR_HELMET.getMessage())
                                        .build()
                        });
                    }
                    game.getGameMode().spawnPlayers(game.getPlayerManager().getPlayers().toArray(new GamePlayer[game.getPlayerManager().getPlayers().size()]));
                    game.setState(GameState.STARTING);
                    game.updateSign();
                    new GameCountdown(game, game.getConfiguration().getGameCountdown()).runTaskTimer(20, 20);
                    cancel();
                    return;
                }
                if (display.contains(countdown)) {
                    game.broadcastMessage(EnumMessage.COUNTDOWN_NOTE.getMessage(new Placeholder("bg_countdown", countdown)));
                    BattleSound.COUNTDOWN_NOTE.play(game);
                }
                scoreboard.setCountdown(countdown);
                scoreboard.display(game);
                countdown --;
            }
        }, 0, 20);
    }
}