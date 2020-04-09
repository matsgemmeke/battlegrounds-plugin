package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LoadoutManagerView implements View {

    public Gui gui;
    private ItemFactory<Attachment> attachmentFactory;
    private ItemFactory<Equipment> equipmentFactory;
    private ItemFactory<Firearm> firearmFactory;
    private ItemFactory<MeleeWeapon> meleeWeaponFactory;
    private LoadoutFactory loadoutFactory;
    private Player player;
    private PlayerStorage playerStorage;
    private Translator translator;
    private ViewFactory viewFactory;

    public LoadoutManagerView setAttachmentFactory(ItemFactory<Attachment> attachmentFactory) {
        this.attachmentFactory = attachmentFactory;
        return this;
    }

    public LoadoutManagerView setEquipmentFactory(ItemFactory<Equipment> equipmentFactory) {
        this.equipmentFactory = equipmentFactory;
        return this;
    }

    public LoadoutManagerView setFirearmFactory(ItemFactory<Firearm> firearmFactory) {
        this.firearmFactory = firearmFactory;
        return this;
    }

    public LoadoutManagerView setLoadoutFactory(LoadoutFactory loadoutFactory) {
        this.loadoutFactory = loadoutFactory;
        return this;
    }

    public LoadoutManagerView setMeleeWeaponFactory(ItemFactory<MeleeWeapon> meleeWeaponFactory) {
        this.meleeWeaponFactory = meleeWeaponFactory;
        return this;
    }

    public LoadoutManagerView setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public LoadoutManagerView setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
        return this;
    }

    public LoadoutManagerView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public LoadoutManagerView setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        return this;
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateLoadouts(StaticPane pane) {
        int i = 0;

        for (Map<String, String> loadoutSetup : playerStorage.getStoredPlayer(player.getUniqueId()).getLoadoutSetups()) {
            List<Attachment> primaryAttachments = new ArrayList<>();
            List<Attachment> secondaryAttachments = new ArrayList<>();
            String attachmentString;

            if ((attachmentString = loadoutSetup.get("primary_attachments")) != null && !attachmentString.isEmpty()) {
                for (String attachmentId : attachmentString.split(", ")) {
                    Attachment attachment = attachmentFactory.make(attachmentId);
                    if (attachment != null) {
                        primaryAttachments.add(attachment);
                    }
                }
            }

            if ((attachmentString = loadoutSetup.get("secondary_attachments")) != null && !attachmentString.isEmpty()) {
                for (String attachmentId : attachmentString.split(", ")) {
                    Attachment attachment = attachmentFactory.make(attachmentId);
                    if (attachment != null) {
                        secondaryAttachments.add(attachment);
                    }
                }
            }

            Loadout loadout = loadoutFactory.make(
                    Integer.parseInt(loadoutSetup.get("loadout_nr")),
                    loadoutSetup.get("loadout_name"),
                    firearmFactory.make(loadoutSetup.get("primary")),
                    firearmFactory.make(loadoutSetup.get("secondary")),
                    equipmentFactory.make(loadoutSetup.get("equipment")),
                    meleeWeaponFactory.make(loadoutSetup.get("melee_weapon")),
                    primaryAttachments.toArray(new Attachment[primaryAttachments.size()]),
                    secondaryAttachments.toArray(new Attachment[secondaryAttachments.size()]),
                    null,
                    null,
                    true
            );
            ItemStack itemStack = new ItemStackBuilder(getLoadoutItemStack(loadout))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(++i)
                    .setDisplayName(ChatColor.WHITE + loadout.getName())
                    .setLore(translator.translate(TranslationKey.EDIT_LOADOUT.getPath()))
                    .setUnbreakable(true)
                    .build();

            pane.addItem(new GuiItem(itemStack, event -> {
                View view = viewFactory.make(EditLoadoutView.class, instance -> {
                    instance.setLoadout(loadout);
                    instance.setPlayerUUID(player.getUniqueId());
                    instance.setPreviousView(this);
                });
                view.openInventory(event.getWhoClicked());
            }), i - 1, 0);
        }
    }

    private ItemStack getLoadoutItemStack(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null && weapon.getItemStack() != null) {
                return weapon.getItemStack();
            }
        }
        return new ItemStack(Material.BARRIER);
    }
}
