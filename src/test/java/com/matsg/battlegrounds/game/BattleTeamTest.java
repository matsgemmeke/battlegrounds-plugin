package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.junit.Test;

import static org.junit.Assert.*;

public class BattleTeamTest {

    @Test
    public void testTeam() {
        int id = 1;
        ChatColor chatColor = ChatColor.WHITE;
        Color color = Color.AQUA;
        String name = "Test";

        BattleTeam team = new BattleTeam(id, name, color, chatColor);

        assertEquals(id, team.getId());
        assertEquals(name, team.getName());
        assertEquals(color, team.getColor());
        assertEquals(chatColor, team.getChatColor());

        assertArrayEquals(new GamePlayer[0], team.getPlayers());
        assertEquals(0, team.getScore());
        assertNotNull(team.getPlayers());

        int score = 100;

        team.setScore(score);

        assertEquals(score, team.getScore());
    }
}
