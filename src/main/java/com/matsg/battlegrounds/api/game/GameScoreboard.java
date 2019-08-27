package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;
import java.util.Set;

public interface GameScoreboard {

    /**
     * Gets the layout of the scoreboard.
     *
     * @return the scoreboard layout
     */
    Map<String, String> getLayout();

    /**
     * Sets the layout of the scoreboard.
     *
     * @param layout the scoreboard layout
     */
    void setLayout(Map<String, String> layout);

    /**
     * Gets the id of the scoreboard.
     *
     * @return the scoreboard id
     */
    String getScoreboardId();

    /**
     * Gets the collection of worlds the scoreboard can be displayed in.
     *
     * @return the scoreboard worlds
     */
    Set<World> getWorlds();

    /**
     * Creates the scoreboard and returns a bukkit scoreboard instance.
     *
     * @return the built scoreboard
     */
    Scoreboard createScoreboard();

    /**
     * Displays the scoreboard in a game.
     *
     * @param game the game to display the scoreboard in
     */
    void display(Game game);

    /**
     * Displays the scoreboard to an individual player.
     *
     * @param gamePlayer the player to display the scoreboard to
     */
    void display(GamePlayer gamePlayer);
}
