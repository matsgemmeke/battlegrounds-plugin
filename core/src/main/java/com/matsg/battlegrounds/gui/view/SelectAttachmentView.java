package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.ItemFactory;
import com.matsg.battlegrounds.api.item.Loadout;
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

public class SelectAttachmentView implements View {

    public Gui gui;
    private Gun gun;
    private int attachmentNr;
    private ItemFactory<Attachment> attachmentFactory;
    private LevelConfig levelConfig;
    private Loadout loadout;
    private PlayerStorage playerStorage;
    private Translator translator;
    private UUID playerUUID;
    private View previousView;

    public SelectAttachmentView setAttachmentFactory(ItemFactory<Attachment> attachmentFactory) {
        this.attachmentFactory = attachmentFactory;
        return this;
    }

    public SelectAttachmentView setAttachmentNr(int attachmentNr) {
        this.attachmentNr = attachmentNr;
        return this;
    }

    public SelectAttachmentView setGun(Gun gun) {
        this.gun = gun;
        return this;
    }

    public SelectAttachmentView setLevelConfig(LevelConfig levelConfig) {
        this.levelConfig = levelConfig;
        return this;
    }

    public SelectAttachmentView setLoadout(Loadout loadout) {
        this.loadout = loadout;
        return this;
    }

    public SelectAttachmentView setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
        return this;
    }

    public SelectAttachmentView setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
        return this;
    }

    public SelectAttachmentView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public SelectAttachmentView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateAttachments(OutlinePane pane) {
        for (Attachment attachment : sortAttachments(getAttachmentList(gun))) {
            if (levelConfig.getLevelUnlocked(attachment.getMetadata().getId()) <= levelConfig.getLevel(playerStorage.getStoredPlayer(playerUUID).getExp())) {
                pane.addItem(new GuiItem(getUnlockedItemStack(attachment), event -> {
                    gun.getAttachments().clear();
                    gun.getAttachments().add(attachmentNr, attachment);
                    playerStorage.getStoredPlayer(playerUUID).saveLoadout(loadout.getLoadoutNr(), loadout.convertToMap());
                    previousView.openInventory(event.getWhoClicked());
                }));
            } else {
                pane.addItem(new GuiItem(getLockedItemStack(attachment), event -> {
                    event.setCancelled(true);
                }));
            }
        }
    }

    private List<Attachment> getAttachmentList(Gun gun) {
        List<Attachment> list = new ArrayList<>();
        for (String attachmentId : gun.getCompatibleAttachments()) {
            Attachment attachment = attachmentFactory.make(attachmentId);
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
                .setDisplayName(translator.translate(TranslationKey.ITEM_LOCKED.getPath(), new Placeholder("bg_level", levelConfig.getLevelUnlocked(attachment.getMetadata().getId()))))
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

    private List<Attachment> sortAttachments(Collection<Attachment> unsorted) {
        List<Attachment> list = new ArrayList<>(unsorted);
        Collections.sort(list, (o1, o2) -> Integer.compare(levelConfig.getLevelUnlocked(o1.getMetadata().getId()), levelConfig.getLevelUnlocked(o2.getMetadata().getId())));
        return list;
    }
}
