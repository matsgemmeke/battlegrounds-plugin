package com.matsg.battlegrounds.gamemode.ffa;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.gamemode.AbstractGameMode;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.Color;

public class FreeForAll extends AbstractGameMode {

    public FreeForAll(Game game, Yaml yaml) {
        super(game, EnumMessage.FFA_NAME.getMessage(), EnumMessage.FFA_SHORT.getMessage(), yaml);
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (getTeam(gamePlayer) != null) {
            return;
        }
        Team team = new BattleTeam(0, gamePlayer.getName(), Color.fromRGB(100, 100, 100));
        team.getPlayers().add(gamePlayer);
        teams.add(team);
    }

    public void onStart() {
        game.broadcastMessage(Title.FFA_START);
    }

    public void onStop() {

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