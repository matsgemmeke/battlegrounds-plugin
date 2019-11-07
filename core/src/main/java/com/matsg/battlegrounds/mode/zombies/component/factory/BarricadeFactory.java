package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.mode.zombies.component.Barricade;
import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesBarricade;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BarricadeFactory {

    private InternalsProvider internals;
    private Translator translator;

    public BarricadeFactory(InternalsProvider internals, Translator translator) {
        this.internals = internals;
        this.translator = translator;
    }

    /**
     * Creates a barricade component based on the given input.
     *
     * @param id the component id
     * @param mobSpawn the mob spawn the barricade is connected to
     * @param maximumPoint the maximum location point of the barricade
     * @param minimumPoint the minimum location point of the barricade
     * @param world the world of the barricade
     * @param material the material of the barricade
     * @return a barricade implementation
     */
    public Barricade make(
            int id,
            MobSpawn mobSpawn,
            Location maximumPoint,
            Location minimumPoint,
            World world,
            Material material
    ) {
        return new ZombiesBarricade(id, mobSpawn, maximumPoint, minimumPoint, world, material, internals, translator);
    }
}
