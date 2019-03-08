package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BattleMeleeWeapon extends BattleWeapon implements MeleeWeapon {

    private boolean throwing, throwable;
    private double damage;
    private int amount, cooldown, maxAmount;
    private List<Item> droppedItems;
    private String type;

    public BattleMeleeWeapon(String id, String name, String description, ItemStack itemStack, short durability, double damage, int amount, boolean throwable, int cooldown) {
        super(id, name, description, itemStack, durability);
        this.amount = amount;
        this.cooldown = cooldown;
        this.damage = damage;
        this.droppedItems = new ArrayList<>();
        this.maxAmount = amount;
        this.throwable = throwable;
        this.throwing = false;
        this.type = messageHelper.create(TranslationKey.ITEM_TYPE_MELEE_WEAPON);
    }

    public MeleeWeapon clone() {
        return (MeleeWeapon) super.clone();
    }

    public int getAmount() {
        return amount;
    }

    public int getCooldown() {
        return cooldown;
    }

    public double getDamage() {
        return damage;
    }

    public List<Item> getDroppedItems() {
        return droppedItems;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public ItemType getType() {
        return new ItemType() {
            public ItemSlot getDefaultItemSlot() {
                return ItemSlot.MELEE_WEAPON;
            }

            public String getName() {
                return type;
            }

            public boolean hasSubTypes() {
                return false;
            }

            public boolean isRemovable() {
                return false;
            }
        };
    }

    public boolean isThrowable() {
        return throwable;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void cooldown(int time) {
        new BattleRunnable() {
            public void run() {
                throwing = false;
            }
        }.runTaskLater(time);
    }

    public double damage(GamePlayer gamePlayer) {
        double damage = this.damage;
        if (throwable) {
            damage /= 2;
        }
        return damage(gamePlayer, damage);
    }

    private double damage(GamePlayer gamePlayer, double damage) {
        game.getPlayerManager().damagePlayer(gamePlayer, damage, plugin.getBattlegroundsConfig().displayBloodEffect);
        if (gamePlayer.getPlayer().isDead()) {
            plugin.getServer().getPluginManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this, Hitbox.TORSO));
            game.getGameMode().onKill(gamePlayer, this.gamePlayer, this, Hitbox.TORSO);
        }
        return gamePlayer.getPlayer().getHealth();
    }

    private String[] getLore() {
        return new String[] {
                ChatColor.WHITE + type
        };
    }

    public boolean isRelated(ItemStack itemStack) {
        for (Item item : droppedItems) {
            if (item.getItemStack().equals(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDrop(GamePlayer gamePlayer, Item item) {
        return true;
    }

    public void onLeftClick() { }

    public boolean onPickUp(GamePlayer gamePlayer, Item itemEntity) {
        if (this.gamePlayer != gamePlayer) {
            return true;
        }
        itemEntity.remove();
        amount ++;
        droppedItems.remove(itemEntity);
        update();
        for (Sound sound : BattleSound.ITEM_EQUIP) {
            sound.play(game, itemEntity.getLocation());
        }
        return true;
    }

    public void onRightClick() {
        if (throwing || !throwable || amount < 0) {
            return;
        }
        shoot();
    }

    public void onSwap() { }

    public void onSwitch() { }

    public void remove() {
        super.remove();
        for (Item item : droppedItems) {
            item.remove();
        }
    }

    public void resetState() {
        amount = maxAmount;
    }

    public void shoot() {
        amount --;
        throwing = true;

        Item item = game.getArena().getWorld().dropItem(gamePlayer.getPlayer().getEyeLocation(), new ItemStackBuilder(itemStack.clone()).setAmount(1).build());
        item.setPickupDelay(20);
        item.setVelocity(gamePlayer.getPlayer().getEyeLocation().getDirection().multiply(2.0));

        droppedItems.add(item);

        cooldown(cooldown);
        update();

        BattleSound.KNIFE_THROW.play(game, gamePlayer.getPlayer().getLocation());

        new BattleRunnable() {
            Location location;
            public void run() {
                GamePlayer[] players = game.getPlayerManager().getNearbyPlayers(item.getLocation(), 2.0);
                Team team = gamePlayer.getTeam();
                if (players.length > 0) {
                    GamePlayer gamePlayer = players[0];
                    if (gamePlayer == null || gamePlayer == BattleMeleeWeapon.this.gamePlayer || gamePlayer.getPlayer().isDead() || team != null && gamePlayer.getTeam() == team) {
                        return;
                    }
                    item.remove();
                    damage(gamePlayer, damage);
                    cancel();
                }
                if (location != null && item.getLocation().equals(location)) {
                    cancel(); // Cancel player searching loop
                }
                location = item.getLocation();
            }
        }.runTaskTimer(0, 1);
    }

    public boolean update() {
        Placeholder placeholder = new Placeholder("bg_weapon", name);
        String displayName = messageHelper.createSimple(plugin.getBattlegroundsConfig().getWeaponDisplayName("melee-weapon"), placeholder);

        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(amount)
                .setDisplayName(displayName)
                .setDurability(durability)
                .setLore(getLore())
                .setUnbreakable(true)
                .build();
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().setItem(itemSlot.getSlot(), itemStack);
        }
        return gamePlayer != null;
    }
}
