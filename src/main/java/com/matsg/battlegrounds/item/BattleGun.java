package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.AttributeModifier;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.HalfBlocks;
import com.matsg.battlegrounds.util.BattleAttribute;
import com.matsg.battlegrounds.util.valueobject.BooleanValueObject;
import com.matsg.battlegrounds.util.valueobject.FireModeValueObject;
import com.matsg.battlegrounds.util.valueobject.FloatValueObject;
import com.matsg.battlegrounds.util.valueobject.IntegerValueObject;
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

public class BattleGun extends BattleFirearm implements Gun {

    private boolean scoped, toggled;
    private Bullet bullet;
    private GenericAttribute<Boolean> scopeNightVision, scope, suppressed;
    private GenericAttribute<FireMode> fireMode;
    private GenericAttribute<Float> spread;
    private GenericAttribute<Integer> burstRounds, fireRate, scopeZoom;
    private int hits;
    private List<Attachment> attachments;
    private Map<Attachment, AttributeModifier> appliedModifiers, toggleModifiers;
    private Map<String, GenericAttribute> toggleAttributes;
    private Map<String, String[]> compatibleAttachments;
    private Sound[] suppressedSound;

    public BattleGun(String id, String name, String description, ItemStack itemStack,
                     int magazine, int ammo, int maxAmmo, int fireRate, int burstRounds, int cooldown, int reloadDuration, double accuracy,
                     Bullet bullet, FirearmType firearmType, FireMode fireMode, ReloadType reloadType,
                     Sound[] reloadSound, Sound[] shootSound, Sound[] suppressedSound, Map<String, String[]> compatibleAttachments) {
        super(id, name, description, itemStack, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, firearmType, reloadSound, shootSound);
        this.appliedModifiers = new HashMap<>();
        this.attachments = new ArrayList<>();
        this.bullet = bullet;
        this.compatibleAttachments = compatibleAttachments;
        this.scoped = false;
        this.suppressedSound = suppressedSound;
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
                        attribute.applyModifier(modifier, compatibleAttachments.get(attachment.getId()));
                    } else {
                        toggleModifiers.put(attachment, modifier);
                    }
                }
            }
        }
    }

    protected String[] getLore() {
        return new String[] {
                ChatColor.WHITE + firearmType.getName(),
                ChatColor.GRAY + format(6, getAccuracy() * 100.0, 100.0) + " " + messageHelper.create(TranslationKey.STAT_ACCURACY),
                ChatColor.GRAY + format(6, bullet.getShortDamage(), 55.0) + " " + messageHelper.create(TranslationKey.STAT_DAMAGE),
                ChatColor.GRAY + format(6, Math.max((fireRate.getValue() + 10 - cooldown.getValue() / 2) * 10.0, 40.0), 200.0) + " " + messageHelper.create(TranslationKey.STAT_FIRERATE),
                ChatColor.GRAY + format(6, bullet.getMidRange(), 70.0) + " " + messageHelper.create(TranslationKey.STAT_RANGE)
        };
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
            if (gamePlayer == null || gamePlayer == this.gamePlayer) {
                return;
            }
            if (gamePlayer.getPlayer().isDead() || team != null && gamePlayer.getTeam() == team) {
                hits ++;
                return;
            }
            Hitbox hitbox = Hitbox.getHitbox(gamePlayer.getLocation().getY(), location.getY());
            double damage = bullet.getDamage(hitbox, gamePlayer.getLocation().distance(this.gamePlayer.getLocation())) / plugin.getBattlegroundsConfig().firearmDamageModifer;
            game.getPlayerManager().damagePlayer(gamePlayer, damage, plugin.getBattlegroundsConfig().displayBloodEffect);
            hits ++;
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getServer().getPluginManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this, hitbox));
                game.getGameMode().onKill(gamePlayer, this.gamePlayer, this, hitbox);
            }
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
                        attribute.applyModifier(modifier, compatibleAttachments.get(attachment.getId()));
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

            if (!blocks.contains(block.getType()) && !HalfBlocks.isAir(direction)) {
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
        reloadType = getAttribute("reload-type");
        scope = getAttribute("scope-use");
        scopeNightVision = getAttribute("scope-nightvision");
        scopeZoom = getAttribute("scope-zoom");
        spread = getAttribute("shot-spread");
        suppressed = getAttribute("shot-suppressed");
        verticalAccuracy = getAttribute("accuracy-vertical");
    }
}
