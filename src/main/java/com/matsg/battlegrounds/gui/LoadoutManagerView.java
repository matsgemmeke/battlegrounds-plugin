package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.config.BattlePlayerYaml;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoadoutManagerView implements View {

    private Battlegrounds plugin;
    private Inventory inventory;
    private Map<ItemStack, Loadout> loadouts;

    public LoadoutManagerView(Battlegrounds plugin, Player player) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, EnumMessage.TITLE_LOADOUT_MANAGER.getMessage());
        this.loadouts = new HashMap<>();

        try {
            int i = 0;
            for (Loadout loadout : new BattlePlayerYaml(plugin, player.getUniqueId()).getLoadouts()) {
                ItemStack itemStack = new ItemStackBuilder(getLoadoutItemStack(loadout).clone())
                        .addItemFlags(ItemFlag.values())
                        .setAmount(++ i)
                        .setDisplayName(ChatColor.WHITE + loadout.getName())
                        .setLore(EnumMessage.EDIT_LOADOUT.getMessage())
                        .setUnbreakable(true)
                        .build();

                inventory.setItem(i + 10, itemStack);
                loadouts.put(inventory.getItem(i + 10), loadout);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack getLoadoutItemStack(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null && weapon.getItemStack() != null) {
                return weapon.getItemStack();
            }
        }
        return null;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        Loadout loadout = loadouts.get(itemStack);
        if (loadout == null) {
            return;
        }
        player.openInventory(new EditLoadoutView(plugin, loadout).getInventory());
    }

    public boolean onClose() {
        return true;
    }
}