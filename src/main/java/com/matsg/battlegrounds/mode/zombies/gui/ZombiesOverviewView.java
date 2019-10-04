package com.matsg.battlegrounds.mode.zombies.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.gui.ClickableItem;
import com.matsg.battlegrounds.gui.View;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ZombiesOverviewView implements View {

    private static final int INVENTORY_SIZE = 45;
    private static final String EMPTY_STRING = "";

    private Battlegrounds plugin;
    private Inventory inventory;
    private Map<ItemStack, ClickableItem> items;
    private Translator translator;
    private View previousView;
    private Zombies zombies;

    public ZombiesOverviewView(Battlegrounds plugin, Zombies zombies, Translator translator, View previousView) {
        this.plugin = plugin;
        this.zombies = zombies;
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
        String title = translator.translate(TranslationKey.VIEW_ZOMBIES_OVERVIEW_TITLE.getPath());
        Inventory inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);

        for (Section section : zombies.getSectionContainer().getAll()) {
            ItemStack sectionItemStack = new ItemStackBuilder(new ItemStack(XMaterial.GLASS.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_ZOMBIES_OVERVIEW_SECTION.getPath(),
                                    new Placeholder("bg_section", section.getName())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_ZOMBIES_OVERVIEW_SECTION_COMPONENTS.getPath(),
                                    new Placeholder("bg_components", section.getComponentCount())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_ZOMBIES_OVERVIEW_SECTION_OVERVIEW.getPath()),
                            translator.translate(TranslationKey.VIEW_ZOMBIES_OVERVIEW_SECTION_REMOVE.getPath())
                    )
                    .build();

            View sectionView = new SectionOverviewView(section, translator);

            ClickableItem item = new SectionViewItem(zombies, section, sectionItemStack, sectionView);

            items.put(sectionItemStack, item);

            inventory.addItem(sectionItemStack);
        }

        return inventory;
    }
}
