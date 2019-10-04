package com.matsg.battlegrounds.mode.zombies.gui;

import com.matsg.battlegrounds.api.game.ComponentWrapper;
import com.matsg.battlegrounds.gui.ClickableItem;
import com.matsg.battlegrounds.gui.View;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SectionViewItem implements ClickableItem {

    private ComponentWrapper componentWrapper;
    private ItemStack itemStack;
    private Section section;
    private View nextView;

    public SectionViewItem(ComponentWrapper componentWrapper, Section section, ItemStack itemStack, View nextView) {
        this.componentWrapper = componentWrapper;
        this.section = section;
        this.itemStack = itemStack;
        this.nextView = nextView;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void onLeftClick(Player player) {
        player.openInventory(nextView.getInventory());
    }

    public void onRightClick(Player player) {
        componentWrapper.removeComponent(section);
    }
}
