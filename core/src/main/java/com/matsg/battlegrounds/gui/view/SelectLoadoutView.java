package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectLoadoutView implements View {

    public Gui gui;
    private Game game;
    private GamePlayer gamePlayer;
    private InternalsProvider internals;
    private ItemFactory<Attachment> attachmentFactory;
    private ItemFactory<Equipment> equipmentFactory;
    private ItemFactory<Firearm> firearmFactory;
    private ItemFactory<MeleeWeapon> meleeWeaponFactory;
    private LevelConfig levelConfig;
    private LoadoutFactory loadoutFactory;
    private PlayerStorage playerStorage;
    private TaskRunner taskRunner;
    private Translator translator;

    public SelectLoadoutView setAttachmentFactory(ItemFactory<Attachment> attachmentFactory) {
        this.attachmentFactory = attachmentFactory;
        return this;
    }

    public SelectLoadoutView setEquipmentFactory(ItemFactory<Equipment> equipmentFactory) {
        this.equipmentFactory = equipmentFactory;
        return this;
    }

    public SelectLoadoutView setFirearmFactory(ItemFactory<Firearm> firearmFactory) {
        this.firearmFactory = firearmFactory;
        return this;
    }

    public SelectLoadoutView setGame(Game game) {
        this.game = game;
        return this;
    }

    public SelectLoadoutView setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        return this;
    }

    public SelectLoadoutView setInternals(InternalsProvider internals) {
        this.internals = internals;
        return this;
    }

    public SelectLoadoutView setLevelConfig(LevelConfig levelConfig) {
        this.levelConfig = levelConfig;
        return this;
    }

    public SelectLoadoutView setLoadoutFactory(LoadoutFactory loadoutFactory) {
        this.loadoutFactory = loadoutFactory;
        return this;
    }

    public SelectLoadoutView setMeleeWeaponFactory(ItemFactory<MeleeWeapon> meleeWeaponFactory) {
        this.meleeWeaponFactory = meleeWeaponFactory;
        return this;
    }

    public SelectLoadoutView setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
        return this;
    }

    public SelectLoadoutView setTaskRunner(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
        return this;
    }

    public SelectLoadoutView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public void guiClose(InventoryCloseEvent event) {
        if (gamePlayer.getSelectedLoadout() != null) {
            return;
        }
        taskRunner.runTaskLater(() -> gui.show(event.getPlayer()), 1);
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateLoadouts(OutlinePane pane) {
        for (Map<String, String> loadoutSetup : playerStorage.getStoredPlayer(gamePlayer.getUUID()).getLoadoutSetups()) {
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
                    game,
                    gamePlayer,
                    true
            );

            int levelUnlocked = levelConfig.getLevelUnlocked(loadout.getName());
            boolean locked = levelUnlocked > levelConfig.getLevel(playerStorage.getStoredPlayer(gamePlayer.getUUID()).getExp());

            String displayName = locked ? translator.translate(TranslationKey.ITEM_LOCKED.getPath(), new Placeholder("bg_level", levelConfig.getLevelUnlocked(loadout.getName()))) : ChatColor.WHITE + loadout.getName();

            ItemStack itemStack = new ItemStackBuilder(locked ? new ItemStack(Material.BARRIER) : getLoadoutItemStack(loadout))
                    .addItemFlags(ItemFlag.values())
                    .setAmount(loadout.getLoadoutNr())
                    .setDisplayName(displayName)
                    .setLore(new String[0])
                    .setUnbreakable(true)
                    .build();

            pane.addItem(new GuiItem(itemStack, event -> {
                Player player = (Player) event.getWhoClicked();

                if (!locked) {
                    if (loadout.equals(gamePlayer.getLoadout())) {
                        internals.sendActionBar(player, translator.translate(TranslationKey.ACTIONBAR_SAME_LOADOUT.getPath()));
                        player.closeInventory();
                        return;
                    }

                    if (gamePlayer.getLoadout() != null || game.getTimeControl().getTime() > 10) {
                        String actionBar = translator.translate(TranslationKey.CHANGE_LOADOUT.getPath());
                        internals.sendActionBar(player, actionBar);
                    }

                    game.getPlayerManager().changeLoadout(gamePlayer, loadout);
                    gamePlayer.setSelectedLoadout(loadout);
                    player.closeInventory();
                } else {
                    event.setCancelled(true);
                }
            }));
        }
    }

    private ItemStack getLoadoutItemStack(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null && weapon.getItemStack() != null) {
                return weapon.getItemStack().clone();
            }
        }
        return new ItemStack(Material.BARRIER);
    }
}
