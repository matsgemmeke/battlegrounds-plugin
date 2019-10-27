package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
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
    private Translator translator;

    public SelectAttachmentView(Battlegrounds plugin, Translator translator, Player player, Loadout loadout, Gun gun, int attachmentNr) {
        this.plugin = plugin;
        this.translator = translator;
        this.attachmentNr = attachmentNr;
        this.attachments = new HashMap<>();
        this.gun = gun;
        this.loadout = loadout;
        this.player = player;

        inventory = buildInventory(plugin.getServer().createInventory(this, 27, translator.translate(TranslationKey.VIEW_SELECT_ATTACHMENT.getPath(), new Placeholder("bg_weapon", gun.getMetadata().getName()))));
        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.COMPASS)).setDisplayName(translator.translate(TranslationKey.GO_BACK.getPath())).build());
    }

    public SelectAttachmentView(Battlegrounds plugin, Translator translator, Player player, Loadout loadout, Gun gun, int attachmentNr, Inventory previous) {
        this(plugin, translator, player, loadout, gun, attachmentNr);
        this.previous = previous;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private Inventory buildInventory(Inventory inventory) {
        int slot = -1;
        for (Attachment attachment : sortAttachments(getAttachmentList(gun))) {
            if (attachment != null) {
                ItemStack itemStack = plugin.getLevelConfig().getLevelUnlocked(attachment.getMetadata().getName())
                        <= plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getExp())
                        ? getUnlockedItemStack(attachment) : getLockedItemStack(attachment);

                inventory.setItem(++slot, itemStack);
                attachments.put(inventory.getItem(slot), attachment);
            }
        }
        return inventory;
    }

    private List<Attachment> getAttachmentList(Gun gun) {
        List<Attachment> list = new ArrayList<>();
        for (String attachmentId : gun.getCompatibleAttachments()) {
            Attachment attachment = plugin.getAttachmentFactory().make(attachmentId);
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
                .setDisplayName(translator.translate(TranslationKey.ITEM_LOCKED.getPath(), new Placeholder("bg_level", plugin.getLevelConfig().getLevelUnlocked(attachment.getMetadata().getName()))))
                .setUnbreakable(true)
                .build();
    }

    private ItemStack getUnlockedItemStack(Attachment attachment) {
        return new ItemStackBuilder(attachment.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + attachment.getMetadata().getName())
                .setLore(getAttachmentLore(attachment.getMetadata().getDescription()))
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
        plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadout.getLoadoutNr(), loadout.convertToMap());
        player.openInventory(new EditLoadoutView(plugin, translator, loadout).getInventory());
    }

    public void onClose(Player player) { }

    private List<Attachment> sortAttachments(Collection<Attachment> unsorted) {
        List<Attachment> list = new ArrayList<>(unsorted);
        Collections.sort(list, new Comparator<Attachment>() {
            public int compare(Attachment o1, Attachment o2) {
                return Integer.valueOf(plugin.getLevelConfig().getLevelUnlocked(o1.getMetadata().getName())).compareTo(plugin.getLevelConfig().getLevelUnlocked(o2.getMetadata().getName()));
            }
        });
        return list;
    }
}
