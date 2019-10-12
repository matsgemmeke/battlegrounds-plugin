package com.matsg.battlegrounds.mode.zombies.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.gui.Button;
import com.matsg.battlegrounds.gui.FunctionalButton;
import com.matsg.battlegrounds.gui.view.AbstractOverviewView;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.mode.zombies.component.*;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class SectionOverviewView extends AbstractOverviewView {

    private static final int INVENTORY_SIZE = 45;
    private static final String EMPTY_STRING = "";

    private Consumer<ArenaComponent> removeFunction;
    private Inventory inventory;
    private Plugin plugin;
    private Section section;
    private Translator translator;
    private View previousView;

    public SectionOverviewView(Plugin plugin, Section section, Consumer<ArenaComponent> removeFunction, Translator translator, View previousView) {
        this.plugin = plugin;
        this.section = section;
        this.removeFunction = removeFunction;
        this.translator = translator;
        this.previousView = previousView;
        this.inventory = createInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void refreshContent() {
        inventory.clear();

        Map<Integer, ItemStack> items = new TreeMap<>();

        for (Barricade barricade : section.getBarricadeContainer().getAll()) {
            Location location = barricade.getCenter();
            String locationString = toLocationString(location);

            ItemStack barricadeItemStack = new ItemStackBuilder(new ItemStack(XMaterial.IRON_BARS.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", barricade.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Barricade")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())
                    )
                    .build();

            Button button = createButton(location, barricade);

            addButton(barricadeItemStack, button);

            items.put(barricade.getId(), barricadeItemStack);
        }

        for (Door door : section.getDoorContainer().getAll()) {
            Location location = door.getCenter();
            String locationString = toLocationString(location);

            ItemStack doorItemStack = new ItemStackBuilder(new ItemStack(XMaterial.IRON_DOOR.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", door.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Door")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())
                    )
                    .build();

            Button button = createButton(location, door);

            addButton(doorItemStack, button);

            items.put(door.getId(), doorItemStack);
        }

        for (MobSpawn mobSpawn : section.getMobSpawnContainer().getAll()) {
            // Get the default mob spawn location
            Location location = mobSpawn.getSpawnLocation(BattleEntityType.ZOMBIE);
            String locationString = toLocationString(location);

            ItemStack mobSpawnItemStack = new ItemStackBuilder(new ItemStack(XMaterial.SPAWNER.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", mobSpawn.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Mob spawn")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_BARRICADE.getPath(),
                                    new Placeholder("bg_component_barricade", mobSpawn.getBarricade() != null ? "Yes" : "No")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())
                    )
                    .build();

            Button button = createButton(location, mobSpawn);

            addButton(mobSpawnItemStack, button);

            items.put(mobSpawn.getId(), mobSpawnItemStack);
        }

        for (MysteryBox mysteryBox : section.getMysteryBoxContainer().getAll()) {
            Location location = mysteryBox.getItemDropLocation();
            String locationString = toLocationString(location);

            ItemStack mysteryBoxItemStack = new ItemStackBuilder(new ItemStack(XMaterial.CHEST.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", mysteryBox.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Mystery box")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_PRICE.getPath(),
                                    new Placeholder("bg_component_price", mysteryBox.getPrice())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())
                    )
                    .build();

            Button button = createButton(location, mysteryBox);

            addButton(mysteryBoxItemStack, button);

            items.put(mysteryBox.getId(), mysteryBoxItemStack);
        }

        for (PerkMachine perkMachine : section.getPerkMachineContainer().getAll()) {
            Location location = perkMachine.getSign().getLocation();
            String locationString = toLocationString(location);

            ItemStack perkMachineItemStack = new ItemStackBuilder(new ItemStack(XMaterial.POTION.parseMaterial()))
                    .addItemFlags(ItemFlag.values())
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", perkMachine.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Perk machine")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_PERK.getPath(),
                                    new Placeholder("bg_component_perk", perkMachine.getPerk().getMetadata().getName())
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_PRICE.getPath(),
                                    new Placeholder("bg_component_price", perkMachine.getPrice())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())
                    )
                    .build();

            Button button = createButton(location, perkMachine);

            addButton(perkMachineItemStack, button);

            items.put(perkMachine.getId(), perkMachineItemStack);
        }
        
        for (WallWeapon wallWeapon : section.getWallWeaponContainer().getAll()) {
            Location location = wallWeapon.getItemFrame().getLocation();
            String locationString = toLocationString(location);

            ItemStack wallWeaponItemStack = new ItemStackBuilder(new ItemStack(XMaterial.ITEM_FRAME.parseMaterial()))
                    .setDisplayName(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON.getPath(),
                                    new Placeholder("bg_component_id", wallWeapon.getId())
                            )
                    )
                    .setLore(
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TYPE.getPath(),
                                    new Placeholder("bg_component_type", "Wall weapon")
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_LOCATION.getPath(),
                                    new Placeholder("bg_component_location", locationString)
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_PRICE.getPath(),
                                    new Placeholder("bg_component_price", wallWeapon.getPrice())
                            ),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_WEAPON.getPath(),
                                    new Placeholder("bg_component_weapon", wallWeapon.getWeapon().getMetadata().getName())
                            ),
                            EMPTY_STRING,
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_TELEPORT.getPath()),
                            translator.translate(TranslationKey.VIEW_COMPONENT_BUTTON_REMOVE.getPath())
                    )
                    .build();

            Button button = createButton(location, wallWeapon);

            addButton(wallWeaponItemStack, button);

            items.put(wallWeapon.getId(), wallWeaponItemStack);
        }

        for (ItemStack itemStack : items.values()) {
            inventory.addItem(itemStack);
        }

        ItemStack backButton = new ItemStackBuilder(new ItemStack(XMaterial.COMPASS.parseMaterial()))
                .setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath()))
                .build();

        inventory.setItem(INVENTORY_SIZE - 1, backButton);
    }

    public void returnToPreviousView(Player player) {
        player.openInventory(previousView.getInventory());
    }

    private Button createButton(Location location, ArenaComponent component) {
        Consumer<Player> leftClick = player -> player.teleport(location);
        Consumer<Player> rightClick = player -> {
            section.removeComponent(component);
            removeFunction.accept(component);
        };
        return new FunctionalButton(leftClick, rightClick);
    }

    private Inventory createInventory() {
        String title = translator.translate(TranslationKey.VIEW_SECTION_OVERVIEW_TITLE.getPath(), new Placeholder("bg_section", section.getName()));
        inventory = plugin.getServer().createInventory(this, INVENTORY_SIZE, title);
        refreshContent();
        return inventory;
    }

    private String toLocationString(Location location) {
        return "x" + location.getBlockX() + ", y" + location.getBlockY() + ", z" + location.getBlockZ();
    }
}
