package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.SavedInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BattleSavedInventory implements SavedInventory {

    private final float exp;
    private final int level;
    private final ItemStack[] armor, items;

    public BattleSavedInventory(Player player) {
        this.armor = player.getInventory().getArmorContents();
        this.exp = player.getExp();
        this.items = player.getInventory().getContents();
        this.level = player.getLevel();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    public void restore(Player player) {
        player.getInventory().setArmorContents(armor);
        player.getInventory().setContents(items);
        player.setExp(exp);
        player.setLevel(level);
    }
}