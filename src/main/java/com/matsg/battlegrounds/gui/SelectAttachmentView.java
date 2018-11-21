package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.Loadout;
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectAttachmentView implements View {

    private Battlegrounds plugin;
    private Gun gun;
    private int attachmentNr;
    private Inventory inventory, previous;
    private Loadout loadout;
    private Map<ItemStack, Attachment> attachments;
    private Player player;

    public SelectAttachmentView(Battlegrounds plugin, Player player, Loadout loadout, Gun gun, int attachmentNr) {
        this.plugin = plugin;
        this.attachmentNr = attachmentNr;
        this.attachments = new HashMap<>();
        this.gun = gun;
        this.loadout = loadout;
        this.player = player;

        inventory = buildInventory(plugin.getServer().createInventory(this, 27, Message.create(TranslationKey.VIEW_SELECT_ATTACHMENT, new Placeholder("bg_weapon", gun.getName()))));
        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(Message.create(TranslationKey.GO_BACK)).build());
    }

    public SelectAttachmentView(Battlegrounds plugin, Player player, Loadout loadout, Gun gun, int attachmentNr, Inventory previous) {
        this(plugin, player, loadout, gun, attachmentNr);
        this.previous = previous;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory buildInventory(Inventory inventory) {
        int slot = -1;
        for (Attachment attachment : sortAttachments(getAttachmentList(gun))) {
            if (attachment != null) {
                ItemStack itemStack = plugin.getLevelConfig().getLevelUnlocked(attachment.getName())
                        <= plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getExp())
                        ? getUnlockedItemStack(attachment) : getLockedItemStack(attachment);

                inventory.setItem(++ slot, itemStack);
                attachments.put(inventory.getItem(slot), attachment);
            }
        }
        return inventory;
    }

    private List<Attachment> getAttachmentList(Gun gun) {
        List<Attachment> list = new ArrayList<>();
        for (String attachmentId : gun.getCompatibleAttachments()) {
            Attachment attachment = plugin.getAttachmentConfig().get(attachmentId);
            if (attachment != null) {
                list.add(attachment);
            }
        }
        return list;
    }

    private String[] getAttachmentLore(String arg) {
        List<String> lore = new ArrayList<>();
        int maxLength = 30;
        Pattern pattern = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(arg);
        while (matcher.find()) {
            lore.add(ChatColor.GRAY + matcher.group(1));
        }
        return lore.toArray(new String[lore.size()]);
    }

    private ItemStack getLockedItemStack(Attachment attachment) {
        return new ItemStackBuilder(Material.BARRIER)
                .addItemFlags(ItemFlag.values())
                .setDisplayName(Message.create(TranslationKey.ITEM_LOCKED, new Placeholder("bg_level", plugin.getLevelConfig().getLevelUnlocked(attachment.getName()))))
                .setUnbreakable(true)
                .build();
    }

    private ItemStack getUnlockedItemStack(Attachment attachment) {
        return new ItemStackBuilder(attachment.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + attachment.getName())
                .setLore(getAttachmentLore(attachment.getDescription()))
                .setUnbreakable(true)
                .build();
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getType() == Material.BARRIER) {
            return;
        }
        if (itemStack.getType() == Material.COMPASS && previous != null) {
            player.openInventory(previous);
        }
        Attachment attachment = attachments.get(itemStack);
        if (attachment == null) {
            return;
        }
        gun.getAttachments().clear();
        gun.getAttachments().add(attachmentNr, attachment);
        plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadout.getId(), loadout);
        player.openInventory(new EditLoadoutView(plugin, loadout).getInventory());
    }

    public boolean onClose() {
        return true;
    }

    private List<Attachment> sortAttachments(Collection<Attachment> unsorted) {
        List<Attachment> list = new ArrayList<>(unsorted);
        Collections.sort(list, new Comparator<Attachment>() {
            public int compare(Attachment o1, Attachment o2) {
                return Integer.valueOf(plugin.getLevelConfig().getLevelUnlocked(o1.getName())).compareTo(plugin.getLevelConfig().getLevelUnlocked(o2.getName()));
            }
        });
        return list;
    }
}