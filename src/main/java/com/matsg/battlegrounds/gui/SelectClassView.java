package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.LoadoutClass;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SelectClassView implements View {

    private Game game;
    private GamePlayer gamePlayer;
    private Inventory inventory;
    private Map<ItemStack, LoadoutClass> classes;

    public SelectClassView(Battlegrounds plugin, Game game, GamePlayer gamePlayer) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.classes = new HashMap<>();
        this.inventory = plugin.getServer().createInventory(this, 27, EnumMessage.SELECT_CLASS.getMessage());

        int i = 0;
        for (LoadoutClass loadoutClass : plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getPlayerYaml().getLoadoutClasses()) {
            ItemStack itemStack = new ItemStackBuilder(getClassItemStack(loadoutClass))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(++ i)
                    .setDisplayName(ChatColor.WHITE + loadoutClass.getName())
                    .setUnbreakable(true)
                    .build();

            classes.put(itemStack, loadoutClass);
            inventory.setItem(i + 10, itemStack);
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
        LoadoutClass loadoutClass = classes.get(itemStack);
        if (loadoutClass == null) {
            return;
        }
        game.getPlayerManager().changeLoadoutClass(gamePlayer, loadoutClass);
        player.closeInventory();
    }

    public boolean onClose() {
        return gamePlayer.getLoadoutClass() != null;
    }
}
