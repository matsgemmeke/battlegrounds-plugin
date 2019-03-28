package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SelectLoadout extends BattleItem {

    public SelectLoadout(Game game) {
        super(null, "SelectLoadout", null);
        this.game = game;
        this.itemSlot = ItemSlot.MISCELLANEOUS;
        this.itemStack = getDefaultItemStack();
    }

    private static ItemStack getDefaultItemStack() {
        MessageHelper messageHelper = new MessageHelper();
        return new ItemStackBuilder(Material.COMPASS).setDisplayName(ChatColor.WHITE + messageHelper.create(TranslationKey.CHANGE_LOADOUT)).build();
    }

    private void onClick(GamePlayer gamePlayer) {
        if (gamePlayer == null) {
            return;
        }
        gamePlayer.getPlayer().openInventory(new SelectLoadoutView(plugin, game, gamePlayer).getInventory());
    }

    public void onLeftClick(GamePlayer gamePlayer) {
        onClick(gamePlayer);
    }

    public void onRightClick(GamePlayer gamePlayer) {
        onClick(gamePlayer);
    }

    public boolean update() {
        return false;
    }
}
