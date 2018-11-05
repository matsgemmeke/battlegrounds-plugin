package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.util.ActionBar;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SelectLoadoutView implements View {

    private static Battlegrounds plugin = BattlegroundsPlugin.getPlugin();
    private Game game;
    private GamePlayer gamePlayer;
    private Inventory inventory;
    private Map<ItemStack, Loadout> loadouts;

    public SelectLoadoutView(Game game, GamePlayer gamePlayer) {
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.inventory = plugin.getServer().createInventory(this, 27, Message.create(TranslationKey.VIEW_SELECT_LOADOUT));
        this.loadouts = new HashMap<>();

        for (Loadout loadout : plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getLoadouts()) {
            addLoadout(loadout);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void addLoadout(Loadout loadout) {
        int levelUnlocked = plugin.getLevelConfig().getLevelUnlocked(loadout.getName());
        boolean locked = levelUnlocked > plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).getExp());

        String displayName = locked ? Message.create(TranslationKey.ITEM_LOCKED, new Placeholder("bg_level", plugin.getLevelConfig().getLevelUnlocked(loadout.getName()))) : ChatColor.WHITE + loadout.getName();

        ItemStack itemStack = new ItemStackBuilder(locked ? new ItemStack(Material.BARRIER) : getLoadoutItemStack(loadout))
                .addItemFlags(ItemFlag.values())
                .setAmount(loadout.getId())
                .setDisplayName(displayName)
                .setLore(new String[0])
                .setUnbreakable(true)
                .build();

        inventory.setItem(loadout.getId() + 10, itemStack);
        loadouts.put(inventory.getItem(loadout.getId() + 10), loadout);
    }

    private ItemStack getLoadoutItemStack(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null && weapon.getItemStack() != null) {
                return weapon.getItemStack();
            }
        }
        return null;
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Loadout loadout = loadouts.get(itemStack);
        if (game == null || !game.getState().isInProgress() || gamePlayer == null || loadout == null || itemStack == null || itemStack.getType() == Material.BARRIER) {
            return;
        }
        if (loadout.equals(gamePlayer.getLoadout())) {
            ActionBar.SAME_LOADOUT.send(player);
            player.closeInventory();
            return;
        }
        game.getPlayerManager().changeLoadout(gamePlayer, loadout, gamePlayer.getLoadout() == null || game.getTimeControl().getTime() <= 10);
        player.closeInventory();
    }

    public boolean onClose() {
        return gamePlayer.getLoadout() != null || !game.getState().isInProgress();
    }
}
