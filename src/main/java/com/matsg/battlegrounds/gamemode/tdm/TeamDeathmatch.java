package com.matsg.battlegrounds.gamemode.tdm;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.gamemode.AbstractGameMode;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TeamDeathmatch extends AbstractGameMode {

    private PlayerManager playerManager;

    public TeamDeathmatch(Game game, Yaml yaml) {
        super(game, EnumMessage.TDM_NAME.getMessage(), EnumMessage.TDM_SHORT.getMessage(), yaml);
        this.playerManager = game.getPlayerManager();
        this.teams.addAll(getConfigTeams());
    }

    public void addPlayer(GamePlayer gamePlayer) {
        getEmptiestTeam().getPlayers().add(gamePlayer);
    }

    private List<Team> getConfigTeams() {
        List<Team> list = new ArrayList<>();
        for (String team : yaml.getConfigurationSection("teams").getKeys(false)) {
            ConfigurationSection section = yaml.getConfigurationSection("teams." + team);

            list.add(new BattleTeam(section.getString("name"), section.getColor("color")));
        }
        return list;
    }

    private Team getEmptiestTeam() {
        int size = Integer.MAX_VALUE;
        Team emptiestTeam = null;
        for (Team team : teams) {
            if (team.getPlayers().size() < size) {
                emptiestTeam = team;
                size = team.getPlayers().size();
            }
        }
        return emptiestTeam;
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = playerManager.getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        team.getPlayers().remove(gamePlayer);
    }

    public void spawnPlayers(GamePlayer... players) {
        for (GamePlayer gamePlayer : players) {
            Team team = playerManager.getTeam(gamePlayer);
            for (Spawn spawn : game.getArena().getSpawns()) {
                if (spawn.getTeam() == team && !spawn.isOccupied()) {
                    gamePlayer.getPlayer().teleport(spawn.getLocation());
                    break;
                }
            }
        }
    }
}