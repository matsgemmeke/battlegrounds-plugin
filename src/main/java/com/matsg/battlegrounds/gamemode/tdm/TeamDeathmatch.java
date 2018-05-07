package com.matsg.battlegrounds.gamemode.tdm;

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
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TeamDeathmatch extends AbstractGameMode {

    public TeamDeathmatch(Game game, Yaml yaml) {
        super(game, EnumMessage.TDM_NAME.getMessage(), EnumMessage.TDM_SHORT.getMessage(), yaml);
        this.teams.addAll(getConfigTeams());
    }

    public void addPlayer(GamePlayer gamePlayer) {
        getEmptiestTeam().getPlayers().add(gamePlayer);
    }

    private List<Team> getConfigTeams() {
        List<Team> list = new ArrayList<>();
        for (String teamId : yaml.getConfigurationSection("teams").getKeys(false)) {
            ConfigurationSection section = yaml.getConfigurationSection("teams." + teamId);
            String[] array = section.getString("color").split(",");
            Color color = Color.fromRGB(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));

            list.add(new BattleTeam(Integer.parseInt(teamId), section.getString("name"), color));
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

    public void onStart() {
        game.broadcastMessage(Title.TDM_START);
    }

    public void onStop() {

    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        team.getPlayers().remove(gamePlayer);
    }

    public void spawnPlayers(GamePlayer... players) {
        for (GamePlayer gamePlayer : players) {
            Team team = getTeam(gamePlayer);
            for (Spawn spawn : game.getArena().getSpawns()) {
                if (spawn.getTeamId() == team.getId() && !spawn.isOccupied()) {
                    gamePlayer.getPlayer().teleport(spawn.getLocation());
                    break;
                }
            }
        }
    }
}