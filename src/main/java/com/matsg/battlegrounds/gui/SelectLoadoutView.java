package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.ActionBar;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SelectLoadoutView implements View {

    private Battlegrounds plugin;
    private Game game;
    private GamePlayer gamePlayer;
    private Inventory inventory;
    private List<SelectLoadoutViewItem> items;
    private MessageHelper messageHelper;

    public SelectLoadoutView(Battlegrounds plugin, Game game, GamePlayer gamePlayer) {
        this.plugin = plugin;
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.items = new ArrayList<>();
        this.messageHelper = new MessageHelper();
        this.inventory = plugin.getServer().createInventory(this, 27, messageHelper.create(TranslationKey.VIEW_SELECT_LOADOUT));

        for (Loadout loadout : plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getLoadouts()) {
            addLoadout(loadout);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void addLoadout(Loadout loadout) {
        int levelUnlocked = plugin.getLevelConfig().getLevelUnlocked(loadout.getName());
        boolean locked = levelUnlocked > plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getExp());

        String displayName = locked ? messageHelper.create(TranslationKey.ITEM_LOCKED, new Placeholder("bg_level", plugin.getLevelConfig().getLevelUnlocked(loadout.getName()))) : ChatColor.WHITE + loadout.getName();

        ItemStack itemStack = new ItemStackBuilder(locked ? new ItemStack(Material.BARRIER) : getLoadoutItemStack(loadout))
                .addItemFlags(ItemFlag.values())
                .setAmount(loadout.getLoadoutNr())
                .setDisplayName(displayName)
                .setLore(new String[0])
                .setUnbreakable(true)
                .build();

        inventory.setItem(loadout.getLoadoutNr() + 10, itemStack);
        items.add(new SelectLoadoutViewItem(inventory.getItem(loadout.getLoadoutNr() + 10), loadout, locked));
    }

    private ItemStack getLoadoutItemStack(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null && weapon.getItemStack() != null) {
                return weapon.getItemStack();
            }
        }
        return new ItemStack(Material.BARRIER);
    }

    private SelectLoadoutViewItem getViewItem(ItemStack itemStack) {
        for (SelectLoadoutViewItem item : items) {
            if (item.getItemStack().equals(itemStack)) {
                return item;
            }
        }
        return null;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        SelectLoadoutViewItem item = getViewItem(itemStack);
        if (gamePlayer == null || itemStack == null || item == null || item.getLoadout() == null || item.isLocked() || !game.getState().isInProgress()) {
            return;
        }
        Loadout loadout = item.getLoadout();
        if (loadout.equals(gamePlayer.getLoadout())) {
            ActionBar.SAME_LOADOUT.send(player);
            player.closeInventory();
            return;
        }
        game.getPlayerManager().changeLoadout(gamePlayer, loadout.clone(), gamePlayer.getLoadout() == null || game.getTimeControl().getTime() <= 10);
        gamePlayer.setSelectedLoadout(loadout);
        player.closeInventory();
    }

    public boolean onClose() {
        return gamePlayer.getLoadout() != null || !game.getState().isInProgress();
    }

    public class SelectLoadoutViewItem {

        private boolean locked;
        private ItemStack itemStack;
        private Loadout loadout;

        public SelectLoadoutViewItem(ItemStack itemStack, Loadout loadout, boolean locked) {
            this.itemStack = itemStack;
            this.loadout = loadout;
            this.locked = locked;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public Loadout getLoadout() {
            return loadout;
        }

        public boolean isLocked() {
            return locked;
        }
    }
}
