package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.item.WeaponType;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WeaponTypeView implements View {

    private Inventory inventory;

    public WeaponTypeView(Plugin plugin, String title, List<Weapon> weapons) {
        this.inventory = plugin.getServer().createInventory(this, 27, title);

        Collections.sort(weapons, new Comparator<Weapon>() {
            public int compare(Weapon o1, Weapon o2){
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Weapon weapon : weapons) {
            weapon.update();
            inventory.addItem(weapon.getItemStack().clone());
        }

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.BARRIER, 1)).setDisplayName("§fGo back").build());
    }

    public WeaponTypeView(Plugin plugin, WeaponType weaponType, List<Weapon> weapons) {
        this(plugin, weaponType.getName() + "s", weapons);
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (itemStack.getType() == Material.BARRIER) {
            player.openInventory(WeaponsView.getInstance().getInventory());
        }
    }

    public boolean onClose() {
        return true;
    }

    public Inventory getInventory() {
        return inventory;
    }
}