package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.game.mode.AbstractGameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.item.ItemFinder;
import org.bukkit.ChatColor;

public class Zombies extends AbstractGameMode {

    private ComponentContainer<Section> sectionContainer;
    private Team team;

    public Zombies(Battlegrounds plugin, Game game, Yaml config) {
        super(plugin, game, config);
        this.sectionContainer = new SectionContainer();
        this.team = new BattleTeam(1, "Players", null, ChatColor.WHITE);

        setName(messageHelper.create(TranslationKey.ZOMBIES_NAME));
        setShortName(messageHelper.create(TranslationKey.ZOMBIES_SHORT));

        teams.add(team);
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
        ZombiesDataLoader dataLoader = new ZombiesDataLoader(this, game, arena, plugin.getBattlegroundsConfig(), itemFinder);
        dataLoader.load();
    }

    public void onDeath(GamePlayer gamePlayer, GamePlayerDeathEvent.DeathCause deathCause) {

    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {

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
