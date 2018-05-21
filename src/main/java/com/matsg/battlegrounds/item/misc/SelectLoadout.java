package com.matsg.battlegrounds.item.misc;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import com.matsg.battlegrounds.item.BattleItem;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SelectLoadout extends BattleItem {

    public SelectLoadout(Game game, GamePlayer gamePlayer) {
        super("SelectLoadout", null);
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.itemSlot = ItemSlot.MISCELLANEOUS;
        this.itemStack = getDefaultItemStack();

        game.getItemRegistry().addItem(this);
    }

    private static ItemStack getDefaultItemStack() {
        return new ItemStackBuilder(Material.COMPASS).setDisplayName(EnumMessage.CHANGE_LOADOUT.getMessage()).build();
    }

    public void onLeftClick() {
        openLoadoutView();
    }

    public void onRightClick() {
        openLoadoutView();
    }

    private void openLoadoutView() {
        gamePlayer.getPlayer().openInventory(new SelectLoadoutView(plugin, game, gamePlayer).getInventory());
    }

    public boolean update() {
        Inventory inventory = gamePlayer.getPlayer().getInventory();
        inventory.setItem(itemSlot.getSlot(), itemStack);
        return inventory.contains(itemStack);
    }
}