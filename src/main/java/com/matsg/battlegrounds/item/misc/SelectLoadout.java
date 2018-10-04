package com.matsg.battlegrounds.item.misc;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import com.matsg.battlegrounds.item.BattleItem;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SelectLoadout extends BattleItem {

    public SelectLoadout(Game game) {
        super(null, "SelectLoadout", null);
        this.game = game;
        this.itemSlot = ItemSlot.MISCELLANEOUS;
        this.itemStack = getDefaultItemStack();
    }

    private static ItemStack getDefaultItemStack() {
        return new ItemStackBuilder(Material.COMPASS).setDisplayName(ChatColor.WHITE + EnumMessage.CHANGE_LOADOUT.getMessage()).build();
    }

    private void onClick(Player player) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        if (gamePlayer == null) {
            return;
        }
        player.openInventory(new SelectLoadoutView(game, gamePlayer).getInventory());
    }

    public void onLeftClick(Player player) {
        onClick(player);
    }

    public void onRightClick(Player player) {
        onClick(player);
    }

    public boolean update() {
        return false;
    }
}