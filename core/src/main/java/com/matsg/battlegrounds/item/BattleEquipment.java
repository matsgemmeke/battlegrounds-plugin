package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Transaction;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystem;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleEquipment extends BattleWeapon implements Equipment {

    private boolean droppable;
    private boolean beingThrown;
    private double velocity;
    private EquipmentType equipmentType;
    private IgnitionSystem ignitionSystem;
    private int amount;
    private int cooldown;
    private int ignitionTime;
    private int maxAmount;
    private int supplyAmount;
    protected double longRange;
    protected double midRange;
    protected double shortRange;
    protected List<Item> droppedItems;
    protected Sound[] ignitionSound;

    public BattleEquipment(
            ItemMetadata metadata,
            ItemStack itemStack,
            InternalsProvider internals,
            TaskRunner taskRunner,
            EquipmentType equipmentType,
            IgnitionSystem ignitionSystem,
            Sound[] ignitionSound,
            int amount,
            int maxAmount,
            int cooldown,
            int ignitionTime,
            double longRange,
            double midRange,
            double shortRange,
            double velocity
    ) {
        super(metadata, itemStack, internals, taskRunner);
        this.equipmentType = equipmentType;
        this.ignitionSystem = ignitionSystem;
        this.ignitionSound = ignitionSound;
        this.amount = amount;
        this.maxAmount = maxAmount;
        this.supplyAmount = amount;
        this.cooldown = cooldown;
        this.ignitionTime = ignitionTime;
        this.longRange = longRange;
        this.midRange = midRange;
        this.shortRange = shortRange;
        this.velocity = velocity;
        this.beingThrown = false;
        this.droppable = true;
        this.droppedItems = new ArrayList<>();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Item> getDroppedItems() {
        return droppedItems;
    }

    public Item getFirstDroppedItem() {
        return droppedItems.get(0);
    }

    public Sound[] getIgnitionSound() {
        return ignitionSound;
    }

    public int getIgnitionTime() {
        return ignitionTime;
    }

    public double getLongRange() {
        return longRange;
    }

    public void setLongRange(double longRange) {
        this.longRange = longRange;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public double getMidRange() {
        return midRange;
    }

    public void setMidRange(double midRange) {
        this.midRange = midRange;
    }

    public double getShortRange() {
        return shortRange;
    }

    public void setShortRange(double shortRange) {
        this.shortRange = shortRange;
    }

    public EquipmentType getType() {
        return equipmentType;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public boolean isBeingThrown() {
        return beingThrown;
    }

    public boolean isDroppable() {
        return droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }

    public void cooldown(int cooldownDuration) {
        taskRunner.runTaskLater(() -> beingThrown = false, cooldownDuration);
    }

    public BattleEquipment clone() {
        BattleEquipment equipment = (BattleEquipment) super.clone();
        equipment.ignitionSystem.setWeapon(equipment);
        return equipment;
    }

    public void deployEquipment() {
        deployEquipment(velocity);
    }

    public void deployEquipment(double velocity) {
        Item item = gamePlayer.getPlayer().getWorld().dropItem(gamePlayer.getPlayer().getEyeLocation(), new ItemStackBuilder(itemStack.clone()).setAmount(1).build());
        item.setPickupDelay(1000);
        item.setVelocity(gamePlayer.getPlayer().getEyeLocation().getDirection().multiply(velocity));

        amount--;
        beingThrown = true;
        droppedItems.add(item);
        ignitionSystem.igniteItem(item);

        cooldown(cooldown);
        update();

        BattleSound.EXPLOSIVE_THROW.play(game, item.getLocation());
    }

    public void handleTransaction(Transaction transaction) {
        this.context = transaction.getGame().getGameMode();
        this.game = transaction.getGame();
        this.gamePlayer = transaction.getGamePlayer();
        this.itemSlot = ItemSlot.fromSlot(transaction.getSlot());

        amount = maxAmount;
        game.getItemRegistry().addItem(this);
        gamePlayer.getLoadout().setEquipment(this);

        update();
    }

    public boolean isInUse() {
        return beingThrown;
    }

    public boolean isRelated(ItemStack itemStack) {
        for (Item item : droppedItems) {
            if (item.getItemStack() == itemStack) {
                return true;
            }
        }
        return false;
    }

    public boolean onDrop(GamePlayer gamePlayer, Item item) {
        return true;
    }

    public void onLeftClick(PlayerInteractEvent event) {
        if (amount <= 0 || beingThrown || game == null || gamePlayer == null) {
            return;
        }
        deployEquipment(velocity);
        event.setCancelled(true);
    }

    public boolean onPickUp(GamePlayer gamePlayer, Item item) {
        return true;
    }

    public void onRightClick(PlayerInteractEvent event) {
        if (amount <= 0 || beingThrown || game == null || gamePlayer == null) {
            return;
        }
        deployEquipment(0.0);
        event.setCancelled(true);
        event.getPlayer().updateInventory();
    }

    public void onSwap(PlayerSwapHandItemsEvent event) { }

    public void onSwitch(PlayerItemHeldEvent event) { }

    public void remove() {
        super.remove();
        for (Item item : droppedItems) {
            item.remove();
        }
        droppedItems.clear();
    }

    public void resetState() {
        amount = supplyAmount;
    }

    public void resupply() {
        amount = maxAmount;
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
