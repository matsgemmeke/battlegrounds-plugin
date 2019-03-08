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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectWeaponView implements View {

    private Battlegrounds plugin;
    private Inventory inventory, previous;
    private Loadout loadout;
    private Map<ItemStack, Weapon> weapons;
    private MessageHelper messageHelper;
    private Player player;

    public SelectWeaponView(Battlegrounds plugin, Player player, Loadout loadout, ItemType itemType, List<Weapon> weapons) {
        this.plugin = plugin;
        this.loadout = loadout;
        this.player = player;
        this.messageHelper = new MessageHelper();
        this.weapons = new HashMap<>();

        this.inventory = plugin.getServer().createInventory(this, 27, itemType.getName());

        Collections.sort(weapons, new Comparator<Weapon>() {
            public int compare(Weapon o1, Weapon o2){
                return Integer.valueOf(plugin.getLevelConfig().getLevelUnlocked(o1.getName())).compareTo(plugin.getLevelConfig().getLevelUnlocked(o2.getName()));
            }
        });

        int slot = 0;

        for (Weapon weapon : weapons) {
            weapon.update();
            addWeapon(weapon, slot ++);
        }

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(messageHelper.create(TranslationKey.GO_BACK)).build());
    }

    public SelectWeaponView(Battlegrounds plugin, Player player, Loadout loadout, ItemType itemType, List<Weapon> weapons, Inventory previous) {
        this(plugin, player, loadout, itemType, weapons);
        this.previous = previous;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void addWeapon(Weapon weapon, int slot) {
        ItemStack itemStack = plugin.getLevelConfig().getLevelUnlocked(weapon.getName())
                <= plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getExp())
                ? getUnlockedItemStack(weapon) : getLockedItemStack(weapon);

        inventory.setItem(slot, itemStack);
        weapons.put(inventory.getItem(slot), weapon);
    }

    private ItemStack getLockedItemStack(Weapon weapon) {
        return new ItemStackBuilder(Material.BARRIER)
                .addItemFlags(ItemFlag.values())
                .setDisplayName(messageHelper.create(TranslationKey.ITEM_LOCKED, new Placeholder("bg_level", plugin.getLevelConfig().getLevelUnlocked(weapon.getName()))))
                .setUnbreakable(true)
                .build();
    }

    private ItemStack getUnlockedItemStack(Weapon weapon) {
        return new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + weapon.getName())
                .setLore(getLore(weapon))
                .setUnbreakable(true)
                .build();
    }

    private String[] getLore(Weapon weapon) {
        List<String> lore = new ArrayList<>(weapon.getItemStack().getItemMeta().getLore());
        lore.add(" ");
        int maxLength = 30;
        Pattern pattern = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(weapon.getDescription());
        while (matcher.find()) {
            lore.add(ChatColor.WHITE + matcher.group(1));
        }
        return lore.toArray(new String[lore.size()]);
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getType() == Material.BARRIER) {
            return;
        }
        if (itemStack.getType() == Material.COMPASS && previous != null) {
            player.openInventory(previous);
        }
        Weapon weapon = weapons.get(itemStack);
        if (weapon == null || loadout == null) {
            return;
        }
        replaceWeapon(loadout, weapon);
        plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadout.getId(), loadout);
        player.openInventory(new EditLoadoutView(plugin, loadout).getInventory());
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

    public boolean onClose() {
        return true;
    }
}
