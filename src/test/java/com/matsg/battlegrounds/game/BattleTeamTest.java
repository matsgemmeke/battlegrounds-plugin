package com.matsg.battlegrounds.game;

import static org.junit.Assert.*;

import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class BattleTeamTest {

    @Test
    public void testTeam() {
        int id = 1;
        ChatColor chatColor = ChatColor.WHITE;
        Color color = Color.AQUA;
        List<GamePlayer> list = new ArrayList<>();
        String name = "Test";

        BattleTeam team = new BattleTeam(id, name, color, chatColor);

        assertEquals(id, team.getId());
        assertEquals(name, team.getName());
        assertEquals(color, team.getColor());
        assertEquals(chatColor, team.getChatColor());

        assertEquals(list, team.getPlayers());
        assertEquals(0, team.getScore());
        assertNotNull(team.getPlayers());

        int score = 100;

        team.setScore(score);

        assertEquals(score, team.getScore());
    }
}
