package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BattleMeleeWeapon extends BattleWeapon implements MeleeWeapon {

    private boolean droppable;
    private boolean throwing;
    private boolean throwable;
    private double damage;
    private EventDispatcher eventDispatcher;
    private int amount;
    private int cooldown;
    private int maxAmount;
    private ItemType itemType;
    private List<Item> droppedItems;

    public BattleMeleeWeapon(
            ItemMetadata metadata,
            ItemStack itemStack,
            InternalsProvider internals,
            TaskRunner taskRunner,
            EventDispatcher eventDispatcher,
            ItemType itemType,
            double damage,
            int amount,
            boolean throwable,
            int cooldown
    ) {
        super(metadata, itemStack, internals, taskRunner);
        this.amount = amount;
        this.cooldown = cooldown;
        this.damage = damage;
        this.eventDispatcher = eventDispatcher;
        this.itemType = itemType;
        this.droppable = true;
        this.droppedItems = new ArrayList<>();
        this.maxAmount = amount;
        this.throwable = throwable;
        this.throwing = false;
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
        return itemType;
    }

    public boolean isDroppable() {
        return droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }

    public boolean isThrowable() {
        return throwable;
    }

    public void cooldown(int cooldownDuration) {
        taskRunner.runTaskLater(() -> throwing = false, cooldownDuration);
    }

    public double damage(BattleEntity entity) {
        double damage = this.damage;
        double throwablePenalty = 2.0;

        if (throwable) {
            damage /= throwablePenalty;
        }

        return damage(entity, damage);
    }

    private double damage(BattleEntity entity, double damage) {
        if (context.hasBloodEffectDisplay(entity.getEntityType())) {
            gamePlayer.getLocation().getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }

        int pointsPerKill = 130;

        Event event;

        if (entity.getHealth() - damage <= 0) {
            event = new GamePlayerKillEntityEvent(game, gamePlayer, entity, this, Hitbox.TORSO, pointsPerKill);
        } else {
            event = new GamePlayerDamageEntityEvent(game, gamePlayer, entity, this, damage, Hitbox.TORSO);
        }

        eventDispatcher.dispatchExternalEvent(event);

        return entity.getHealth();
    }

    public void handleTransaction(Transaction transaction) {
        this.context = transaction.getGame().getGameMode();
        this.game = transaction.getGame();
        this.gamePlayer = transaction.getGamePlayer();
        this.itemSlot = ItemSlot.fromSlot(transaction.getSlot());

        amount = maxAmount;
        game.getItemRegistry().addItem(this);
        gamePlayer.getLoadout().setMeleeWeapon(this);

        update();
    }

    public boolean isInUse() {
        return throwing;
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

    public void onLeftClick(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    public boolean onPickUp(GamePlayer gamePlayer, Item itemEntity) {
        if (this.gamePlayer != gamePlayer) {
            return true;
        }
        itemEntity.remove();
        amount++;
        droppedItems.remove(itemEntity);
        update();
        for (Sound sound : BattleSound.ITEM_EQUIP) {
            sound.play(game, itemEntity.getLocation());
        }
        return true;
    }

    public void onRightClick(PlayerInteractEvent event) {
        if (throwing || !throwable || amount < 0) {
            return;
        }
        shoot();
        event.setCancelled(true);
    }

    public void onSwap(PlayerSwapHandItemsEvent event) { }

    public void onSwitch(PlayerItemHeldEvent event) { }

    public void remove() {
        super.remove();
        for (Item item : droppedItems) {
            item.remove();
        }
    }

    public void resetState() {
        amount = maxAmount;
    }

    public void resupply() {
        amount = maxAmount;
    }

    public void shoot() {
        amount--;
        throwing = true;

        Item item = game.getArena().getWorld().dropItem(gamePlayer.getPlayer().getEyeLocation(), new ItemStackBuilder(itemStack.clone()).setAmount(1).build());
        item.setPickupDelay(20);
        item.setVelocity(gamePlayer.getPlayer().getEyeLocation().getDirection().multiply(2.0));

        droppedItems.add(item);

        cooldown(cooldown);
        update();

        BattleSound.KNIFE_THROW.play(game, gamePlayer.getPlayer().getLocation());

        taskRunner.runTaskTimer(new BukkitRunnable() {
            Location location;
            public void run() {
                BattleEntity[] entities = context.getNearbyEnemies(item.getLocation(), gamePlayer.getTeam(), 2.0);

                if (entities.length > 0) {
                    BattleEntity entity = entities[0];

                    if (entity == null || entity == gamePlayer || entity.getBukkitEntity().isDead()) {
                        return;
                    }

                    item.remove();
                    damage(entity, damage);
                    cancel();
                }
                if (location != null && item.getLocation().equals(location)) {
                    cancel(); // Cancel player searching loop
                }
                location = item.getLocation();
            }
        }, 0, 1);
    }

    public boolean update() {
        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(amount)
                .setUnbreakable(true)
                .build();
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().setItem(itemSlot.getSlot(), itemStack);
        }
        return gamePlayer != null;
    }
}
