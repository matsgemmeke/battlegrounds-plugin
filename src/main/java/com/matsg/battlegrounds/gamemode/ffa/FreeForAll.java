package com.matsg.battlegrounds.gamemode.ffa;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.gamemode.AbstractGameMode;
import com.matsg.battlegrounds.gamemode.Result;
import com.matsg.battlegrounds.gui.scoreboard.GameScoreboard;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public class FreeForAll extends AbstractGameMode {

    private boolean scoreboardEnabled;
    private Color color;
    private double minSpawnDistance;
    private int killsToWin;

    public FreeForAll(Game game, Yaml yaml) {
        super(game, EnumMessage.FFA_NAME.getMessage(), EnumMessage.FFA_SHORT.getMessage(), yaml);
        this.color = getConfigColor();
        this.killsToWin = yaml.getInt("kills-to-win");
        this.minSpawnDistance = yaml.getDouble("minimum-spawn-distance");
        this.scoreboardEnabled = yaml.getBoolean("scoreboard.enabled");
        this.timeLimit = yaml.getInt("time-limit");
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (getTeam(gamePlayer) != null) {
            return;
        }
        Team team = new BattleTeam(0, gamePlayer.getName(), color, ChatColor.WHITE);
        team.addPlayer(gamePlayer);
        teams.add(team);
    }

    private Color getConfigColor() {
        String[] array = yaml.getString("armor-color").split(",");
        return Color.fromRGB(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return game.getArena().getRandomSpawn(minSpawnDistance);
    }

    public GameScoreboard getScoreboard() {
        return scoreboardEnabled ? new FFAScoreboard(game, yaml) : null;
    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon) {
        game.broadcastMessage(EnumMessage.DEATH_PLAYER_KILL.getMessage(new Placeholder[] {
                new Placeholder("bg_killer", killer.getName()),
                new Placeholder("bg_player", gamePlayer.getName()),
                new Placeholder("bg_weapon", weapon.getName())
        }));
        gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
        killer.addScore(100);
        killer.setKills(killer.getKills() + 1);

        if (killer.getKills() >= killsToWin) {
            game.stop();
        }
    }

    public void onStart() {
        super.onStart();
        game.broadcastMessage(EnumTitle.FFA_START);
    }

    public void onStop() {
        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    gamePlayer.sendMessage(new Title(result.getTitle(), getEndMessage(), 10, 80, 10));
                }
            }
        }
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        teams.remove(team);
    }

    public void spawnPlayers(GamePlayer... players) {
        for (Team team : teams) {
            for (GamePlayer gamePlayer : team.getPlayers()) {
                Spawn spawn = game.getArena().getRandomSpawn();
                spawn.setGamePlayer(gamePlayer);
                gamePlayer.getPlayer().teleport(spawn.getLocation());
            }
        }
    }
}