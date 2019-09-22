package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.FireMode;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.HalfBlocks;
import com.matsg.battlegrounds.util.BattleAttribute;
import com.matsg.battlegrounds.util.data.*;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class BattleGun extends BattleFirearm implements Gun {

    private boolean scoped;
    private boolean toggled;
    private Bullet bullet;
    private double damageAmplifier;
    private GenericAttribute<Boolean> scope;
    private GenericAttribute<Boolean> scopeNightVision;
    private GenericAttribute<Boolean> suppressed;
    private GenericAttribute<FireMode> fireMode;
    private GenericAttribute<Float> spread;
    private GenericAttribute<Integer> burstRounds;
    private GenericAttribute<Integer> fireRate;
    private GenericAttribute<Integer> scopeZoom;
    private int hits;
    private List<Attachment> attachments;
    private Map<Attachment, AttributeModifier> appliedModifiers;
    private Map<Attachment, AttributeModifier> toggleModifiers;
    private Map<String, GenericAttribute> toggleAttributes;
    private Map<String, String[]> compatibleAttachments;
    private Sound[] suppressedSound;

    public BattleGun(
            ItemMetadata metadata,
            ItemStack itemStack,
            EventDispatcher eventDispatcher,
            Version version,
            Bullet bullet,
            FirearmType firearmType,
            FireMode fireMode,
            List<Material> piercableMaterials,
            Map<String, String[]> compatibleAttachments,
            ReloadSystem reloadSystem,
            Sound[] reloadSound,
            Sound[] shootSound, Sound[] suppressedSound,
            int magazine,
            int ammo,
            int maxAmmo,
            int fireRate,
            int burstRounds,
            int cooldown,
            int reloadDuration,
            double accuracy,
            double accuracyAmplifier,
            double damageAmplifier
    ) {
        super(metadata, itemStack, eventDispatcher, version, firearmType, piercableMaterials, reloadSystem, reloadSound,
                shootSound, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, accuracyAmplifier);
        this.damageAmplifier = damageAmplifier;
        this.bullet = bullet;
        this.suppressedSound = suppressedSound;
        this.compatibleAttachments = compatibleAttachments;
        this.appliedModifiers = new HashMap<>();
        this.attachments = new ArrayList<>();
        this.scoped = false;
        this.toggleAttributes = new HashMap<>();
        this.toggled = false;
        this.toggleModifiers = new HashMap<>();

        this.burstRounds = new BattleAttribute<>("shot-burstrounds", new IntegerValueObject(burstRounds));
        this.fireMode = new BattleAttribute<>("shot-firemode", new FireModeValueObject(fireMode));
        this.fireRate = new BattleAttribute<>("shot-firerate", new IntegerValueObject(fireRate));
        this.scope = new BattleAttribute<>("scope-use", new BooleanValueObject(firearmType.hasScope()));
        this.scopeNightVision = new BattleAttribute<>("scope-nightvision", new BooleanValueObject(false));
        this.scopeZoom = new BattleAttribute<>("scope-zoom", new IntegerValueObject(10));
        this.spread = new BattleAttribute<>("shot-spread", new FloatValueObject((float) 4.0));
        this.suppressed = new BattleAttribute<>("shot-suppressed", new BooleanValueObject(false));

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
        return burstRounds.getValue();
    }

    public Set<String> getCompatibleAttachments() {
        return compatibleAttachments.keySet();
    }

    public int getFireRate() {
        return fireRate.getValue();
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
            for (GenericAttribute attribute : attributes) {
                AttributeModifier modifier = attachment.getModifier(attribute.getId());
                if (modifier != null) {
                    if (!attachment.isToggleable()) {
                        appliedModifiers.put(attachment, modifier);
                        attribute.applyModifier(modifier, compatibleAttachments.get(attachment.getMetadata().getId()));
                    } else {
                        toggleModifiers.put(attachment, modifier);
                    }
                }
            }
        }
    }

    private Sound[] getShotSound() {
        return suppressed.getValue() ? suppressedSound : shotSound;
    }

    private List<Location> getSpreadDirections(Location direction, int amount) {
        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }
        Float spread = this.spread.getValue();
        List<Location> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= amount; i++) {
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
        BattleEntity[] entities = context.getNearbyEntities(location, gamePlayer.getTeam(), range);

        if (entities.length > 0) {
            BattleEntity entity = entities[0];

            if (entity == null || entity == gamePlayer) {
                return;
            }

            if (entity.getBukkitEntity().isDead()) {
                hits++;
                return;
            }

            Location entityLocation = entity.getLocation();
            Hitbox hitbox = Hitbox.getHitbox(entityLocation.getY(), location.getY());

            double damage = bullet.getDamage(hitbox, entityLocation.distance(gamePlayer.getLocation())) / damageAmplifier;

            if (context.hasBloodEffectDisplay(entity.getEntityType())) {
                gamePlayer.getLocation().getWorld().playEffect(location, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }

            hits++;

            Event event;

            if (entity.getHealth() - damage <= 0) {
                event = new GamePlayerKillEntityEvent(game, gamePlayer, entity, this, hitbox, hitbox.getPoints());
            } else {
                event = new GamePlayerDamageEntityEvent(game, gamePlayer, entity, this, damage, hitbox);
            }

            eventDispatcher.dispatchExternalEvent(event);
        }
    }

    public void onLeftClick() {
        if (scope.getValue() && scoped) {
            setScoped(false);
            return;
        }
        if (reloading || shooting || ammo.getValue() <= 0 || magazine.getValue() >= magazineSize.getValue()) {
            return;
        }
        reload(reloadDuration.getValue());
    }

    public void onRightClick() {
        if (reloading || shooting) {
            return;
        }
        if (scope.getValue() && !scoped) {
            setScoped(true);
            return;
        }
        if (magazine.getValue() <= 0) {
            if (ammo.getValue() > 0) {
                reload(reloadDuration.getValue()); // Reload if the magazine is empty
            }
            return;
        }
        shoot();
    }

    public void onSwap() {
        if (reloading || shooting || !hasToggleableAttachments()) {
            return;
        }
        BattleSound.ATTACHMENT_TOGGLE.play(game, gamePlayer.getLocation());
        if (!toggled) {
            for (Attachment attachment : toggleModifiers.keySet()) {
                for (GenericAttribute attribute : attributes) {
                    AttributeModifier modifier = attachment.getModifier(attribute.getId());
                    if (modifier != null) {
                        toggleAttributes.put(attribute.getId(), attribute.clone());
                        attribute.applyModifier(modifier, compatibleAttachments.get(attachment.getMetadata().getId()));
                    }
                }
            }
        } else {
            for (String attributeId : toggleAttributes.keySet()) {
                setAttribute(attributeId, toggleAttributes.get(attributeId));
            }
            toggleAttributes.clear();
            updateAttributes();
        }
        toggled = !toggled;
        update();
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
        appliedModifiers.clear();
        addAttachments();
        setScoped(false);
        super.resetState();
    }

    public void setScoped(boolean scoped) {
        if (!scope.getValue() || scoped == this.scoped) {
            return;
        }

        this.scoped = scoped;

        Player player = gamePlayer.getPlayer();

        if (scoped) {
            for (Sound sound : BattleSound.GUN_SCOPE) {
                sound.play(game, player.getLocation());
            }
            if (scopeNightVision.getValue()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000, 1));
            }
            cooldown(1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, -scopeZoom.getValue())); // Zoom effect
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
        fireMode.getValue().shoot(this, getFireRate(), getBurstRounds());
    }

    public void shootProjectile() {
        shootProjectile(getShootingDirection(gamePlayer.getPlayer().getEyeLocation().subtract(0, 0.25, 0)), firearmType.getProjectileAmount());
    }

    private void shootProjectile(Location direction) {
        double distance = 0.5, range = 0.1; // Multiplier and range constant

        do {
            Vector vector = direction.getDirection();
            direction.add(vector.multiply(distance));

            displayParticle(direction, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
            inflictDamage(direction, range);

            Block block = direction.getBlock();

            if (!pierableMaterials.contains(block.getType()) && !HalfBlocks.isAir(direction)) {
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                return;
            }

            direction.subtract(vector); // Restore the location
            distance += 0.5;
        } while (distance < bullet.getLongRange() && hits < firearmType.getMaxHits()); // If the projectile distance exceeds the long range, stop the loop

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

    private void updateAttributes() {
        ammo = getAttribute("ammo-reserve");
        burstRounds = getAttribute("shot-burstrounds");
        cooldown = getAttribute("shot-cooldown");
        horizontalAccuracy = getAttribute("accuracy-horizontal");
        fireMode = getAttribute("shot-firemode");
        fireRate = getAttribute("shot-firerate");
        magazine = getAttribute("ammo-magazine");
        magazineSize = getAttribute("ammo-magazine-size");
        magazineSupply = getAttribute("ammo-magazine-supply");
        maxAmmo = getAttribute("ammo-max");
        reloadDuration = getAttribute("reload-duration");
        reloadDurationOg = getAttribute("reload-duration-og");
        reloadSystem = getAttribute("reload-system");
        scope = getAttribute("scope-use");
        scopeNightVision = getAttribute("scope-nightvision");
        scopeZoom = getAttribute("scope-zoom");
        spread = getAttribute("shot-spread");
        suppressed = getAttribute("shot-suppressed");
        verticalAccuracy = getAttribute("accuracy-vertical");
    }
}
