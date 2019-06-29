package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.api.item.MeleeWeapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
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

    public BattleMeleeWeapon(
            Battlegrounds plugin,
            String id,
            String name,
            String description,
            ItemStack itemStack,
            double damage,
            int amount,
            boolean throwable,
            int cooldown
    ) {
        super(plugin, id, name, description, itemStack);
        this.amount = amount;
        this.cooldown = cooldown;
        this.damage = damage;
        this.droppedItems = new ArrayList<>();
        this.maxAmount = amount;
        this.throwable = throwable;
        this.throwing = false;

        type = translator.translate(TranslationKey.ITEM_TYPE_MELEE_WEAPON);
    }

    public MeleeWeapon clone() {
        return (MeleeWeapon) super.clone();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCooldown() {
        return cooldown;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
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

    public void cooldown(int time) {
        new BattleRunnable() {
            public void run() {
                throwing = false;
            }
        }.runTaskLater(time);
    }

    public double damage(BattleEntity entity) {
        double damage = this.damage;
        if (throwable) {
            damage /= 2;
        }
        return damage(entity, damage);
    }

    private double damage(BattleEntity entity, double damage) {
        if (plugin.getBattlegroundsConfig().getDisplayBloodEffect(entity.getEntityType().toString())) {
            gamePlayer.getLocation().getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }

        int pointsPerKill = 130;

        Event event;

        if (entity.getHealth() - damage <= 0) {
            event = new GamePlayerKillEntityEvent(game, gamePlayer, entity, this, Hitbox.TORSO, pointsPerKill);
        } else {
            event = new GamePlayerDamageEntityEvent(game, gamePlayer, entity, this, damage, Hitbox.TORSO);
        }

        // Handle the event on the plugin manager so other plugins can listen to this event as well
        plugin.getServer().getPluginManager().callEvent(event);
        // Handle the event on the event dispatcher so we can reuse the event without calling a listener to it
        plugin.getEventDispatcher().dispatchEvent(event);

        return entity.getHealth();
    }

    private String[] getLore() {
        return new String[] {
                ChatColor.WHITE + type
        };
    }

    public void handleTransaction(Transaction transaction) {
        this.gamePlayer = transaction.getGamePlayer();
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
                BattleEntity[] entities = context.getNearbyEntities(item.getLocation(), gamePlayer.getTeam(), 2.0);

                if (entities.length > 0) {
                    BattleEntity entity = entities[0];

                    if (entity == null || entity == gamePlayer || entity.getBukkitEntity().isDead()) {
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
        String displayName = translator.createSimpleMessage(plugin.getBattlegroundsConfig().getWeaponDisplayName("melee-weapon"), placeholder);

        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(amount)
                .setDisplayName(displayName)
                .setLore(getLore())
                .setUnbreakable(true)
                .build();
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().setItem(itemSlot.getSlot(), itemStack);
        }
        return gamePlayer != null;
    }
}
