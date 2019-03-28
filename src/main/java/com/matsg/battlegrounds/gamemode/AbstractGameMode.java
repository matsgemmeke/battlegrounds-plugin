package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.gamemode.GameMode;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.entity.PlayerStatus;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractGameMode implements GameMode, Listener {

    protected Battlegrounds plugin;
    protected boolean active;
    protected Game game;
    protected int timeLimit;
    protected List<Objective> objectives;
    protected List<Team> teams;
    protected MessageHelper messageHelper;
    protected String name, shortName;
    protected Yaml yaml;

    public AbstractGameMode(Battlegrounds plugin, Game game, Yaml yaml) {
        this.plugin = plugin;
        this.game = game;
        this.yaml = yaml;
        this.active = false;
        this.objectives = new ArrayList<>();
        this.messageHelper = new MessageHelper();
        this.teams = new ArrayList<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Yaml getConfig() {
        return yaml;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Iterable<Team> getTeams() {
        return teams;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    protected Objective getReachedObjective() {
        for (Objective objective : objectives) {
            if (objective.isReached(game)) {
                return objective;
            }
        }
        return null;
    }

    protected TranslationKey getKillMessageKey(Hitbox hitbox) {
        switch (hitbox) {
            case HEAD:
                return TranslationKey.DEATH_HEADSHOT;
            case LEG:
                return TranslationKey.DEATH_PLAYER_KILL;
            case TORSO:
                return TranslationKey.DEATH_PLAYER_KILL;
            default:
                return null;
        }
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

    public void handleDeath(GamePlayer gamePlayer) {
        gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
        gamePlayer.setLives(gamePlayer.getLives() - 1);
        if (gamePlayer.getLives() <= 0) {
            gamePlayer.setStatus(PlayerStatus.SPECTATING).apply(game, gamePlayer);
        }
        Weapon weapon = gamePlayer.getLoadout().getWeapon(gamePlayer.getPlayer().getItemInHand());

        if (weapon != null && weapon instanceof Firearm) {
            Location location = gamePlayer.getLocation();
            Item item = location.getWorld().dropItem(location, weapon.getItemStack());
            ((Firearm) weapon).onDrop(gamePlayer, item);
        }
    }

    public void onDisable() { }

    public void onEnable() { }

    public void onStateChange(GameState state) {
        switch (state) {
            case IN_GAME:
                onStart();
                break;
            case RESETTING:
                onStop();
                break;
        }
    }

    public void onStart() {
        for (Spawn spawn : game.getArena().getSpawns()) {
            spawn.setGamePlayer(null);
        }
    }

    public void onStop() { }

    public void tick() {
        GameScoreboard scoreboard = getScoreboard();

        if (scoreboard != null) {
            scoreboard.display(game);
        }

        for (Objective objective : objectives) {
            if (objective.isReached(game)) {
                game.callEvent(new GameEndEvent(game, objective, null, getSortedTeams()));
                game.stop();
                break;
            }
        }
    }
}
