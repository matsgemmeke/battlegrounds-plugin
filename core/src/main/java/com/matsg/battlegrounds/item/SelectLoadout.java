package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.SelectLoadoutView;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import org.bukkit.Material;

public class SelectLoadout extends BattleItem {

    private ViewFactory viewFactory;

    public SelectLoadout(String name, Game game, ViewFactory viewFactory) {
        super(null, null);
        this.game = game;
        this.viewFactory = viewFactory;
        this.itemSlot = ItemSlot.MISCELLANEOUS;
        this.itemStack = new ItemStackBuilder(Material.COMPASS).setDisplayName(name).build();
    }

    private void onClick(GamePlayer gamePlayer) {
        if (gamePlayer == null) {
            return;
        }

        LoadoutFactory loadoutFactory = new LoadoutFactory();

        View view = viewFactory.make(SelectLoadoutView.class, instance -> {
            instance.setGame(game);
            instance.setGamePlayer(gamePlayer);
            instance.setLoadoutFactory(loadoutFactory);
        });
        view.openInventory(gamePlayer.getPlayer());
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
