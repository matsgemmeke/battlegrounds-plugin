package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.ItemNotFoundException;
import com.matsg.battlegrounds.api.Translator;
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

    private InternalsProvider internals;
    private ItemFinder itemFinder;
    private Translator translator;
    private ZombiesConfig config;

    public MysteryBoxFactory(InternalsProvider internals, ItemFinder itemFinder, Translator translator, ZombiesConfig config) {
        this.internals = internals;
        this.itemFinder = itemFinder;
        this.translator = translator;
        this.config = config;
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
            Weapon weapon;

            try {
                weapon = itemFinder.findWeapon(weaponId);
            } catch (ItemNotFoundException e) {
                continue;
            }

            weapon.setDroppable(false);

            weaponList.add(weapon);
        }

        Weapon[] weapons = weaponList.toArray(new Weapon[weaponList.size()]);

        MysteryBox mysteryBox = new ZombiesMysteryBox(id, blocks, weapons, price, internals, translator);
        mysteryBox.setState(new InactiveState(mysteryBox));

        return mysteryBox;
    }
}
