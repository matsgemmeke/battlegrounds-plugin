package com.matsg.battlegrounds.mode.zombies.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.gui.ClickableItem;
import com.matsg.battlegrounds.gui.View;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class SectionOverviewView implements View {

    private static final int INVENTORY_SIZE = 45;
    private static final String EMPTY_STRING = "";

    private Inventory inventory;
    private Map<ItemStack, ClickableItem> items;
    private Plugin plugin;
    private Section section;
    private Translator translator;
    private View previousView;

    public SectionOverviewView(Plugin plugin, Section section, Translator translator, View previousView) {
        this.plugin = plugin;
        this.section = section;
        this.translator = translator;
        this.previousView = previousView;
        this.inventory = createInventory();
        this.items = new HashMap<>();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() == Material.COMPASS) {
            player.openInventory(previousView.getInventory());
            return;
        }

        ClickableItem item = items.get(itemStack);

        if (clickType == ClickType.LEFT) {
            item.onLeftClick(player);
        } else if (clickType == ClickType.RIGHT) {
            item.onRightClick(player);
        }
    }

    public boolean onClose() {
        return true;
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_SECTION_OVERVIEW_TITLE.getPath(), new Placeholder("bg_section", section.getName()));
        Inventory inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);

        
    }
}
