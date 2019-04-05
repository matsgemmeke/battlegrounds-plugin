package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameScoreboard;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.game.mode.AbstractGameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import org.bukkit.ChatColor;

public class Zombies extends AbstractGameMode {

    private Team team;

    public Zombies(Battlegrounds plugin, Game game, Yaml config) {
        super(plugin, game, config);
        this.team = new BattleTeam(1, "Players", null, ChatColor.WHITE);

        setName(messageHelper.create(TranslationKey.ZOMBIES_NAME));
        setShortName(messageHelper.create(TranslationKey.ZOMBIES_SHORT));

        teams.add(team);
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
