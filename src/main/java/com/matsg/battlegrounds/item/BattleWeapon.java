package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.Particle;
import com.matsg.battlegrounds.util.Particle.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public abstract class BattleWeapon extends BattleItem implements Weapon {

    protected short durability;
    protected String description;

    public BattleWeapon(String name, String description, ItemStack itemStack, short durability) {
        super(name, itemStack);
        this.description = description;
        this.durability = durability;
        this.itemStack.setDurability(durability);
    }

    public Weapon clone() {
        return (Weapon) super.clone();
    }

    public String getDescription() {
        return description;
    }

    protected void displayCircleEffect(Location location, int size, ParticleEffect effect, final int amount, int random) {
        final Location center = location.clone();

        for (double i = 0; i <= Math.PI; i += Math.PI / size) {
            double radius = Math.sin(i);
            double y = Math.cos(i) * 2;

            for (double a = 0; a < Math.PI * 2; a += Math.PI / size) {
                double x = Math.cos(a) * radius * 2;
                double z = Math.sin(a) * radius * 2;

                center.add(x, y, z);

                new BattleRunnable() {
                    public void run() {
                        new Particle(effect, amount, center, 1, 1, 1, 0).display();
                    }
                }.runTaskLater(new Random().nextInt(random));

                center.subtract(x, y, z);
            }
        }
    }

    protected String format(int length, double value, double max) {
        if (value >= max) {
            return "☗☗☗☗☗☗";
        }
        StringBuilder string = new StringBuilder();
        int a = (int) (Math.round(value / length) * length / (max / length)), i;
        for (i = 1; i <= a; i++) {
            string.append("☗");
        }
        for (i = 1; i <= length - a; i++) {
            string.append("☖");
        }
        return string.toString();
    }

    public void onDrop() { }

    public void onPickUp(GamePlayer gamePlayer, Item itemEntity) { }

    public void remove() {
        if (game != null) {
            game.getItemRegistry().removeItem(this);
        }
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().remove(itemStack);
        }
    }
}