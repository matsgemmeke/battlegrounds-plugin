package com.matsg.battlegrounds.game.mode;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.game.mode.shared.SpawningResult;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * General abstract gamemode class containing functions that apply for all
 * gamemodes by default.
 */
public abstract class AbstractGameMode implements GameMode {

    protected Battlegrounds plugin;
    protected boolean active;
    protected Game game;
    protected List<ArenaComponent> components;
    protected List<Objective> objectives;
    protected List<Team> teams;
    protected String name, shortName;
    protected Translator translator;
    private SpawningBehavior spawningBehavior;

    public AbstractGameMode(Battlegrounds plugin, Game game, Translator translator, SpawningBehavior spawningBehavior) {
        this.plugin = plugin;
        this.game = game;
        this.translator = translator;
        this.spawningBehavior = spawningBehavior;
        this.active = false;
        this.components = new ArrayList<>();
        this.objectives = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public String getShortName() {
        return shortName;
    }

    public Iterable<Team> getTeams() {
        return teams;
    }

    public void addObjective(Objective objective) {
        objectives.add(objective);
    }

    protected Objective getAchievedObjective() {
        for (Objective objective : objectives) {
            if (objective.isAchieved()) {
                return objective;
            }
        }
        return null;
    }

    protected List<Team> getSortedTeams() {
        List<Team> list = new ArrayList<>();
        list.addAll(teams);

        Collections.sort(list, new Comparator<Team>() {
            public int compare(Team o1, Team o2) {
                return ((Integer) o2.getScore()).compareTo(o1.getScore()); // Reverse sort
            }
        });

        return list;
    }

    public Team getTeam(GamePlayer gamePlayer) {
        for (Team team : teams) {
            if (team.hasPlayer(gamePlayer)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeam(int id) {
        for (Team team : teams) {
            if (team.getId() == id) {
                return team;
            }
        }
        return null;
    }

    public Team getTopTeam() {
        return getSortedTeams().get(0);
    }

    public void loadData(Arena arena) { }

    public void onDisable() { }

    public void onEnable() { }

    public void preparePlayer(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        player.setFoodLevel(20);
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.setHealth(20.0);
        player.setSaturation((float) 10);
    }

    public boolean removeComponent(ArenaComponent component) {
        return false;
    }

    public boolean spawnPlayers(Iterable<GamePlayer> players) {
        SpawningResult result = spawningBehavior.spawnPlayers(players);

        if (!result.passed()) {
            plugin.getLogger().severe("Unable to spawn players in game " + game.getId() + ": " + result.getMessage());

            game.getPlayerManager().broadcastMessage(translator.translate(TranslationKey.ERROR_OCCURRED));
        }

        return result.passed();
    }

    public void start() {
        for (Spawn spawn : game.getArena().getSpawnContainer().getAll()) {
            spawn.setGamePlayer(null);
        }
    }
}
