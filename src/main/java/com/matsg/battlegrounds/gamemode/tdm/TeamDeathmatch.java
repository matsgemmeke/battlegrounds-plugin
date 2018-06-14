package com.matsg.battlegrounds.gamemode.tdm;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.gamemode.AbstractGameMode;
import com.matsg.battlegrounds.gamemode.Result;
import com.matsg.battlegrounds.api.game.GameScoreboard;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class TeamDeathmatch extends AbstractGameMode {

    private boolean scoreboardEnabled;
    private double minSpawnDistance;
    private int killsToWin;

    public TeamDeathmatch(Game game, Yaml yaml) {
        super(game, EnumMessage.TDM_NAME.getMessage(), EnumMessage.TDM_SHORT.getMessage(), yaml);
        this.killsToWin = yaml.getInt("kills-to-win");
        this.minSpawnDistance = yaml.getDouble("minimum-spawn-distance");
        this.scoreboardEnabled = yaml.getBoolean("scoreboard.enabled");
        this.teams.addAll(getConfigTeams());
        this.timeLimit = yaml.getInt("time-limit");
    }

    public void addPlayer(GamePlayer gamePlayer) {
        Team team = getEmptiestTeam();
        team.addPlayer(gamePlayer);
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
            if (team.getTotalPlayers() < size) {
                emptiestTeam = team;
                size = team.getTotalPlayers();
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

    private Spawn getTeamBase(Team team) {
        for (Spawn spawn : game.getArena().getSpawns()) {
            if (spawn.getTeamId() == team.getId() && spawn.isTeamBase()) {
                return spawn;
            }
        }
        return null;
    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {
        game.broadcastMessage(getKillMessage(hitbox).getMessage(new Placeholder[] {
                new Placeholder("bg_killer", getTeam(killer).getChatColor() + killer.getName() + ChatColor.WHITE),
                new Placeholder("bg_player", getTeam(gamePlayer).getChatColor() + gamePlayer.getName() + ChatColor.WHITE),
                new Placeholder("bg_weapon", weapon.getName())
        }));
        gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
        killer.addScore(100);
        killer.setKills(killer.getKills() + 1);
        killer.getTeam().setScore(killer.getTeam().getScore() + 1);

        if (killer.getKills() > killsToWin) {
            game.stop();
        }
    }

    public void onStart() {
        super.onStart();
        game.broadcastMessage(EnumTitle.TDM_START);
    }

    public void onStop() {
        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    gamePlayer.sendMessage(new Title(result.getTitle(), EnumMessage.ENDREASON_SCORE.getMessage(), 20, 160, 20));
                }
            }
        }
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Team team = getTeam(gamePlayer);
        if (team == null) {
            return;
        }
        team.removePlayer(gamePlayer);
    }

    public void spawnPlayers(GamePlayer... players) {
        for (GamePlayer gamePlayer : players) {
            gamePlayer.getPlayer().teleport(getTeamBase(getTeam(gamePlayer)).getLocation());
        }
    }
}