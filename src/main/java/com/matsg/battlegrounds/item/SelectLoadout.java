package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SelectLoadout extends BattleItem {

    private Battlegrounds plugin;
    private Translator translator;

    public SelectLoadout(Battlegrounds plugin, Game game, Translator translator) {
        super(null, null);
        this.plugin = plugin;
        this.game = game;
        this.translator = translator;
        this.itemSlot = ItemSlot.MISCELLANEOUS;
        this.itemStack = new ItemStackBuilder(Material.COMPASS).setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.CHANGE_LOADOUT.getPath())).build();
    }

    private void onClick(GamePlayer gamePlayer) {
        if (gamePlayer == null) {
            return;
        }

        gamePlayer.getPlayer().openInventory(new SelectLoadoutView(plugin, translator, game, gamePlayer).getInventory());
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
