package com.matsg.battlegrounds.gui;

import org.bukkit.entity.Player;

public class FunctionalButton implements Button {

    private ButtonFunction<Player, ?> leftClickFunction;
    private ButtonFunction<Player, ?> rightClickFunction;

    public FunctionalButton(ButtonFunction<Player, ?> leftClickFunction, ButtonFunction<Player, ?> rightClickFunction) {
        this.leftClickFunction = leftClickFunction;
        this.rightClickFunction = rightClickFunction;
    }

    public void onLeftClick(Player player) {
        leftClickFunction.invoke(player);
    }

    public void onRightClick(Player player) {
        rightClickFunction.invoke(player);
    }
}
