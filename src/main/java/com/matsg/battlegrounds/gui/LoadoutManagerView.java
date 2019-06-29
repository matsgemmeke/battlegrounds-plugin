package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class LoadoutManagerView implements View {

    private Battlegrounds plugin;
    private Inventory inventory;
    private LoadoutFactory loadoutFactory;
    private Map<ItemStack, Loadout> loadouts;
    private Translator translator;

    public LoadoutManagerView(Battlegrounds plugin, Translator translator, Player player) {
        this.plugin = plugin;
        this.translator = translator;
        this.loadoutFactory = new LoadoutFactory();
        this.loadouts = new HashMap<>();
        this.inventory = plugin.getServer().createInventory(this, 27, translator.translate(TranslationKey.VIEW_LOADOUT_MANAGER));

        addLoadouts(player.getUniqueId());
    }

    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack getLoadoutItemStack(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null && weapon.getItemStack() != null) {
                return weapon.getItemStack();
            }
        }
        return new ItemStack(Material.BARRIER);
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        Loadout loadout = loadouts.get(itemStack);
        if (loadout == null) {
            return;
        }
        player.openInventory(new EditLoadoutView(plugin, translator, loadout).getInventory());
    }

    public boolean onClose() {
        return true;
    }

    private void addLoadouts(UUID uuid) {
        int i = 0;

        for (Map<String, String> loadoutSetup : plugin.getPlayerStorage().getStoredPlayer(uuid).getLoadoutSetups()) {
            List<Attachment> primaryAttachments = new ArrayList<>();
            List<Attachment> secondaryAttachments = new ArrayList<>();
            String attachmentString;

            if ((attachmentString = loadoutSetup.get("primary_attachments")) != null && !attachmentString.isEmpty()) {
                for (String attachmentId : attachmentString.split(", ")) {
                    Attachment attachment = plugin.getAttachmentFactory().make(attachmentId);
                    if (attachment != null) {
                        primaryAttachments.add(attachment);
                    }
                }
            }

            if ((attachmentString = loadoutSetup.get("secondary_attachments")) != null && !attachmentString.isEmpty()) {
                for (String attachmentId : attachmentString.split(", ")) {
                    Attachment attachment = plugin.getAttachmentFactory().make(attachmentId);
                    if (attachment != null) {
                        secondaryAttachments.add(attachment);
                    }
                }
            }

            Loadout loadout = loadoutFactory.make(
                    Integer.parseInt(loadoutSetup.get("loadout_nr")),
                    loadoutSetup.get("loadout_name"),
                    plugin.getFirearmFactory().make(loadoutSetup.get("primary")),
                    plugin.getFirearmFactory().make(loadoutSetup.get("secondary")),
                    plugin.getEquipmentFactory().make(loadoutSetup.get("equipment")),
                    plugin.getMeleeWeaponFactory().make(loadoutSetup.get("melee_weapon")),
                    primaryAttachments.toArray(new Attachment[primaryAttachments.size()]),
                    secondaryAttachments.toArray(new Attachment[secondaryAttachments.size()]),
                    null,
                    null
            );
            ItemStack itemStack = new ItemStackBuilder(getLoadoutItemStack(loadout))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(++ i)
                    .setDisplayName(ChatColor.WHITE + loadout.getName())
                    .setLore(translator.translate(TranslationKey.EDIT_LOADOUT))
                    .setUnbreakable(true)
                    .build();

            inventory.setItem(i + 10, itemStack);
            loadouts.put(inventory.getItem(i + 10), loadout);
        }
    }
}
