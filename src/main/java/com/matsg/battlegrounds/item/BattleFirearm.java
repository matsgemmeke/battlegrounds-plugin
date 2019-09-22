package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.item.modifier.IntegerAttributeModifier;
import com.matsg.battlegrounds.util.BattleAttribute;
import com.matsg.battlegrounds.util.data.FloatValueObject;
import com.matsg.battlegrounds.util.data.IntegerValueObject;
import com.matsg.battlegrounds.util.data.ReloadSystemValueObject;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BattleFirearm extends BattleWeapon implements Firearm {

    private double accuracyAmplifier;
    protected boolean reloadCancelled;
    protected boolean reloading;
    protected boolean shooting;
    protected EventDispatcher eventDispatcher;
    protected FirearmType firearmType;
    protected GenericAttribute<Float> horizontalAccuracy;
    protected GenericAttribute<Float> verticalAccuracy;
    protected GenericAttribute<Integer> ammo;
    protected GenericAttribute<Integer> cooldown;
    protected GenericAttribute<Integer> magazine;
    protected GenericAttribute<Integer> magazineSize;
    protected GenericAttribute<Integer> magazineSupply;
    protected GenericAttribute<Integer> maxAmmo;
    protected GenericAttribute<Integer> reloadDuration;
    protected GenericAttribute<Integer> reloadDurationOg;
    protected GenericAttribute<ReloadSystem> reloadSystem;
    protected List<Item> droppedItems;
    protected List<Material> pierableMaterials;
    protected Sound[] reloadSound, shotSound;

    public BattleFirearm(
            ItemMetadata metadata,
            ItemStack itemStack,
            TaskRunner taskRunner,
            Version version,
            EventDispatcher eventDispatcher,
            FirearmType firearmType,
            List<Material> pierceableMaterials,
            ReloadSystem reloadSystem,
            Sound[] reloadSound,
            Sound[] shotSound,
            int magazine,
            int ammo,
            int maxAmmo,
            int cooldown,
            int reloadDuration,
            double accuracy,
            double accuracyAmplifier
    ) {
        super(metadata, itemStack, taskRunner, version);
        this.eventDispatcher = eventDispatcher;
        this.firearmType = firearmType;
        this.pierableMaterials = pierceableMaterials;
        this.reloadSound = reloadSound;
        this.shotSound = shotSound;
        this.accuracyAmplifier = accuracyAmplifier;
        this.droppedItems = new ArrayList<>();
        this.reloadCancelled = false;
        this.reloading = false;
        this.shooting = false;

        this.ammo = new BattleAttribute<>("ammo-reserve", new IntegerValueObject(magazine * ammo));
        this.cooldown = new BattleAttribute<>("shot-cooldown", new IntegerValueObject(cooldown));
        this.horizontalAccuracy = new BattleAttribute<>("accuracy-horizontal", new FloatValueObject((float) accuracy));
        this.magazine = new BattleAttribute<>("ammo-magazine", new IntegerValueObject(magazine));
        this.magazineSize = new BattleAttribute<>("ammo-magazine-size", new IntegerValueObject(magazine));
        this.magazineSupply = new BattleAttribute<>("ammo-magazine-supply", new IntegerValueObject(ammo));
        this.maxAmmo = new BattleAttribute<>("ammo-max", new IntegerValueObject(magazine * maxAmmo));
        this.reloadDuration = new BattleAttribute<>("reload-duration", new IntegerValueObject(reloadDuration));
        this.reloadDurationOg = new BattleAttribute<>("reload-duration-og", new IntegerValueObject(reloadDuration));
        this.reloadSystem = new BattleAttribute<>("reload-system", new ReloadSystemValueObject(reloadSystem));
        this.verticalAccuracy = new BattleAttribute<>("accuracy-vertical", new FloatValueObject((float) accuracy));

        attributes.add(this.ammo);
        attributes.add(this.cooldown);
        attributes.add(this.horizontalAccuracy);
        attributes.add(this.magazine);
        attributes.add(this.magazineSize);
        attributes.add(this.magazineSupply);
        attributes.add(this.maxAmmo);
        attributes.add(this.reloadDuration);
        attributes.add(this.reloadDurationOg);
        attributes.add(this.reloadSystem);
        attributes.add(this.verticalAccuracy);
    }

    public Firearm clone() {
        BattleFirearm firearm = (BattleFirearm) super.clone();
        firearm.ammo = firearm.getAttribute("ammo-reserve");
        firearm.cooldown = firearm.getAttribute("shot-cooldown");
        firearm.droppedItems = new ArrayList<>();
        firearm.horizontalAccuracy = firearm.getAttribute("accuracy-horizontal");
        firearm.magazine = firearm.getAttribute("ammo-magazine");
        firearm.magazineSize = firearm.getAttribute("ammo-magazine-size");
        firearm.magazineSupply = firearm.getAttribute("ammo-magazine-supply");
        firearm.maxAmmo = firearm.getAttribute("ammo-max");
        firearm.reloadDuration = firearm.getAttribute("reload-duration");
        firearm.reloadDurationOg = firearm.getAttribute("reload-duration-og");
        firearm.reloadSystem = firearm.getAttribute("reload-system");
        firearm.verticalAccuracy = firearm.getAttribute("accuracy-vertical");
        return firearm;
    }

    public double getAccuracy() {
        return (horizontalAccuracy.getValue() + verticalAccuracy.getValue()) / 2;
    }

    public int getAmmo() {
        return ammo.getValue();
    }

    public void setAmmo(int ammo) {
        this.ammo.applyModifier(new IntegerAttributeModifier(ammo));
    }

    public int getCooldown() {
        return cooldown.getValue();
    }

    public List<Item> getDroppedItems() {
        return droppedItems;
    }

    public int getMagazine() {
        return magazine.getValue();
    }

    public void setMagazine(int magazine) {
        this.magazine.applyModifier(new IntegerAttributeModifier(magazine));
    }

    public int getMagazineSize() {
        return magazineSize.getValue();
    }

    public int getMaxAmmo() {
        return maxAmmo.getValue();
    }

    public void setReloadCancelled(boolean reloadCancelled) {
        this.reloadCancelled = reloadCancelled;
    }

    public int getReloadDuration() {
        return reloadDuration.getValue();
    }

    public void setReloadDuration(int reloadDuration) {
        this.reloadDuration.applyModifier(new IntegerAttributeModifier(reloadDuration));
    }

    public FirearmType getType() {
        return firearmType;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    private void cancelReload() {
        if (!reloading) {
            return;
        }
        gamePlayer.getPlayer().setFoodLevel(20);
        reloadCancelled = true;
    }

    public void cooldown(int cooldownDuration) {
        taskRunner.runTaskLater(() -> shooting = false, cooldownDuration);
    }

    protected void displayParticle(Location location, float red, float green, float blue) {
        version.spawnColoredParticle(location, "REDSTONE", red, green, blue);
    }

    private double getReloadSpeed() {
        return (double) reloadDurationOg.getValue() / (double) reloadDuration.getValue();
    }

    protected Location getShootingDirection(Location targetDirection) {
        Player player = gamePlayer.getPlayer();
        Random random = new Random();

        double accuracyAmplifier = this.accuracyAmplifier;

        if (gamePlayer.getPlayer().isSneaking()) { // Shoot more accurate when the player is crouching
            accuracyAmplifier /= 3.0;
        }
        if (gamePlayer.getPlayer().isSprinting()) { // Shoot less accurate when the player is sprinting
            accuracyAmplifier /= 0.5;
        }

        double horizontalRecoil = (1 - horizontalAccuracy.getValue()) * accuracyAmplifier;
        double verticalRecoil = (1 - verticalAccuracy.getValue()) * accuracyAmplifier;
        int horizontalOffset = (int) (Math.round(horizontalRecoil / 2) * 2) + 1;
        int verticalOffset = (int) (Math.round(verticalRecoil / 2) * 2) + 1;

        double pitch = (player.getLocation().getPitch() + 90.0 + (random.nextInt(verticalOffset) - verticalOffset / 2)) * Math.PI / 180.0;
        double yaw = (player.getLocation().getYaw() + 90.0 + (random.nextInt(horizontalOffset) - horizontalOffset / 2)) * Math.PI / 180.0;
        double x = Math.sin(pitch) * Math.cos(yaw), y = Math.sin(pitch) * Math.sin(yaw), z = Math.cos(pitch);

        Location bulletDirection = targetDirection.clone();
        bulletDirection.setDirection(new Vector(x, z, y)); // Adds the recoil effect to the gun
        return bulletDirection;
    }

    public void handleTransaction(Transaction transaction) {
        Game game = transaction.getGame();
        GamePlayer gamePlayer = transaction.getGamePlayer();
        ItemSlot itemSlot = ItemSlot.fromSlot(transaction.getSlot());

        game.getItemRegistry().addItem(this);

        if (itemSlot == ItemSlot.FIREARM_PRIMARY) {
            Firearm primary = gamePlayer.getLoadout().getPrimary();

            if (primary != null) {
                game.getItemRegistry().removeItem(primary);
            }

            gamePlayer.getLoadout().setPrimary(this);
        } else if (itemSlot == ItemSlot.FIREARM_SECONDARY) {
            Firearm secondary = gamePlayer.getLoadout().getSecondary();

            if (secondary != null) {
                game.getItemRegistry().removeItem(secondary);
            }

            gamePlayer.getLoadout().setSecondary(this);
        }

        this.ammo = maxAmmo.clone();
        this.context = game.getGameMode();
        this.game = game;
        this.gamePlayer = gamePlayer;
        this.itemSlot = itemSlot;

        update();
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
        if (this.gamePlayer != gamePlayer || reloading || shooting) {
            return true;
        }
        droppedItems.add(item);
        this.gamePlayer.getLoadout().removeWeapon(this);
        this.gamePlayer = null;
        return false;
    }

    public boolean onPickUp(GamePlayer gamePlayer, Item item) {
        ItemSlot itemSlot = ItemSlot.fromSlot(gamePlayer.getPlayer().getInventory().getHeldItemSlot());
        Weapon weapon = gamePlayer.getLoadout().getWeaponIgnoreMetadata(item.getItemStack());

        if (weapon != null && weapon instanceof Firearm) {
            BattleSound.play(BattleSound.ITEM_EQUIP, game, gamePlayer.getLocation());
            Firearm firearm = (Firearm) weapon;

            int maxAmmo = this.maxAmmo.getValue() * magazineSize.getValue();
            int pickupAmmo = firearm.getAmmo() + ammo.getValue();

            firearm.setAmmo(Math.min(maxAmmo, pickupAmmo));
            item.remove();
            return firearm.update();
        }

        if (itemSlot == null || itemSlot != ItemSlot.FIREARM_PRIMARY && itemSlot != ItemSlot.FIREARM_SECONDARY || gamePlayer.getLoadout().getWeapon(itemSlot) != null) {
            return true;
        }

        if (itemSlot == ItemSlot.FIREARM_PRIMARY) {
            gamePlayer.getLoadout().setPrimary(this);
        } else if (itemSlot == ItemSlot.FIREARM_SECONDARY) {
            gamePlayer.getLoadout().setSecondary(this);
        }

        BattleSound.play(BattleSound.ITEM_EQUIP, game, gamePlayer.getLocation());
        droppedItems.clear();
        item.remove();
        this.gamePlayer = gamePlayer;
        this.itemSlot = itemSlot;
        return update();
    }

    public void onSwitch() {
        cancelReload();
    }

    public void playReloadSound() {
        playReloadSound(gamePlayer.getPlayer());
    }

    public void playReloadSound(Entity entity) {
        for (Sound sound : reloadSound) {
            if (sound == null) {
                continue;
            }
            if (!sound.isCancelled()) {
                long delay = sound.getDelay();

                sound.setDelay((long) (delay / getReloadSpeed()));
                sound.play(game, entity);
                sound.setCancelled(false);
                sound.setDelay(delay);
            }
            sound.setCancelled(false);
        }
    }

    public void playShotSound() {
        playShotSound(gamePlayer.getPlayer());
    }

    public void playShotSound(Entity entity) {
        for (Sound sound : shotSound) {
            if (!sound.isCancelled()) {
                sound.play(game, entity);
            }
            sound.setCancelled(false);
        }
    }

    public void reload() {
        reload(reloadDuration.getValue());
    }

    protected void reload(int reloadTime) {
        reloadCancelled = false;
        reloading = true;

        playReloadSound();

        // Force the player to slow down
        gamePlayer.getPlayer().setFoodLevel(6);

        taskRunner.runTaskTimer(new BukkitRunnable() {
            public void run() {
                if (reloadCancelled || !gamePlayer.getState().isAlive()) {
                    cancel();
                    reloadCancelled = false;
                    reloading = false;
                    setSoundCancelled(true, reloadSound);
                    return;
                }

                reloadSystem.getValue().reload();

                if (ammo.getValue() <= 0 || magazine.getValue() >= magazineSize.getValue()) {
                    cancel();
                    gamePlayer.getPlayer().setFoodLevel(20);
                    reloading = false;
                    update();
                }
            }
        }, reloadTime, reloadTime);
    }

    public void remove() {
        super.remove();
        cancelReload();
        resetState();
        for (Item item : droppedItems) {
            item.remove();
        }
    }

    public void resetState() {
        ammo.applyModifier(new IntegerAttributeModifier(magazineSupply.getValue() * magazineSize.getValue()));
        magazine.applyModifier(new IntegerAttributeModifier(magazineSize.getValue()));
    }

    private void setSoundCancelled(boolean cancelled, Sound... sounds) {
        for (Sound sound : sounds) {
            sound.setCancelled(cancelled);
        }
    }

    public boolean update() {
        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(1)
                .setDisplayName(ChatColor.WHITE + metadata.getName() + "  " + magazine.getValue() + "/" + ammo.getValue())
                .setUnbreakable(true)
                .build();
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().setItem(itemSlot.getSlot(), itemStack);
        }
        return gamePlayer != null;
    }
}
