package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.game.BattleComponentContainer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.game.mode.AbstractGameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.item.ItemFinder;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Zombies extends AbstractGameMode {

    private ComponentContainer<Section> sectionContainer;
    private Team team;
    private ZombiesConfig config;

    public Zombies(Battlegrounds plugin, Game game, ZombiesConfig config) {
        super(plugin, game);
        this.config = config;
        this.sectionContainer = new BattleComponentContainer<>();
        this.team = new BattleTeam(1, "Players", null, ChatColor.WHITE);

        setName(messageHelper.create(TranslationKey.ZOMBIES_NAME));
        setShortName(messageHelper.create(TranslationKey.ZOMBIES_SHORT));

        teams.add(team);
    }

    public ZombiesConfig getConfig() {
        return config;
    }

    public ComponentContainer<Section> getSectionContainer() {
        return sectionContainer;
    }

    public GameModeType getType() {
        return GameModeType.ZOMBIES;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        team.addPlayer(gamePlayer);
    }

    public ArenaComponent getComponent(int id) {
        for (Section section : sectionContainer.getAll()) {
            if (section.getId() == id) {
                return section;
            }
            for (ArenaComponent component : section.getComponents()) {
                if (component.getId() == id) {
                    return component;
                }
            }
        }
        return null;
    }

    public int getComponentCount() {
        int count = sectionContainer.getAll().size();
        for (Section section : sectionContainer.getAll()) {
            count += section.getComponentCount();
        }
        return count;
    }

    public Collection<ArenaComponent> getComponents() {
        List<ArenaComponent> list = new ArrayList<>();
        list.addAll(sectionContainer.getAll());

        for (Section section : sectionContainer.getAll()) {
            list.addAll(section.getComponents());
        }

        return Collections.unmodifiableList(list);
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return null;
    }

    public GameScoreboard getScoreboard() {
        return null;
    }

    public Section getSection(String name) {
        for (Section section : sectionContainer.getAll()) {
            if (section.getName().equals(name)) {
                return section;
            }
        }
        return null;
    }

    public void loadData(Arena arena) {
        ItemFinder itemFinder = new ItemFinder(plugin);
        ZombiesDataLoader dataLoader = new ZombiesDataLoader(this, game, arena, itemFinder);
        dataLoader.load();
    }

    public void onDeath(GamePlayer gamePlayer, GamePlayerDeathEvent.DeathCause deathCause) {

    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {

    }

    public boolean removeComponent(ArenaComponent component) {
        int id = component.getId();
        if (sectionContainer.get(id) != null) {
            sectionContainer.remove(id);
            return true;
        }
        return false;
    }

    public void removePlayer(GamePlayer gamePlayer) {
        team.removePlayer(gamePlayer);
    }

    public void spawnPlayers(GamePlayer... players) {

    }

    public void start() {

    }

    public void stop() {

    }

    public void tick() {

    }
}
