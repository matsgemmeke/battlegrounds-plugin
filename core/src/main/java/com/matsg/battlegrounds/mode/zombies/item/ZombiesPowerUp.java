package com.matsg.battlegrounds.mode.zombies.item;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.item.BattleItem;
import com.matsg.battlegrounds.mode.zombies.PowerUpManager;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ZombiesPowerUp extends BattleItem implements PowerUp {

    private boolean active;
    private int duration;
    private List<Item> droppedItems;
    private PowerUpEffect effect;
    private PowerUpManager powerUpManager;

    public ZombiesPowerUp(ItemMetadata metadata, ItemStack itemStack, PowerUpEffect effect, PowerUpManager powerUpManager) {
        super(metadata, itemStack);
        this.effect = effect;
        this.powerUpManager = powerUpManager;
        this.active = false;
        this.droppedItems = new ArrayList<>();
        this.duration = 0;
    }

    public List<Item> getDroppedItems() {
        return droppedItems;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public PowerUpEffect getEffect() {
        return effect;
    }

    public void setEffect(PowerUpEffect effect) {
        this.effect = effect;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public PowerUp clone() {
        return (PowerUp) super.clone();
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
        return false;
    }

    public boolean onPickUp(GamePlayer gamePlayer, Item item) {
        droppedItems.remove(item);
        powerUpManager.activatePowerUp(this);
        item.remove();
        return true;
    }

    public boolean update() {
        return false;
    }
}
