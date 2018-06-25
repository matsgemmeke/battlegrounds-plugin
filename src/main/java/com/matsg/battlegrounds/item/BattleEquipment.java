package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleEquipment extends BattleWeapon implements Equipment {

    protected boolean beingThrown;
    protected double longRange, midRange, shortRange, velocity;
    protected EquipmentType type;
    protected IgnitionType ignitionType;
    protected int amount, cooldown, ignitionTime, maxAmount;
    protected List<Item> droppedItems;
    protected Sound[] ignitionSound;

    public BattleEquipment(String name, String description, ItemStack itemStack, short durability, EquipmentType type, int amount,
                            double longRange, double midRange, double shortRange, double velocity,
                            IgnitionType ignitionType, int ignitionTime, Sound[] ignitionSound) {
        super(name, description, itemStack, durability);
        this.amount = amount;
        this.beingThrown = false;
        this.cooldown = plugin.getBattlegroundsConfig().explosiveCooldown;
        this.droppedItems = new ArrayList<>();
        this.ignitionSound = ignitionSound;
        this.ignitionTime = ignitionTime;
        this.ignitionType = ignitionType;
        this.longRange = longRange;
        this.maxAmount = amount;
        this.midRange = midRange;
        this.shortRange = shortRange;
        this.type = type;
        this.velocity = velocity;
    }

    public Equipment clone() {
        return (Equipment) super.clone();
    }

    public int getAmount() {
        return amount;
    }

    public List<Item> getDroppedItems() {
        return droppedItems;
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

    public int getMaxAmount() {
        return maxAmount;
    }

    public double getMidRange() {
        return midRange;
    }

    public double getShortRange() {
        return shortRange;
    }

    public EquipmentType getType() {
        return type;
    }

    public double getVelocity() {
        return velocity;
    }

    public boolean isBeingThrown() {
        return beingThrown;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setLongRange(double longRange) {
        this.longRange = longRange;
    }

    public void setMidRange(double midRange) {
        this.midRange = midRange;
    }

    public void setShortRange(double shortRange) {
        this.shortRange = shortRange;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void cooldown(int time) {
        new BattleRunnable() {
            public void run() {
                beingThrown = false;
            }
        }.runTaskLater(time);
    }

    public void deployEquipment() {
        deployEquipment(velocity);
    }

    public void deployEquipment(double velocity) {
        Item item = gamePlayer.getPlayer().getWorld().dropItem(gamePlayer.getPlayer().getEyeLocation(), new ItemStackBuilder(itemStack.clone()).setAmount(1).build());
        item.setPickupDelay(1000);
        item.setVelocity(gamePlayer.getPlayer().getEyeLocation().getDirection().multiply(velocity));

        amount --;
        beingThrown = true;
        droppedItems.add(item);
        ignitionType.handleIgnition(this, item);

        cooldown(cooldown);
        update();

        BattleSound.EXPLOSIVE_THROW.play(game, item.getLocation());
    }

    public Item getFirstDroppedItem() {
        return droppedItems.get(0);
    }

    private String[] getLore() {
        return new String[] {
                ChatColor.WHITE + type.getName()
        };
    }

    public boolean isRelated(ItemStack itemStack) {
        for (Item item : droppedItems) {
            if (item.getItemStack() == itemStack) {
                return true;
            }
        }
        return false;
    }

    public void onDrop() { }

    public void onLeftClick() {
        if (amount <= 0 || beingThrown || game == null || gamePlayer == null) {
            return;
        }
        deployEquipment(velocity);
    }

    public void onRightClick() {
        if (amount <= 0 || beingThrown || game == null || gamePlayer == null) {
            return;
        }
        deployEquipment(0.0);
    }

    public void onSwitch() { }

    public void remove() {
        super.remove();
        for (Item item : droppedItems) {
            item.remove();
        }
        droppedItems.clear();
    }

    public void resetState() {
        amount = maxAmount;
    }

    public boolean update() {
        Placeholder placeholder = new Placeholder("bg_weapon", name);
        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(amount)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', Placeholder.replace(plugin.getBattlegroundsConfig().getWeaponDisplayName("equipment"), placeholder)))
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