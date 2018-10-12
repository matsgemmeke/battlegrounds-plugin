package com.matsg.battlegrounds.game;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BattleTeam.class)
public class GameCountdownTest {

    @Test
    public void testCancelCountdown() {
        // Create dependency mocks
        Game game = mock(Game.class);
        GamePlayer gamePlayer = mock(GamePlayer.class);
        Player player = mock(Player.class);
        PlayerManager playerManager = mock(PlayerManager.class);

        List<GamePlayer> players = new ArrayList<>();
        players.add(gamePlayer);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(gamePlayer.getPlayer()).thenReturn(player);
        when(player.openInventory(any(Inventory.class))).thenReturn(null);
        when(playerManager.getPlayers()).thenReturn(players);

        GameCountdown countdown = new GameCountdown(game, 1);
        countdown.run();

        verify(player, only()).openInventory(any(Inventory.class));
    }
}
