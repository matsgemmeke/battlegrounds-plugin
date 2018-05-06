package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.LoadoutClass;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.config.BattlePlayerYaml;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClassManagerView implements View {

    private Inventory inventory;
    private Map<ItemStack, LoadoutClass> classes;

    public ClassManagerView(Battlegrounds plugin, Player player) {
        this.classes = new HashMap<>();
        this.inventory = plugin.getServer().createInventory(this, 27, EnumMessage.CLASS_MANAGER.getMessage());

        try {
            int i = 0;
            for (LoadoutClass loadoutClass : new BattlePlayerYaml(plugin, player.getUniqueId()).getLoadoutClasses()) {
                ItemStack itemStack = new ItemStackBuilder(getClassItemStack(loadoutClass))
                        .addItemFlags(ItemFlag.values())
                        .setAmount(++ i)
                        .setDisplayName("§f" + loadoutClass.getName())
                        .setLore(EnumMessage.EDIT_CLASS.getMessage())
                        .setUnbreakable(true)
                        .build();

                classes.put(itemStack, loadoutClass);
                inventory.setItem(i + 10, itemStack);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack getClassItemStack(LoadoutClass loadoutClass) {
        for (Weapon weapon : loadoutClass.getWeapons()) {
            if (weapon.getItemStack() != null) {
                return weapon.getItemStack();
            }
        }
        return null;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        System.out.print(classes.get(itemStack).getName());
    }

    public boolean onClose() {
        return true;
    }
}