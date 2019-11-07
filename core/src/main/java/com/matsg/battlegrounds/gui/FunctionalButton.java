package com.matsg.battlegrounds.gui;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class FunctionalButton implements Button {

    private Consumer<Player> leftClickFunction;
    private Consumer<Player> rightClickFunction;

    public FunctionalButton(Consumer<Player> clickFunction) {
        this.leftClickFunction = clickFunction;
        this.rightClickFunction = clickFunction;
    }

    public FunctionalButton(Consumer<Player> leftClickFunction, Consumer<Player> rightClickFunction) {
        this.leftClickFunction = leftClickFunction;
        this.rightClickFunction = rightClickFunction;
    }

    public void onLeftClick(Player player) {
        leftClickFunction.accept(player);
    }

    public void onRightClick(Player player) {
        rightClickFunction.accept(player);
    }
}
