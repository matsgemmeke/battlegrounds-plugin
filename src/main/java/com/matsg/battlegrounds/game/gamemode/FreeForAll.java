package com.matsg.battlegrounds.game.gamemode;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.game.BattleTeam;
import org.bukkit.Color;

public class FreeForAll extends AbstractGameMode {

    private PlayerManager playerManager;

    public FreeForAll(Game game, Yaml yaml) {
        super(game, "Free-for-all", "FFA", yaml);
        this.playerManager = game.getPlayerManager();
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (playerManager.getTeam(gamePlayer.getPlayer()) != null) {
            return;
        }
        Team team = new BattleTeam(gamePlayer.getName(), Color.fromRGB(100, 100, 100));
        team.getPlayers().add(gamePlayer);
        teams.add(team);
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = playerManager.getTeam(gamePlayer.getPlayer());
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