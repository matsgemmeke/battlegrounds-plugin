package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.attributes.*;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.HalfBlocks;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class BattleGun extends BattleFireArm implements Gun {

    private boolean scoped, toggled;
    private Bullet bullet;
    private int hits;
    private ItemAttribute<Boolean> scopeNightVision, scope, suppressed;
    private ItemAttribute<Double> spread;
    private ItemAttribute<FireMode> fireMode;
    private ItemAttribute<Integer> burstRounds, fireRate, scopeZoom;
    private List<Attachment> attachments;
    private Map<Attachment, AttributeModifier> appliedModifiers, toggleModifiers;
    private Map<ItemAttribute, AttributeValue> toggleAttributes;
    private Map<String, String[]> compatibleAttachments;
    private Sound[] suppressedSound;

    public BattleGun(String id, String name, String description, ItemStack itemStack, short durability,
                     int magazine, int ammo, int maxAmmo, int fireRate, int burstRounds, int cooldown, int reloadDuration, double accuracy,
                     Bullet bullet, FireMode fireMode, FireArmType fireArmType, ReloadType reloadType,
                     Sound[] reloadSound, Sound[] shootSound, Sound[] suppressedSound, Map<String, String[]> compatibleAttachments) {
        super(id, name, description, itemStack, durability, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, fireArmType, reloadSound, shootSound);
        this.appliedModifiers = new HashMap<>();
        this.attachments = new ArrayList<>();
        this.bullet = bullet;
        this.compatibleAttachments = compatibleAttachments;
        this.scoped = false;
        this.suppressedSound = suppressedSound;
        this.toggleAttributes = new HashMap<>();
        this.toggled = false;
        this.toggleModifiers = new HashMap<>();

        this.burstRounds = new BattleItemAttribute<>("shot-burstrounds", new IntegerAttributeValue(burstRounds));
        this.fireMode = new BattleItemAttribute<>("shot-firemode", new FireModeAttributeValue(fireMode));
        this.fireRate = new BattleItemAttribute<>("shot-firerate", new IntegerAttributeValue(fireRate));
        this.scope = new BattleItemAttribute<>("scope-use", new BooleanAttributeValue(fireArmType.hasScope()));
        this.scopeNightVision = new BattleItemAttribute<>("scope-nightvision", new BooleanAttributeValue(false));
        this.scopeZoom = new BattleItemAttribute<>("scope-zoom", new IntegerAttributeValue(10));
        this.spread = new BattleItemAttribute<>("shot-spread", new DoubleAttributeValue(4.0));
        this.suppressed = new BattleItemAttribute<>("shot-suppressed", new BooleanAttributeValue(false));

        attributes.add(this.burstRounds);
        attributes.add(this.fireMode);
        attributes.add(this.fireRate);
        attributes.add(this.scope);
        attributes.add(this.scopeNightVision);
        attributes.add(this.scopeZoom);
        attributes.add(this.spread);
        attributes.add(this.suppressed);
    }

    public Gun clone() {
        BattleGun gun = (BattleGun) super.clone();
        gun.attachments = new ArrayList<>(); // Gun clones have their attachments stripped
        gun.burstRounds = gun.getAttribute("shot-burstrounds");
        gun.fireMode = gun.getAttribute("shot-firemode");
        gun.fireRate = gun.getAttribute("shot-firerate");
        gun.scope = gun.getAttribute("scope-use");
        gun.scopeNightVision = gun.getAttribute("scope-nightvision");
        gun.scopeZoom = gun.getAttribute("scope-zoom");
        gun.spread = gun.getAttribute("shot-spread");
        gun.suppressed = gun.getAttribute("shot-suppressed");
        return gun;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public int getBurstRounds() {
        return burstRounds.getAttributeValue().getValue();
    }

    public Set<String> getCompatibleAttachments() {
        return compatibleAttachments.keySet();
    }

    public int getFireRate() {
        return fireRate.getAttributeValue().getValue();
    }

    public DamageSource getProjectile() {
        return bullet;
    }

    public void addAttachments() {
        if (appliedModifiers.size() > 0) {
            return;
        }
        toggleModifiers.clear();
        for (Attachment attachment : attachments) {
            for (ItemAttribute attribute : attributes) {
                AttributeModifier modifier = attachment.getModifier(attribute.getId());
                String[] args = compatibleAttachments.get(attachment.getId());
                if (modifier != null) {
                    if (!attachment.isToggleable()) {
                        appliedModifiers.put(attachment, modifier);
                        attribute.applyModifier(modifier, args);
                    } else {
                        toggleModifiers.put(attachment, modifier);
                    }
                }
            }
        }
    }

    protected String[] getLore() {
        return new String[] {
                ChatColor.WHITE + fireArmType.getName(),
                ChatColor.GRAY + format(6, getAccuracy() * 100.0, 100.0) + " " + EnumMessage.STAT_ACCURACY.getMessage(),
                ChatColor.GRAY + format(6, bullet.getShortDamage(), 55.0) + " " + EnumMessage.STAT_DAMAGE.getMessage(),
                ChatColor.GRAY + format(6, Math.max((fireRate.getAttributeValue().getValue() + 10 - cooldown.getAttributeValue().getValue() / 2) * 10.0, 40.0), 200.0) + " " + EnumMessage.STAT_FIRERATE.getMessage(),
                ChatColor.GRAY + format(6, bullet.getMidRange(), 70.0) + " " + EnumMessage.STAT_RANGE.getMessage() };
    }

    private Sound[] getShotSound() {
        return suppressed.getAttributeValue().getValue() ? suppressedSound : shotSound;
    }

    private List<Location> getSpreadDirections(Location direction, int amount) {
        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }
        Float spread = this.spread.getAttributeValue().getValue().floatValue();
        List<Location> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= amount; i ++) {
            Location location = direction.clone();
            location.setPitch(location.getPitch() + random.nextFloat() * spread - spread / 2);
            location.setYaw(location.getYaw() + random.nextFloat() * spread - spread / 2);
            list.add(location);
        }

        return list;
    }

    private boolean hasToggleableAttachments() {
        for (Attachment attachment : attachments) {
            if (attachment.isToggleable()) {
                return true;
            }
        }
        return false;
    }

    private void inflictDamage(Location location, double range) {
        GamePlayer[] players = game.getPlayerManager().getNearbyPlayers(location, range);
        Team team = gamePlayer.getTeam();
        if (players.length > 0) {
            GamePlayer gamePlayer = players[0];
            if (gamePlayer == null || gamePlayer == this.gamePlayer || gamePlayer.getPlayer().isDead() || team != null && gamePlayer.getTeam() == team) {
                return;
            }
            Hitbox hitbox = Hitbox.getHitbox(gamePlayer.getLocation().getY(), location.getY());
            double damage = bullet.getDamage(hitbox, gamePlayer.getLocation().distance(this.gamePlayer.getLocation())) / plugin.getBattlegroundsConfig().firearmDamageModifer;
            game.getPlayerManager().damagePlayer(gamePlayer, damage, plugin.getBattlegroundsConfig().displayBloodEffect);
            hits ++;
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getServer().getPluginManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this, hitbox));
            }
        }
    }

    public boolean onDrop() {
        if (reloading || shooting || !hasToggleableAttachments()) {
            return true;
        }
        BattleSound.ATTACHMENT_TOGGLE.play(game, gamePlayer.getLocation());
        if (!toggled) {
            for (Attachment attachment : toggleModifiers.keySet()) {
                for (ItemAttribute attribute : attributes) {
                    AttributeModifier modifier = attachment.getModifier(attribute.getId());
                    if (modifier != null) {
                        toggleAttributes.put(attribute, attribute.getAttributeValue().copy());
                        attribute.applyModifier(modifier, compatibleAttachments.get(attachment.getId()));
                    }
                }
            }
        } else {
            for (ItemAttribute attribute : toggleAttributes.keySet()) {
                getAttribute(attribute.getId()).getAttributeValue().setValue(toggleAttributes.get(attribute).getValue());
            }
            toggleAttributes.clear();
        }
        toggled = !toggled;
        return true;
    }

    public void onLeftClick() {
        if (scope.getAttributeValue().getValue() && scoped) {
            setScoped(false);
            return;
        }
        if (reloading || shooting || ammo.getAttributeValue().getValue() <= 0 || magazine.getAttributeValue().getValue() >= magazineSize.getAttributeValue().getValue()) {
            return;
        }
        reload(reloadDuration.getAttributeValue().getValue());
    }

    public void onRightClick() {
        if (reloading || shooting) {
            return;
        }
        if (scope.getAttributeValue().getValue() && !scoped) {
            setScoped(true);
            return;
        }
        if (magazine.getAttributeValue().getValue() <= 0) {
            if (ammo.getAttributeValue().getValue() > 0) {
                reload(reloadDuration.getAttributeValue().getValue()); // Reload if the magazine is empty
            }
            return;
        }
        shoot();
    }

    public void playShotSound(Entity entity) {
        for (Sound sound : getShotSound()) {
            if (!sound.isCancelled()) {
                sound.play(game, entity);
            }
            sound.setCancelled(false);
        }
    }

    public void resetState() {
        addAttachments();
        setScoped(false);
        super.resetState();
    }

    public void setScoped(boolean scoped) {
        if (!scope.getAttributeValue().getValue() || scoped == this.scoped) {
            return;
        }

        this.scoped = scoped;

        Player player = gamePlayer.getPlayer();

        if (scoped) {
            for (Sound sound : BattleSound.GUN_SCOPE) {
                sound.play(game, player.getLocation());
            }
            if (scopeNightVision.getAttributeValue().getValue()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000, 1));
            }
            cooldown(1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, -scopeZoom.getAttributeValue().getValue())); // Zoom effect
            player.getInventory().setHelmet(new ItemStack(Material.PUMPKIN, 1));
        } else {
            BattleSound.GUN_SCOPE[0].play(game, player.getLocation(), (float) 0.75);
            BattleSound.GUN_SCOPE[1].play(game, player.getLocation(), (float) 1.5);

            player.getInventory().setHelmet(null);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.removePotionEffect(PotionEffectType.SPEED); // Restore zoom effect
        }
    }

    public void shoot() {
        shooting = true;
        fireMode.getAttributeValue().getValue().shoot(this, getFireRate(), getBurstRounds());
    }

    public void shootProjectile() {
        shootProjectile(getShootingDirection(gamePlayer.getPlayer().getEyeLocation().subtract(0, 0.25, 0)), fireArmType.getProjectileAmount());
    }

    private void shootProjectile(Location direction) {
        double distance = 0.5, range = 0.1; // Multiplier and range constant

        do {
            Vector vector = direction.getDirection();
            direction.add(vector.multiply(distance));

            displayParticle(direction, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
            inflictDamage(direction, range);

            Block block = direction.getBlock();

            if (!blocks.contains(block.getType()) && !HalfBlocks.isAir(direction)) {
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                return;
            }

            direction.subtract(vector); // Restore the location
            distance += 0.5;
        } while (distance < bullet.getLongRange() && hits < fireArmType.getMaxHits()); // If the projectile distance exceeds the long range, stop the loop

        hits = 0;
    }

    private void shootProjectile(Location direction, int amount) {
        if (amount <= 0) {
            return;
        }
        for (Location spreadDirection : getSpreadDirections(direction, amount - 1)) {
            shootProjectile(spreadDirection);
        }
        shootProjectile(direction);
    }
}