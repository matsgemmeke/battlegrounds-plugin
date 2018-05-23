package com.matsg.battlegrounds.gamemode.tdm;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.gamemode.AbstractGameMode;
import com.matsg.battlegrounds.gui.scoreboard.GameScoreboard;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TeamDeathmatch extends AbstractGameMode {

    private boolean scoreboardEnabled;
    private double minSpawnDistance;

    public TeamDeathmatch(Game game, Yaml yaml) {
        super(game, EnumMessage.TDM_NAME.getMessage(), EnumMessage.TDM_SHORT.getMessage(), yaml);
        this.minSpawnDistance = yaml.getDouble("minimum-spawn-distance");
        this.scoreboardEnabled = yaml.getBoolean("scoreboard.enabled");
        this.teams.addAll(getConfigTeams());
    }

    public void addPlayer(GamePlayer gamePlayer) {
        Team team = getEmptiestTeam();
        team.getPlayers().add(gamePlayer);
        gamePlayer.sendMessage(EnumMessage.TEAM_ASSIGNMENT.getMessage(new Placeholder("bg_team", team.getChatColor() + team.getName())));
    }

    private List<Team> getConfigTeams() {
        List<Team> list = new ArrayList<>();
        for (String teamId : yaml.getConfigurationSection("teams").getKeys(false)) {
            ConfigurationSection section = yaml.getConfigurationSection("teams." + teamId);
            String[] array = section.getString("armor-color").split(",");
            Color color = Color.fromRGB(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));

            list.add(new BattleTeam(Integer.parseInt(teamId), section.getString("name"), color, ChatColor.getByChar(section.getString("chatcolor").charAt(0))));
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

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return game.getArena().getRandomSpawn(getTeam(gamePlayer), minSpawnDistance);
    }

    public GameScoreboard getScoreboard() {
        return scoreboardEnabled ? new TDMScoreboard(game, yaml) : null;
    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon) {
        game.broadcastMessage(EnumMessage.DEATH_PLAYER_KILL.getMessage(new Placeholder[] {
                new Placeholder("bg_killer", getTeam(killer).getChatColor() + killer.getName() + ChatColor.WHITE),
                new Placeholder("bg_player", getTeam(gamePlayer).getChatColor() + gamePlayer.getName() + ChatColor.WHITE),
                new Placeholder("bg_weapon", weapon.getName())
        }));
    }

    public void onStart() {
        super.onStart();
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
                    spawn.setGamePlayer(gamePlayer);
                    break;
                }
            }
        }
    }
}