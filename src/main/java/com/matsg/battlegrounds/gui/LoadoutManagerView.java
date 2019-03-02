package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LoadoutManagerView implements View {

    private Battlegrounds plugin;
    private Inventory inventory;
    private Map<ItemStack, Loadout> loadouts;
    private MessageHelper messageHelper;

    public LoadoutManagerView(Battlegrounds plugin, Player player) {
        this.plugin = plugin;
        this.loadouts = new HashMap<>();
        this.messageHelper = new MessageHelper();
        this.inventory = plugin.getServer().createInventory(this, 27, messageHelper.create(TranslationKey.VIEW_LOADOUT_MANAGER));

        int i = 0;
        for (Loadout loadout : plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getLoadouts()) {
            ItemStack itemStack = new ItemStackBuilder(getLoadoutItemStack(loadout))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(++ i)
                    .setDisplayName(ChatColor.WHITE + loadout.getName())
                    .setLore(messageHelper.create(TranslationKey.EDIT_LOADOUT))
                    .setUnbreakable(true)
                    .build();

            inventory.setItem(i + 10, itemStack);
            loadouts.put(inventory.getItem(i + 10), loadout);
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
        return new ItemStack(Material.BARRIER);
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