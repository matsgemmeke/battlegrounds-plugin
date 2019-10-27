package com.matsg.battlegrounds.gui.view;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectLoadoutView implements View {

    private Battlegrounds plugin;
    private Game game;
    private GamePlayer gamePlayer;
    private Inventory inventory;
    private LoadoutFactory loadoutFactory;
    private List<SelectLoadoutViewItem> items;
    private Translator translator;

    public SelectLoadoutView(Battlegrounds plugin, Translator translator, Game game, GamePlayer gamePlayer) {
        this.plugin = plugin;
        this.translator = translator;
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.items = new ArrayList<>();
        this.inventory = plugin.getServer().createInventory(this, 27, translator.translate(TranslationKey.VIEW_SELECT_LOADOUT.getPath()));
        this.loadoutFactory = new LoadoutFactory();

        addLoadouts(game, gamePlayer);
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

    private SelectLoadoutViewItem getViewItem(ItemStack itemStack) {
        for (SelectLoadoutViewItem item : items) {
            if (item.getItemStack().equals(itemStack)) {
                return item;
            }
        }
        return null;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        SelectLoadoutViewItem item = getViewItem(itemStack);

        if (gamePlayer == null || itemStack == null || item == null || item.getLoadout() == null || item.isLocked()) {
            return;
        }

        Loadout loadout = item.getLoadout();

        if (loadout.equals(gamePlayer.getLoadout())) {
            ActionBar.SAME_LOADOUT.send(player);
            player.closeInventory();
            return;
        }

        game.getPlayerManager().changeLoadout(gamePlayer, loadout.clone(), gamePlayer.getLoadout() == null || game.getTimeControl().getTime() <= 10);
        gamePlayer.setSelectedLoadout(loadout);
        player.closeInventory(); // Call this after the loadout has been selected
    }

    public void onClose(Player player) {
        if (gamePlayer.getSelectedLoadout() != null) {
            return;
        }

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(inventory), 1);
    }

    private void addLoadouts(Game game, GamePlayer gamePlayer) {
        for (Map<String, String> loadoutSetup : plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getLoadoutSetups()) {
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
                    game,
                    gamePlayer
            );

            int levelUnlocked = plugin.getLevelConfig().getLevelUnlocked(loadout.getName());
            boolean locked = levelUnlocked > plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getExp());

            String displayName = locked ? translator.translate(TranslationKey.ITEM_LOCKED.getPath(), new Placeholder("bg_level", plugin.getLevelConfig().getLevelUnlocked(loadout.getName()))) : ChatColor.WHITE + loadout.getName();

            ItemStack itemStack = new ItemStackBuilder(locked ? new ItemStack(Material.BARRIER) : getLoadoutItemStack(loadout))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(loadout.getLoadoutNr())
                    .setDisplayName(displayName)
                    .setLore(new String[0])
                    .setUnbreakable(true)
                    .build();

            inventory.setItem(loadout.getLoadoutNr() + 10, itemStack);
            items.add(new SelectLoadoutViewItem(inventory.getItem(loadout.getLoadoutNr() + 10), loadout, locked));
        }
    }

    public class SelectLoadoutViewItem {

        private boolean locked;
        private ItemStack itemStack;
        private Loadout loadout;

        public SelectLoadoutViewItem(ItemStack itemStack, Loadout loadout, boolean locked) {
            this.itemStack = itemStack;
            this.loadout = loadout;
            this.locked = locked;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public Loadout getLoadout() {
            return loadout;
        }

        public boolean isLocked() {
            return locked;
        }
    }
}
