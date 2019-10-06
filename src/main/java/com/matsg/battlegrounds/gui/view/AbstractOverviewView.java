package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.gui.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOverviewView implements View {

    private Map<ItemStack, Button> buttons;

    public AbstractOverviewView() {
        this.buttons = new HashMap<>();
    }

    public abstract void refreshContent();

    public abstract void returnToPreviousView(Player player);

    protected void addButton(ItemStack itemStack, Button button) {
        if (buttons.containsKey(itemStack)) {
            return;
        }
        buttons.put(itemStack, button);
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() == Material.COMPASS) {
            returnToPreviousView(player);
            return;
        }

        Button button = buttons.get(itemStack);

        if (button == null) {
            return;
        }

        if (clickType == ClickType.LEFT) {
            button.onLeftClick(player);
        } else if (clickType == ClickType.RIGHT) {
            button.onRightClick(player);
        }

        refreshContent();
    }

    public boolean onClose() {
        return true;
    }
}
