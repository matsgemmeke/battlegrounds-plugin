package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.gui.Button;
import com.matsg.battlegrounds.gui.FunctionalButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractSettingsView implements View {

    private Map<ItemStack, Button> buttons;

    public AbstractSettingsView() {
        this.buttons = new HashMap<>();
    }

    public abstract void refreshContent();

    protected void addButton(ItemStack itemStack, Button button) {
        if (buttons.containsKey(itemStack)) {
            return;
        }
        buttons.put(itemStack, button);
    }

    protected Button createBackButton(ItemStack itemStack, View view) {
        Consumer<Player> click = player -> player.openInventory(view.getInventory());
        Button backButton = new FunctionalButton(click, click);

        addButton(itemStack, backButton);

        return backButton;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null) {
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

    public void onClose(Player player) { }
}
