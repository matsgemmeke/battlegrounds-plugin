package com.matsg.battlegrounds.nms.v1_12_R1;

import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Obstacle;
import com.matsg.battlegrounds.util.MobSpawnException;
import com.matsg.battlegrounds.util.ReflectionUtils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Creature;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class CustomWolf extends EntityWolf implements Hellhound {

    private BattleEntityType entityType;
    private boolean hostile;
    private boolean spawned;
    private Creature entity;
    private Game game;
    private int targetUpdateInterval;
    private Plugin plugin;

    public CustomWolf(Game game, Plugin plugin) {
        super(((CraftWorld) game.getArena().getWorld()).getHandle());
        this.game = game;
        this.plugin = plugin;
        this.entityType = BattleEntityType.HELLHOUND;
        this.hostile = true;
        this.spawned = false;

        setAngry(true);

        CraftLivingEntity entity = (CraftLivingEntity) getBukkitEntity();
        entity.setCanPickupItems(false);
        entity.setRemainingAir(Integer.MAX_VALUE);
        entity.setRemoveWhenFarAway(false);

        clearPathfinderGoals();
    }

    public BattleEntityType getEntityType() {
        return entityType;
    }

    public void setHostile(boolean hostile) {
        this.hostile = hostile;
    }

    public int getTargetUpdateInterval() {
        return targetUpdateInterval;
    }

    public void setTargetUpdateInterval(int targetUpdateInterval) {
        this.targetUpdateInterval = targetUpdateInterval;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public void clearPathfinderGoals() {
        ((Set) ReflectionUtils.getField(PathfinderGoalSelector.class, "b", goalSelector)).clear();
        ((Set) ReflectionUtils.getField(PathfinderGoalSelector.class, "c", goalSelector)).clear();
        ((Set) ReflectionUtils.getField(PathfinderGoalSelector.class, "b", targetSelector)).clear();
        ((Set) ReflectionUtils.getField(PathfinderGoalSelector.class, "c", targetSelector)).clear();
    }

    public double damage(double damage) {
        double finalHealth = entity.getHealth() - damage;

        entity.damage(0.01); // Create a damage animation
        entity.setHealth(finalHealth > 0.0 ? finalHealth : 0);

        return finalHealth;
    }

    public double getAttackDamage() {
        return getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
    }

    public double getFollowRange() {
        return getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue();
    }

    public Location getLocation() {
        return entity.getLocation();
    }

    public double getMovementSpeed() {
        return getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
    }

    public boolean hasKnockback() {
        return Boolean.parseBoolean(String.valueOf(getAttributeInstance(GenericAttributes.c).getValue()));
    }

    public boolean hasLoot() {
        return true;
    }

    public boolean isHostileTowards(GamePlayer gamePlayer) {
        return hostile;
    }

    public void remove() {
        bukkitEntity.remove();
    }

    public void resetDefaultPathfinderGoals() {
        clearPathfinderGoals();

        goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0, true));
        targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
        goalSelector.a(3, new PathfinderGoalMoveTowardsRestriction(this, 1.0));
        goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0));
        goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float) 30.0));
        targetSelector.a(6, new PathfinderGoalMoveTowardsTarget(this, 10.0, (float) 1.0));
        targetSelector.a(7, new PathfinderGoalTargetPlayer(game, this, plugin, targetUpdateInterval));
    }

    public void setAttackDamage(double damage) {
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(damage);
    }

    public void setFollowRange(double range) {
        AttributeInstance attribute = getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        AttributeModifier modifier = new AttributeModifier(" ", range, 0);

        attribute.b(modifier);
        attribute.a(modifier);
    }

    public void setKnockback(boolean knockback) {
        getAttributeInstance(GenericAttributes.c).setValue(!knockback ? 1 : 0);
    }

    public void setMaxHealth(float maxHealth) {
        getAttributeInstance(GenericAttributes.maxHealth).setValue(maxHealth);
    }

    public void setMovementSpeed(double speed) {
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
    }

    public void setTarget(Location location) {
        entity.setTarget(null);
        getNavigation().a(navigation.a(location.getX(), location.getY(), location.getZ()), 1.0D);
    }

    public void spawn(Location location, Obstacle obstacle) {
        location.getWorld().strikeLightningEffect(location);

        setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        if (!world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            throw new MobSpawnException("Failed to spawn mob with uuid " + bukkitEntity.getUniqueId());
        }

        entity = (Creature) bukkitEntity;

        resetDefaultPathfinderGoals();
    }

    public void updatePath() {
        setAngry(true);
        GamePlayer nearestPlayer = game.getPlayerManager().getNearestPlayer(entity.getLocation());
        if (!hostile || nearestPlayer == null) {
            entity.setTarget(null);
            return;
        }
        entity.setTarget(nearestPlayer.getPlayer());
    }
}
