package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.gui.ViewFactory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EditGameConfigurationView implements View {

    public Gui gui;
    private Game game;
    private ViewFactory viewFactory;
    private View previousView;

    public EditGameConfigurationView setGame(Game game) {
        this.game = game;
        return this;
    }

    public EditGameConfigurationView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public EditGameConfigurationView setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        return this;
    }

    public void cancelClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void selectGameModesClick(InventoryClickEvent event) {
        View view = viewFactory.make(SelectGameModesView.class, instance -> {
            instance.setGame(game);
            instance.setPreviousView(this);
        });
        view.openInventory(event.getWhoClicked());
    }
}
