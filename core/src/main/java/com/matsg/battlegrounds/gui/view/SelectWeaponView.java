package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectWeaponView implements View {

    public Gui gui;
    private LevelConfig levelConfig;
    private List<Weapon> weapons;
    private Loadout loadout;
    private PlayerStorage playerStorage;
    private Translator translator;
    private UUID playerUUID;
    private View loadoutView;
    private View previousView;

    public SelectWeaponView setLevelConfig(LevelConfig levelConfig) {
        this.levelConfig = levelConfig;
        return this;
    }

    public SelectWeaponView setLoadout(Loadout loadout) {
        this.loadout = loadout;
        return this;
    }

    public SelectWeaponView setLoadoutView(View loadoutView) {
        this.loadoutView = loadoutView;
        return this;
    }

    public SelectWeaponView setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
        return this;
    }

    public SelectWeaponView setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
        return this;
    }

    public SelectWeaponView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public SelectWeaponView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public SelectWeaponView setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateWeapons(OutlinePane pane) {
        Collections.sort(weapons, (o1, o2) -> Integer.valueOf(levelConfig.getLevelUnlocked(o1.getMetadata().getId())).compareTo(levelConfig.getLevelUnlocked(o2.getMetadata().getId())));

        for (Weapon weapon : weapons) {
            weapon.update();

            if (levelConfig.getLevelUnlocked(weapon.getMetadata().getId()) <= levelConfig.getLevel(playerStorage.getStoredPlayer(playerUUID).getExp())) {
                pane.addItem(new GuiItem(getUnlockedItemStack(weapon), event -> {
                    replaceWeapon(loadout, weapon);
                    playerStorage.getStoredPlayer(playerUUID).saveLoadout(loadout.getLoadoutNr(), loadout.convertToMap());
                    loadoutView.openInventory(event.getWhoClicked());
                }));
            } else {
                pane.addItem(new GuiItem(getLockedItemStack(weapon), event -> {
                    event.setCancelled(true);
                }));
            }
        }
    }

    private ItemStack getLockedItemStack(Weapon weapon) {
        return new ItemStackBuilder(Material.BARRIER)
                .addItemFlags(ItemFlag.values())
                .setDisplayName(translator.translate(TranslationKey.VIEW_ITEM_LOCKED.getPath(), new Placeholder("bg_level", levelConfig.getLevelUnlocked(weapon.getMetadata().getId()))))
                .setUnbreakable(true)
                .build();
    }

    private ItemStack getUnlockedItemStack(Weapon weapon) {
        return new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + weapon.getMetadata().getName())
                .setLore(getLore(weapon))
                .setUnbreakable(true)
                .build();
    }

    private String[] getLore(Weapon weapon) {
        List<String> lore = new ArrayList<>(weapon.getItemStack().getItemMeta().getLore());
        lore.add(" ");
        int maxLength = 30;

        Pattern pattern = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(weapon.getMetadata().getDescription());

        while (matcher.find()) {
            lore.add(ChatColor.WHITE + matcher.group(1));
        }

        return lore.toArray(new String[lore.size()]);
    }

    private void replaceWeapon(Loadout loadout, Weapon weapon) {
        ItemSlot itemSlot = weapon.getType().getDefaultItemSlot();
        weapon.setItemSlot(itemSlot);
        switch (itemSlot) {
            case FIREARM_PRIMARY:
                if (loadout.getPrimary() != null && loadout.getPrimary() instanceof Gun) {
                    ((Gun) loadout.getPrimary()).getAttachments().clear();
                }
                loadout.setPrimary((Firearm) weapon);
                break;
            case FIREARM_SECONDARY:
                if (loadout.getSecondary() != null && loadout.getSecondary() instanceof Gun) {
                    ((Gun) loadout.getSecondary()).getAttachments().clear();
                }
                loadout.setSecondary((Firearm) weapon);
                break;
            case EQUIPMENT:
                loadout.setEquipment((Equipment) weapon);
                break;
            case MELEE_WEAPON:
                loadout.setMeleeWeapon((MeleeWeapon) weapon);
                break;
        }
    }
}
