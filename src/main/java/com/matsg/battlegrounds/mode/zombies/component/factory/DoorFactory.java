package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Door;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesDoor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class DoorFactory {

    private Game game;

    public DoorFactory(Game game) {
        this.game = game;
    }

    /**
     * Creates a door component based on the given input.
     *
     * @param id the component id
     * @param section the section the door guards
     * @param world the world the door is located in
     * @param maximumPoint the maximum location point of the door
     * @param minimumPoint the minimum location point of the door
     * @param material the material of the door
     * @return a door implementation
     */
    public Door make(
            int id,
            Section section,
            World world,
            Location maximumPoint,
            Location minimumPoint,
            Material material
    ) {
        return new ZombiesDoor(id, game, section, world, maximumPoint, minimumPoint, material);
    }
}
