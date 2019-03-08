package com.matsg.battlegrounds.nms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class Particle {

    private final ParticleEffect particle;
    private int amount;
    private float speed;
    private Location location;
    private float offsetX;
    private float offsetY;
    private float offsetZ;

    public Particle(ParticleEffect particle) {
        this.particle = particle;
    }

    public Particle(ParticleEffect particle, int amount, Location location, float offsetX, float offsetY, float offsetZ, float speed) {
        this.particle = particle;
        this.amount = amount;
        this.location = location;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
    }

    public enum ParticleEffect {

        CLOUD("CLOUD"), CRIT("CRIT"), EXPLOSION("EXPLOSION_NORMAL"), EXPLOSION_LARGE("EXPLOSION_LARGE"), HEART("HEART"),
        FIREWORKS("FIREWORKS_SPARK"), FLAME("FLAME"), REDSTONE("REDSTONE"), SLIME("SLIME"), SMOKE("SMOKE_NORMAL"), SPELL("SPELL");

        private Object enumParticle;

        ParticleEffect(String enumParticle) {
            this.enumParticle = getEnumParticle(enumParticle);
        }
    }

    public void display() {
        try {
            Constructor particleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutWorldParticles").getConstructor(
                    ReflectionUtils.getNMSClass("EnumParticle"),
                    boolean.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    int.class,
                    int[].class);

            Object particlePacket = particleConstructor.newInstance(
                    particle.enumParticle,
                    true,
                    (float) location.getX(),
                    (float) location.getY(),
                    (float) location.getZ(),
                    offsetX,
                    offsetY,
                    offsetZ,
                    speed,
                    amount,
                    null);

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                ReflectionUtils.sendPacket(player, particlePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void display(Player player) {
        try {
            Constructor particleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutWorldParticles").getConstructor(
                    ReflectionUtils.getNMSClass("EnumParticle"),
                    boolean.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    int.class,
                    int[].class);

            Object particlePacket = particleConstructor.newInstance(
                    particle.enumParticle,
                    true,
                    (float) location.getX(),
                    (float) location.getY(),
                    (float) location.getZ(),
                    offsetX,
                    offsetY,
                    offsetZ,
                    speed,
                    amount,
                    null);

            ReflectionUtils.sendPacket(player, particlePacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAmount() {
        return amount;
    }

    private static Object getEnumParticle(String particle) {
        try {
            return ReflectionUtils.getNMSClass("EnumParticle").getField(particle.toUpperCase()).get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Location getLocation() {
        return location;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public float getSpeed() {
        return speed;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setOffsetX(float x) {
        this.offsetX = x;
    }

    public void setOffsetY(float y) {
        this.offsetY = y;
    }

    public void setOffsetZ(float z) {
        this.offsetZ = z;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
