package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLoadoutView implements View {

    private Battlegrounds plugin;
    private Inventory inventory;
    private Loadout loadout;
    private Map<ItemStack, Attachment> attachments;
    private Map<ItemStack, Gun> attachmentGun;
    private Map<ItemStack, Weapon> weapons;

    public EditLoadoutView(Battlegrounds plugin, Loadout loadout) {
        this.loadout = loadout;
        this.plugin = plugin;
        this.attachments = new HashMap<>();
        this.attachmentGun = new HashMap<>();
        this.weapons = new HashMap<>();

        this.inventory = buildInventory(plugin.getServer().createInventory(this, 27,
                Message.create(TranslationKey.VIEW_EDIT_LOADOUT, new Placeholder("bg_loadout", loadout.getName()))));

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(Message.create(TranslationKey.GO_BACK)).build());
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void addAttachmentSlot(Inventory inventory, Gun gun, int slot) {
        Attachment attachment = gun.getAttachments().size() > 0 ? gun.getAttachments().get(0) : null;
        String[] lore = Message.create(TranslationKey.EDIT_ATTACHMENT).split(",");

        ItemStack itemStack = new ItemStackBuilder(attachment != null ? attachment.getItemStack() : new ItemStack(Material.BARRIER))
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + Message.create(TranslationKey.GUN_ATTACHMENT, new Placeholder("bg_weapon", gun.getName())))
                .setLore(ChatColor.WHITE + getItemName(attachment), lore[0], lore[1])
                .setUnbreakable(true)
                .build();

        inventory.setItem(slot, itemStack);
        attachments.put(inventory.getItem(slot), attachment);
        attachmentGun.put(inventory.getItem(slot), gun);
    }

    private Inventory buildInventory(Inventory inventory) {
        int i = -2;
        for (Weapon weapon : loadout.getWeapons()) {
            ItemStack itemStack = new ItemStackBuilder(getItemStack(weapon))
                    .addItemFlags(ItemFlag.values())
                    .setDisplayName(ChatColor.WHITE + getDisplayName(weapon))
                    .setLore(ChatColor.WHITE + getItemName(weapon), Message.create(TranslationKey.EDIT_WEAPON))
                    .setUnbreakable(true)
                    .build();

            inventory.setItem(i += 2, itemStack);
            weapons.put(inventory.getItem(i), weapon);

            if (weapon instanceof Gun) {
                addAttachmentSlot(inventory, (Gun) weapon, i + 9);
            }
        }
        return inventory;
    }

    private Item findItem(ItemStack itemStack) {
        for (Map<ItemStack, ? extends Item> map : new Map[] { attachments, weapons }) {
            if (map.containsKey(itemStack)) {
                return map.get(itemStack);
            }
        }
        return null;
    }

    private View getAttachmentView(Player player, Loadout loadout, Gun gun, int attachmentNr) {
        return new SelectAttachmentView(plugin, player, loadout, gun, attachmentNr, inventory);
    }

    private String getDisplayName(Weapon weapon) {
        if (weapon == loadout.getPrimary()) {
            return Message.create(TranslationKey.WEAPON_PRIMARY);
        } else if (weapon == loadout.getSecondary()) {
            return Message.create(TranslationKey.WEAPON_SECONDARY);
        } else if (weapon == loadout.getEquipment()) {
            return Message.create(TranslationKey.WEAPON_EQUIPMENT);
        } else if (weapon == loadout.getKnife()) {
            return Message.create(TranslationKey.WEAPON_KNIFE);
        }
        return null;
    }

    private String getItemName(Item item) {
        return item != null ? item.getName() : Message.create(TranslationKey.NONE_SELECTED);
    }

    private ItemStack getItemStack(Weapon weapon) {
        return weapon != null ? weapon.getItemStack() : new ItemStack(Material.BARRIER);
    }

    private View getWeaponView(Player player, Weapon weapon) {
        if (weapon.getType().hasSubTypes()) {
            return new WeaponsView(plugin, loadout, weapon.getType().getDefaultItemSlot(), this);
        } else {
            List<Weapon> weapons = new ArrayList<>();
            weapons.addAll(plugin.getKnifeConfig().getList());
            return new SelectWeaponView(plugin, player, loadout, weapon.getType(), weapons, inventory);
        }
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (itemStack.getType() == Material.COMPASS) {
            player.openInventory(new LoadoutManagerView(plugin, player).getInventory());
            return;
        }
        Item item = findItem(itemStack);
        if (clickType == ClickType.LEFT) {
            if (item != null && item instanceof Weapon) {
                player.openInventory(getWeaponView(player, (Weapon) item).getInventory());
            }
            // If the clicked item is the attachment placeholder
            if (attachmentGun.get(itemStack) != null) {
                player.openInventory(getAttachmentView(player, loadout, attachmentGun.get(itemStack), 0).getInventory());
            }
        }
        if (clickType == ClickType.RIGHT) {
            if (item != null && item instanceof Attachment) {
                attachmentGun.get(itemStack).getAttachments().remove(item);
                player.openInventory(new EditLoadoutView(plugin, loadout).getInventory());
                plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadout);
            } else {
                player.openInventory(getWeaponView(player, (Weapon) item).getInventory());
            }
        }
    }

    public boolean onClose() {
        return true;
    }
}