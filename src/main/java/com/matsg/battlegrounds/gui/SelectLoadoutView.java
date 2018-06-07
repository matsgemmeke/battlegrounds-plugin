package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Loadout;
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

public class SelectLoadoutView implements View {

    private Game game;
    private GamePlayer gamePlayer;
    private Inventory inventory;
    private Map<ItemStack, Loadout> loadouts;

    public SelectLoadoutView(Battlegrounds plugin, Game game, GamePlayer gamePlayer) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.inventory = plugin.getServer().createInventory(this, 27, EnumMessage.TITLE_SELECT_LOADOUT.getMessage());
        this.loadouts = new HashMap<>();

        int i = 0;
        for (Loadout loadout : plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getPlayerYaml().getLoadouts()) {
            ItemStack itemStack = new ItemStackBuilder(getLoadoutItemStack(loadout))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(++ i)
                    .setDisplayName(ChatColor.WHITE + loadout.getName())
                    .setUnbreakable(true)
                    .build();

            inventory.setItem(i + 10, itemStack);
            loadouts.put(itemStack, loadout);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack getLoadoutItemStack(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon.getItemStack() != null) {
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
        game.getPlayerManager().changeLoadout(gamePlayer, loadout, gamePlayer.getLoadout() == null || game.getTimeControl().getTime() <= 10);
        player.closeInventory();
    }

    public boolean onClose() {
        return gamePlayer.getLoadout() != null;
    }
}
