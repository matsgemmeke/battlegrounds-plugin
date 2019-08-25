package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.mode.zombies.component.WallWeapon;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesWallWeapon;
import org.bukkit.block.Chest;

public class WallWeaponFactory {

    private Game game;
    private Translator translator;

    public WallWeaponFactory(Game game, Translator translator) {
        this.game = game;
        this.translator = translator;
    }

    /**
     * Creates a wall weapon component based on the given input.
     *
     * @param id the component id
     * @param chest the chest of the wall weapon
     * @param weapon the weapon the wall weapon sells
     * @param price the wall weapon price
     * @return a wall weapon implementation
     */
    public WallWeapon make(
            int id,
            Chest chest,
            Weapon weapon,
            int price
    ) {
        return new ZombiesWallWeapon(id, game, chest, weapon, translator, price);
    }
}
