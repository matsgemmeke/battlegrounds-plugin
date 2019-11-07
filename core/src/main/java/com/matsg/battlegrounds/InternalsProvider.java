package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Contains miscellaneous functions of whose executions depend on the server version.
 */
public interface InternalsProvider {

    /**
     * Makes a hellhound implementation of a game.
     *
     * @param game the game
     * @param plugin the plugin
     * @return a hellhound implementation
     */
    Hellhound makeHellhound(Game game, Plugin plugin);

    /**
     * Makes a zombie implementation of a game.
     *
     * @param game the game
     * @param plugin the plugin
     * @return a zombie implementation
     */
    Zombie makeZombie(Game game, Plugin plugin);

    /**
     * Plays a chest opening animation.
     *
     * @param location the location of the chest
     * @param open whether to play the opening or closing animation
     */
    void playChestAnimation(Location location, boolean open);

    /**
     * Registers the custom entities for the version.
     */
    void registerCustomEntities();

    /**
     * Sends an action bar message to a player.
     *
     * @param player the player to send the action bar to
     * @param message the message
     */
    void sendActionBar(Player player, String message);

    /**
     * Sends a JSON message to a player.
     *
     * @param player the player to send the JSON message to
     * @param message the message
     * @param command the command that gets put into the command line when the message is clicked
     * @param hoverMessage the message that appear when hovering over the message
     */
    void sendJSONMessage(Player player, String message, String command, String hoverMessage);

    /**
     * Sends a title to a player
     *
     * @param player the player to send the title to
     * @param title the message of the title
     * @param subTitle the message of the subtitle
     * @param fadeIn the duration of the fade-in in ticks
     * @param time the duration of the title in ticks
     * @param fadeOut the duration of the fade-out in ticks
     */
    void sendTitle(Player player, String title, String subTitle, int fadeIn, int time, int fadeOut);

    /**
     * Spawns a colored particle in a certain location.
     *
     * @param location the location
     * @param effect the effect type
     * @param red the red color value
     * @param green the green color value
     * @param blue the blue color value
     */
    void spawnColoredParticle(Location location, String effect, float red, float green, float blue);

    /**
     * Spawns a default particle in a certain location.
     *
     * @param location the location
     * @param effect the effect type
     * @param amount the amount of particle
     * @param offsetX the maximum x offset
     * @param offsetY the maximum y offset
     * @param offsetZ the maximum z offset
     * @param speed the speed of the particle
     */
    void spawnParticle(Location location, String effect, int amount, float offsetX, float offsetY, float offsetZ, int speed);
}
