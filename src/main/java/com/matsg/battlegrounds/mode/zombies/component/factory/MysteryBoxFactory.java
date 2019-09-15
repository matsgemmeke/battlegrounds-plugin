package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.ItemFinder;
import com.matsg.battlegrounds.mode.zombies.ZombiesConfig;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesMysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.mysterybox.InactiveState;
import com.matsg.battlegrounds.util.Pair;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MysteryBoxFactory {

    private ItemFinder itemFinder;
    private Version version;
    private ZombiesConfig config;

    public MysteryBoxFactory(ItemFinder itemFinder, ZombiesConfig config, Version version) {
        this.itemFinder = itemFinder;
        this.config = config;
        this.version = version;
    }

    /**
     * Creates a mystery box component based on the given input.
     *
     * @param id the component id
     * @param blocks the mystery box blocks
     * @param price the price of the mystery box
     * @return a mystery box implementation
     */
    public MysteryBox make(int id, Pair<Block, Block> blocks, int price) {
        List<Weapon> weaponList = new ArrayList<>();

        for (String weaponId : config.getMysteryBoxWeapons()) {
            weaponList.add(itemFinder.findWeapon(weaponId));
        }

        Weapon[] weapons = weaponList.toArray(new Weapon[weaponList.size()]);

        MysteryBox mysteryBox = new ZombiesMysteryBox(id, blocks, weapons, price, version);
        mysteryBox.setState(new InactiveState(mysteryBox));

        return mysteryBox;
    }
}
