package com.matsg.battlegrounds.mode.zombies.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.component.*;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class SectionSettingsView implements View {

    private static final String EMPTY_STRING = "";

    public Gui gui;
    private Consumer<ArenaComponent> onComponentRemove;
    private Section section;
    private Translator translator;
    private View previousView;

    public SectionSettingsView setOnComponentRemove(Consumer<ArenaComponent> onComponentRemove) {
        this.onComponentRemove = onComponentRemove;
        return this;
    }

    public SectionSettingsView setSection(Section section) {
        this.section = section;
        return this;
    }

    public SectionSettingsView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public SectionSettingsView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateComponents(OutlinePane pane) {
        Map<Integer, GuiItem> items = new TreeMap<>();

        for (Barricade barricade : section.getBarricadeContainer().getAll()) {
            Location location = barricade.getCenter();
            String locationString = toLocationString(location);

            ItemStack barricadeItemStack = new ItemStackBuilder(new ItemStack(XMaterial.IRON_BARS.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", barricade.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Barricade")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            GuiItem item = getGuiItem(barricadeItemStack, barricade, location, pane);

            items.put(barricade.getId(), item);
        }

        for (Door door : section.getDoorContainer().getAll()) {
            Location location = door.getCenter();
            String locationString = toLocationString(location);

            ItemStack doorItemStack = new ItemStackBuilder(new ItemStack(XMaterial.IRON_DOOR.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", door.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Door")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            GuiItem item = getGuiItem(doorItemStack, door, location, pane);

            items.put(door.getId(), item);
        }

        for (MobSpawn mobSpawn : section.getMobSpawnContainer().getAll()) {
            // Get the default mob spawn location
            Location location = mobSpawn.getSpawnLocation(BattleEntityType.ZOMBIE);
            String locationString = toLocationString(location);

            ItemStack mobSpawnItemStack = new ItemStackBuilder(new ItemStack(XMaterial.SPAWNER.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", mobSpawn.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Mob spawn")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_BARRICADE.getPath(),
                                    new Placeholder("bg_component_barricade", mobSpawn.getBarricade() != null ? "Id " + mobSpawn.getBarricade().getId() : "N/A")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            GuiItem item = getGuiItem(mobSpawnItemStack, mobSpawn, location, pane);

            items.put(mobSpawn.getId(), item);
        }

        for (MysteryBox mysteryBox : section.getMysteryBoxContainer().getAll()) {
            Location location = mysteryBox.getItemDropLocation();
            String locationString = toLocationString(location);

            ItemStack mysteryBoxItemStack = new ItemStackBuilder(new ItemStack(XMaterial.CHEST.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", mysteryBox.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Mystery box")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_PRICE.getPath(),
                                    new Placeholder("bg_component_price", mysteryBox.getPrice())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            GuiItem item = getGuiItem(mysteryBoxItemStack, mysteryBox, location, pane);

            items.put(mysteryBox.getId(), item);
        }

        for (PerkMachine perkMachine : section.getPerkMachineContainer().getAll()) {
            Location location = perkMachine.getSign().getLocation();
            String locationString = toLocationString(location);

            ItemStack perkMachineItemStack = new ItemStackBuilder(new ItemStack(XMaterial.POTION.parseMaterial()))
                    .addItemFlags(ItemFlag.values())
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", perkMachine.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Perk machine")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_PERK.getPath(),
                                    new Placeholder("bg_component_perk", perkMachine.getPerk().getMetadata().getName())
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_PRICE.getPath(),
                                    new Placeholder("bg_component_price", perkMachine.getPrice())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            GuiItem item = getGuiItem(perkMachineItemStack, perkMachine, location, pane);

            items.put(perkMachine.getId(), item);
        }
        
        for (WallWeapon wallWeapon : section.getWallWeaponContainer().getAll()) {
            Location location = wallWeapon.getItemFrame().getLocation();
            String locationString = toLocationString(location);

            ItemStack wallWeaponItemStack = new ItemStackBuilder(new ItemStack(XMaterial.ITEM_FRAME.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM.getPath(),
                                    new Placeholder("bg_component_id", wallWeapon.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Wall weapon")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_PRICE.getPath(),
                                    new Placeholder("bg_component_price", wallWeapon.getPrice())
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_WEAPON.getPath(),
                                    new Placeholder("bg_component_weapon", wallWeapon.getWeapon().getMetadata().getName())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_ITEM_REMOVE.getPath())
                    )
                    .build();

            GuiItem item = getGuiItem(wallWeaponItemStack, wallWeapon, location, pane);

            items.put(wallWeapon.getId(), item);
        }

        for (GuiItem item : items.values()) {
            pane.addItem(item);
        }
    }

    private GuiItem getGuiItem(ItemStack itemStack, ArenaComponent component, Location teleportLocation, OutlinePane pane) {
        return new GuiItem(itemStack, event -> {
            if (event.getClick() == ClickType.LEFT) {
                event.getWhoClicked().teleport(teleportLocation);
            } else if (event.getClick() == ClickType.RIGHT) {
                onComponentRemove.accept(component);
                section.removeComponent(component);
                pane.clear();
                populateComponents(pane);
                gui.update();
            }
        });
    }

    private String toLocationString(Location location) {
        return "x" + location.getBlockX() + ", y" + location.getBlockY() + ", z" + location.getBlockZ();
    }
}
