package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.MessageHelper;
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
    private List<EditLoadoutViewItem> items;
    private Loadout loadout;
    private Map<ItemStack, Attachment> attachments;
    private Map<ItemStack, Gun> attachmentGun;
    private MessageHelper messageHelper;

    public EditLoadoutView(Battlegrounds plugin, Loadout loadout) {
        this.loadout = loadout;
        this.plugin = plugin;
        this.attachments = new HashMap<>();
        this.attachmentGun = new HashMap<>();
        this.items = new ArrayList<>();
        this.messageHelper = new MessageHelper();

        this.inventory = buildInventory(plugin.getServer().createInventory(this, 27,
                messageHelper.create(TranslationKey.VIEW_EDIT_LOADOUT, new Placeholder("bg_loadout", loadout.getName()))));

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(messageHelper.create(TranslationKey.GO_BACK)).build());
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void addAttachmentSlot(Inventory inventory, Gun gun, int slot) {
        Attachment attachment = gun.getAttachments().size() > 0 ? gun.getAttachments().get(0) : null;
        String[] lore = messageHelper.create(TranslationKey.EDIT_ATTACHMENT).split(",");

        ItemStack itemStack = new ItemStackBuilder(attachment != null ? attachment.getItemStack() : new ItemStack(Material.BARRIER))
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + messageHelper.create(TranslationKey.GUN_ATTACHMENT, new Placeholder("bg_weapon", gun.getName())))
                .setLore(ChatColor.WHITE + getItemName(attachment), lore[0], lore[1])
                .setUnbreakable(true)
                .build();

        inventory.setItem(slot, itemStack);
        attachments.put(inventory.getItem(slot), attachment);
        attachmentGun.put(inventory.getItem(slot), gun);
    }

    private Inventory buildInventory(Inventory inventory) {
        int slot = -2;
        for (ItemSlot itemSlot : ItemSlot.WEAPON_SLOTS) {
            Weapon weapon = loadout.getWeapon(itemSlot);

            String[] lore = messageHelper.create(TranslationKey.EDIT_WEAPON).split(",");

            ItemStack itemStack = new ItemStackBuilder(getItemStack(weapon))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(1)
                    .setDisplayName(ChatColor.WHITE + getDisplayName(itemSlot))
                    .setLore(ChatColor.WHITE + getItemName(weapon), lore[0], lore[1])
                    .setUnbreakable(true)
                    .build();

            inventory.setItem(slot += 2, itemStack);
            items.add(new EditLoadoutViewItem(inventory.getItem(slot), weapon, itemSlot));

            if (weapon instanceof Gun) {
                addAttachmentSlot(inventory, (Gun) weapon, slot + 9); // Add an attachment slot one row below the gun
            }
        }
        return inventory;
    }

    private View getAttachmentView(Player player, Loadout loadout, Gun gun, int attachmentNr) {
        return new SelectAttachmentView(plugin, player, loadout, gun, attachmentNr, inventory);
    }

    private String getDisplayName(ItemSlot itemSlot) {
        switch (itemSlot) {
            case FIREARM_PRIMARY:
                return messageHelper.create(TranslationKey.WEAPON_PRIMARY);
            case FIREARM_SECONDARY:
                return messageHelper.create(TranslationKey.WEAPON_SECONDARY);
            case EQUIPMENT:
                return messageHelper.create(TranslationKey.WEAPON_EQUIPMENT);
            case KNIFE:
                return messageHelper.create(TranslationKey.WEAPON_KNIFE);
        }
        return null;
    }

    private String getItemName(Item item) {
        return item != null ? item.getName() : messageHelper.create(TranslationKey.NONE_SELECTED);
    }

    private ItemStack getItemStack(Weapon weapon) {
        return weapon != null ? weapon.getItemStack() : new ItemStack(Material.BARRIER);
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (itemStack.getType() == Material.COMPASS) {
            player.openInventory(new LoadoutManagerView(plugin, player).getInventory());
            return;
        }
        if (attachments.containsKey(itemStack)) {
            Attachment attachment = attachments.get(itemStack);
            if (clickType == ClickType.LEFT) {
                player.openInventory(getAttachmentView(player, loadout, attachmentGun.get(itemStack), 0).getInventory());
            }
            if (clickType == ClickType.RIGHT) {
                attachmentGun.get(itemStack).getAttachments().remove(attachment);
                plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadout.getId(), loadout);
                player.openInventory(new EditLoadoutView(plugin, loadout).getInventory());
            }
        }
        for (EditLoadoutViewItem item : items) {
            if (itemStack.equals(item.getItemStack())) {
                Weapon weapon = item.getWeapon();
                if (clickType == ClickType.LEFT) {
                    if (weapon != null) {
                        if (weapon.getType().hasSubTypes()) {
                            player.openInventory(new WeaponsView(plugin, loadout, weapon.getType().getDefaultItemSlot(), inventory).getInventory());
                        } else {
                            List<Weapon> weapons = new ArrayList<>();
                            weapons.addAll(plugin.getKnifeConfig().getList());
                            player.openInventory(new SelectWeaponView(plugin, player, loadout, weapon.getType(), weapons, inventory).getInventory());
                        }
                    } else {
                        player.openInventory(new WeaponsView(plugin, loadout, item.getItemSlot(), inventory).getInventory());
                    }
                }
                if (clickType == ClickType.RIGHT) {
                    if (weapon != null) {
                        loadout.removeWeapon(weapon);
                        plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadout.getId(), loadout);
                    }
                    player.openInventory(new EditLoadoutView(plugin, loadout).getInventory());
                }
                break;
            }
        }
    }

    public boolean onClose() {
        return true;
    }

    public class EditLoadoutViewItem {

        private ItemSlot itemSlot;
        private ItemStack itemStack;
        private Weapon weapon;

        public EditLoadoutViewItem(ItemStack itemStack, Weapon weapon, ItemSlot itemSlot) {
            this.itemSlot = itemSlot;
            this.itemStack = itemStack;
            this.weapon = weapon;
        }

        public ItemSlot getItemSlot() {
            return itemSlot;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public Weapon getWeapon() {
            return weapon;
        }
    }
}
