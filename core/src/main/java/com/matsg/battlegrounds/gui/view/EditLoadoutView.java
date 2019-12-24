package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.matsg.battlegrounds.ItemFinder;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditLoadoutView implements View {

    public Gui gui;
    private Consumer<Loadout> updateLoadout;
    private ItemFinder itemFinder;
    private Loadout loadout;
    private PlayerStorage playerStorage;
    private Translator translator;
    private UUID playerUUID;
    private ViewFactory viewFactory;
    private View previousView;

    public EditLoadoutView setItemFinder(ItemFinder itemFinder) {
        this.itemFinder = itemFinder;
        return this;
    }

    public EditLoadoutView setLoadout(Loadout loadout) {
        this.loadout = loadout;
        return this;
    }

    public EditLoadoutView setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
        return this;
    }

    public EditLoadoutView setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
        return this;
    }

    public EditLoadoutView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public EditLoadoutView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public EditLoadoutView setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        updateLoadout.accept(loadout);
        gui.show(entity);
    }

    private void addAttachmentSlot(StaticPane pane, Gun gun, int x, int y) {
        Attachment attachment = gun.getAttachments().size() > 0 ? gun.getAttachments().get(0) : null;
        String[] lore = translator.translate(TranslationKey.EDIT_ATTACHMENT.getPath()).split(",");

        ItemStack itemStack = new ItemStackBuilder(attachment != null ? attachment.getItemStack() : new ItemStack(Material.BARRIER))
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.GUN_ATTACHMENT.getPath(), new Placeholder("bg_weapon", gun.getMetadata().getName())))
                .setLore(ChatColor.WHITE + getItemName(attachment), lore[0], lore[1])
                .setUnbreakable(true)
                .build();

        pane.addItem(new GuiItem(itemStack, event -> {
            if (event.getClick() == ClickType.LEFT) {
                View view = viewFactory.make(SelectAttachmentView.class, instance -> {
                    instance.setAttachmentNr(0);
                    instance.setGun(gun);
                    instance.setLoadout(loadout);
                    instance.setPlayerUUID(playerUUID);
                    instance.setPreviousView(this);
                });
                view.openInventory(event.getWhoClicked());
            } else if (event.getClick() == ClickType.RIGHT) {
                gun.getAttachments().remove(attachment);
                playerStorage.getStoredPlayer(playerUUID).saveLoadout(loadout.getLoadoutNr(), loadout.convertToMap());
                pane.clear();
                populateLoadout(pane);
                gui.update();
            }
        }), x, y);
    }

    public void populateLoadout(StaticPane pane) {
        updateLoadout = loadout -> {
            pane.clear();

            int x = -2;

            for (ItemSlot itemSlot : ItemSlot.WEAPON_SLOTS) {
                Weapon weapon = loadout.getWeapon(itemSlot);

                String[] lore = translator.translate(TranslationKey.EDIT_WEAPON.getPath()).split(",");

                ItemStack itemStack = new ItemStackBuilder(getItemStack(weapon))
                        .addItemFlags(ItemFlag.values())
                        .setAmount(1)
                        .setDisplayName(ChatColor.WHITE + getDisplayName(itemSlot))
                        .setLore(ChatColor.WHITE + getItemName(weapon), lore[0], lore[1])
                        .setUnbreakable(true)
                        .build();

                pane.addItem(new GuiItem(itemStack, event -> {
                    if (event.getClick() == ClickType.LEFT) {
                        if (weapon.getType().hasSubTypes()) {
                            List<Weapon> weapons = itemFinder.findAllWeapons().stream().filter(w -> w.getType().hasSubTypes() && w.getType().getDefaultItemSlot() == itemSlot).collect(Collectors.toList());

                            View view = viewFactory.make(SelectWeaponTypeView.class, instance -> {
                                instance.setLoadout(loadout);
                                instance.setPreviousView(this);
                                instance.setWeapons(weapons);
                            });
                            view.openInventory(event.getWhoClicked());
                        } else {
                            List<Weapon> weapons = itemFinder.findAllWeapons().stream().filter(w -> !w.getType().hasSubTypes()).collect(Collectors.toList());

                            View view = viewFactory.make(SelectWeaponView.class, instance -> {
                                instance.setLoadout(loadout);
                                instance.setLoadoutView(this);
                                instance.setPlayerUUID(playerUUID);
                                instance.setPreviousView(this);
                                instance.setWeapons(weapons);
                            });
                            view.openInventory(event.getWhoClicked());
                        }
                    } else if (event.getClick() == ClickType.RIGHT) {
                        loadout.removeWeapon(weapon);
                        playerStorage.getStoredPlayer(playerUUID).saveLoadout(loadout.getLoadoutNr(), loadout.convertToMap());
                        pane.clear();
                        populateLoadout(pane);
                        gui.update();
                    }
                }), x += 2, 0);

                if (weapon instanceof Gun) {
                    addAttachmentSlot(pane, (Gun) weapon, x, 1); // Add an attachment slot one row below the gun
                }
            }
        };
        updateLoadout.accept(loadout);
    }

    private String getDisplayName(ItemSlot itemSlot) {
        switch (itemSlot) {
            case FIREARM_PRIMARY:
                return translator.translate(TranslationKey.WEAPON_PRIMARY.getPath());
            case FIREARM_SECONDARY:
                return translator.translate(TranslationKey.WEAPON_SECONDARY.getPath());
            case EQUIPMENT:
                return translator.translate(TranslationKey.WEAPON_EQUIPMENT.getPath());
            case MELEE_WEAPON:
                return translator.translate(TranslationKey.WEAPON_MELEE_WEAPON.getPath());
        }
        return null;
    }

    private String getItemName(Item item) {
        return item != null ? item.getMetadata().getName() : translator.translate(TranslationKey.NONE_SELECTED.getPath());
    }

    private ItemStack getItemStack(Weapon weapon) {
        return weapon != null ? weapon.getItemStack() : new ItemStack(Material.BARRIER);
    }
}
