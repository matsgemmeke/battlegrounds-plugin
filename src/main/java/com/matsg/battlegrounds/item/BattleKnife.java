package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.item.WeaponType;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class BattleKnife extends BattleWeapon implements Knife {

    private boolean throwing, throwable;
    private double damage;
    private int amount, cooldown, maxAmount;
    private String type;

    public BattleKnife(String name, String description, ItemStack itemStack, short durability, double damage, int amount, boolean throwable, int cooldown) {
        super(name, description, itemStack, durability);
        this.amount = amount;
        this.cooldown = cooldown;
        this.damage = damage;
        this.maxAmount = amount;
        this.throwable = throwable;
        this.throwing = false;
        this.type = EnumMessage.TYPE_KNIFE.getMessage();
    }

    public Knife clone() {
        return (Knife) super.clone();
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

    public int getMaxAmount() {
        return maxAmount;
    }

    public WeaponType getType() {
        return new WeaponType() {
            public ItemSlot getDefaultItemSlot() {
                return ItemSlot.KNIFE;
            }

            public String getName() {
                return type;
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

    private String[] getLore() {
        return new String[] {
                "ยงf" + type,
                "ยง7" + format(6, damage, 75.0) + " " + EnumMessage.STAT_DAMAGE.getMessage()
        };
    }

    public void onLeftClick() { }

    public void onPickUp(GamePlayer gamePlayer, Item itemEntity) {
        itemEntity.remove();
        amount ++;
        update();
        for (Sound sound : BattleSound.ITEM_EQUIP) {
            sound.play(game, itemEntity.getLocation());
        }
    }

    public void onRightClick() {
        if (throwing || !throwable || amount <= 1) {
            return;
        }
        shoot();
    }

    public void onSwitch() { }

    public void refillAmmo() {
        amount = maxAmount;
    }

    public void shoot() {
        amount --;
        throwing = true;

        Item item = game.getArena().getWorld().dropItem(gamePlayer.getPlayer().getEyeLocation(), new ItemStackBuilder(itemStack.clone()).setAmount(1).build());
        item.setPickupDelay(20);
        item.setVelocity(gamePlayer.getPlayer().getEyeLocation().getDirection().multiply(3.0));

        cooldown(cooldown);
        update();

        BattleSound.KNIFE_THROW.play(game, gamePlayer.getPlayer().getLocation());

        new BattleRunnable() {
            Location location;
            public void run() {
                GamePlayer[] players = game.getPlayerManager().getNearbyPlayers(item.getLocation(), 1.0);
                if (players.length > 0) {
                    item.remove();

                    players[0].getLocation().getWorld().playEffect(players[0].getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

                    game.getPlayerManager().damagePlayer(gamePlayer, damage);
                    cancel();
                }
                if (location != null && item.getLocation().equals(location)) {
                    cancel();
                }
                location = item.getLocation();
            }
        }.runTaskTimer(0, 1);
    }

    public boolean update() {
        Placeholder placeholder = new Placeholder("bg_weapon", name);
        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(amount)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', Placeholder.replace(plugin.getBattlegroundsConfig().getWeaponDisplayName("knife"), placeholder)))
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