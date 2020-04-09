package com.matsg.battlegrounds.nms.v1_9_R1;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.util.Particle;
import com.matsg.battlegrounds.util.ReflectionUtils;
import com.matsg.battlegrounds.util.Title;
import net.minecraft.server.v1_9_R1.*;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class V1_9_R1 implements InternalsProvider {

    public Hellhound makeHellhound(Game game, Plugin plugin) {
        return new CustomWolf(game, plugin);
    }

    public Zombie makeZombie(Game game, Plugin plugin) {
        return new CustomZombie(game, plugin);
    }

    public void playChestAnimation(Location location, boolean open) {
        BlockPosition blockPosition = new BlockPosition(location.getX(), location.getY(), location.getZ());
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        TileEntity tileEntity = world.getTileEntity(blockPosition);

        world.playBlockAction(blockPosition, tileEntity.getBlock(), 1, open ? 1 : 0);
    }

    public void registerCustomEntities() {
        int wolfId = EntityType.WOLF.getTypeId(), zombieId = EntityType.ZOMBIE.getTypeId();
        String wolfKey = "CustomWolf", zombieKey = "CustomZombie";

        ((Map) ReflectionUtils.getField(EntityTypes.class, "c", null)).put(zombieKey, CustomZombie.class);
        ((Map) ReflectionUtils.getField(EntityTypes.class, "d", null)).put(CustomZombie.class, zombieKey);
        ((Map) ReflectionUtils.getField(EntityTypes.class, "f", null)).put(CustomZombie.class, zombieId);
        ((Map) ReflectionUtils.getField(EntityTypes.class, "c", null)).put(wolfKey, CustomWolf.class);
        ((Map) ReflectionUtils.getField(EntityTypes.class, "d", null)).put(CustomWolf.class, wolfKey);
        ((Map) ReflectionUtils.getField(EntityTypes.class, "f", null)).put(CustomWolf.class, wolfId);
    }

    public void sendActionBar(Player player, String message) {
        IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");

        PacketPlayOutChat packet = new PacketPlayOutChat(icbc, (byte) 2);

        ReflectionUtils.sendPacket(player, packet);
    }

    public void sendJSONMessage(Player player, String message, String command, String hoverMessage) {
        String text = "{\"text\":\"\",\"extra\":[{\"text\":\"" + message + "\"," +
                "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessage + "\"}," +
                "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + command + "\"}}]}";

        PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(text), (byte) 0);

        ReflectionUtils.sendPacket(player, packet);
    }

    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int time, int fadeOut) {
        new Title(title, subTitle, fadeIn, time, fadeOut).send(player);
    }

    public void spawnColoredParticle(Location location, String effect, float red, float green, float blue) {
        Particle particle = new Particle(Particle.ParticleEffect.valueOf(effect), 0, location, 0, 0, 0, 1);
        particle.setOffsetX(red);
        particle.setOffsetY(green);
        particle.setOffsetZ(blue);
        particle.display();
    }

    public void spawnParticle(Location location, String effect, int amount, float offsetX, float offsetY, float offsetZ, int speed) {
        new Particle(Particle.ParticleEffect.valueOf(effect), amount, location, offsetX, offsetY, offsetZ, speed).display();
    }
}
