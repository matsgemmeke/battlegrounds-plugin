package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.junit.Test;

import static org.junit.Assert.*;

public class BattleTeamTest {

    @Test
    public void createBasicTeam() {
        int id = 1;
        int score = 100;
        ChatColor chatColor = ChatColor.WHITE;
        Color color = Color.AQUA;
        String name = "Test";

        BattleTeam team = new BattleTeam(id, name, color, chatColor);
        team.setScore(score);

        assertEquals(id, team.getId());
        assertEquals(name, team.getName());
        assertEquals(color, team.getArmorColor());
        assertEquals(chatColor, team.getChatColor());
        assertArrayEquals(new GamePlayer[0], team.getPlayers());
        assertNotNull(team.getPlayers());
        assertEquals(score, team.getScore());
    }

    @Test
    public void addPlayerToTeam() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);

        BattleTeam team = new BattleTeam(1, null, null, null);
        team.addPlayer(gamePlayer);

        assertEquals(1, team.getPlayers().length);
        assertEquals(gamePlayer, team.getPlayers()[0]);
        assertEquals(team, gamePlayer.getTeam());
    }

    @Test
    public void addPlayerWhoIsAlreadyInTheTeam() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);

        BattleTeam team = new BattleTeam(1, null, null, null);
        team.addPlayer(gamePlayer);
        team.addPlayer(gamePlayer);

        assertEquals(1, team.getPlayers().length);
        assertEquals(gamePlayer, team.getPlayers()[0]);
        assertEquals(team, gamePlayer.getTeam());
    }

    @Test
    public void getWhetherPlayerIsInTeam() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);

        BattleTeam team = new BattleTeam(1, null, null, null);
        team.addPlayer(gamePlayer);
        boolean hasPlayer = team.hasPlayer(gamePlayer);

        assertTrue(hasPlayer);
    }

    @Test
    public void removePlayerFromTeam() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);

        BattleTeam team = new BattleTeam(1, null, null, null);
        team.addPlayer(gamePlayer);
        team.removePlayer(gamePlayer);

        assertEquals(0, team.getPlayers().length);
        assertNull(gamePlayer.getTeam());
    }

    @Test
    public void removePlayerWhoIsNotInTheTeam() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);

        BattleTeam team = new BattleTeam(1, null, null, null);
        team.removePlayer(gamePlayer);

        assertEquals(0, team.getPlayers().length);
    }

    @Test
    public void getAmountOfPlayersInTeam() {
        BattleTeam team = new BattleTeam(1, null, null, null);
        team.addPlayer(new BattleGamePlayer(null, null));

        assertEquals(1, team.getTeamSize());
    }

    @Test
    public void getTotalAmountOfKillsInTeam() {
        GamePlayer gamePlayerOne = new BattleGamePlayer(null, null);
        GamePlayer gamePlayerTwo = new BattleGamePlayer(null, null);

        gamePlayerOne.setKills(10);
        gamePlayerTwo.setKills(5);

        BattleTeam team = new BattleTeam(1, null, null, null);
        team.addPlayer(gamePlayerOne);
        team.addPlayer(gamePlayerTwo);

        assertEquals(15, team.getKills());
    }

    @Test
    public void findLivingPlayersInTeam() {
        GamePlayer gamePlayerOne = new BattleGamePlayer(null, null);
        GamePlayer gamePlayerTwo = new BattleGamePlayer(null, null);

        gamePlayerOne.setState(PlayerState.ACTIVE);
        gamePlayerTwo.setState(PlayerState.SPECTATING);

        BattleTeam team = new BattleTeam(1, null, null, null);
        team.addPlayer(gamePlayerOne);
        team.addPlayer(gamePlayerTwo);
        GamePlayer[] players = team.getLivingPlayers();

        assertEquals(1, players.length);
        assertEquals(gamePlayerOne, players[0]);
    }
}
