package com.matsg.battlegrounds.nms.v1_12_R1;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.nms.Particle;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.matsg.battlegrounds.nms.Title;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Version112R1 implements Version {

    public Hellhound createHellhound(Game game) {
        return new CustomWolf(game);
    }

    public Zombie createZombie(Game game) {
        return new CustomZombie(game);
    }

    public void sendActionBar(Player player, String message) {

    }

    public void sendJSONMessage(Player player, String message, String command, String hoverMessage) {
        String text = "{\"text\":\"\",\"extra\":[{\"text\":\"" + message + "\"," +
                "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessage + "\"}," +
                "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + command + "\"}}]}";

        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(text), ChatMessageType.CHAT);

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
