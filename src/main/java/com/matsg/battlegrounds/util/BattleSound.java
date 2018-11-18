package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BattleSound implements com.matsg.battlegrounds.api.util.Sound {

    public static final BattleSound
            ATTACHMENT_TOGGLE = new BattleSound(XSound.CLICK.bukkitSound(), (float) 0.5, (float) 1.25),
            COUNTDOWN_NOTE = new BattleSound(XSound.NOTE_STICKS.bukkitSound(), 1, 1),
            EXPLOSION = new BattleSound(XSound.EXPLODE.bukkitSound(), 10, (float) 0.75),
            EXPLOSIVE_THROW = new BattleSound(XSound.SHOOT_ARROW.bukkitSound(), 1, (float) 0.5),
            KNIFE_THROW = new BattleSound(XSound.SHOOT_ARROW.bukkitSound(), 1, (float) 1.25);
    public static final BattleSound[]
            GUN_SCOPE = new BattleSound[] { new BattleSound(XSound.FALL_SMALL.bukkitSound(), 1, 1), new BattleSound(XSound.HORSE_SADDLE.bukkitSound(), 1, 2) },
            ITEM_EQUIP = new BattleSound[] { new BattleSound(XSound.BAT_TAKEOFF.bukkitSound(), 1, 1), new BattleSound(XSound.HORSE_SADDLE.bukkitSound(), 1, 1) };

    private boolean cancelled;
    private float pitch, volume;
    private long delay;
    private org.bukkit.Sound sound;

    private BattleSound(org.bukkit.Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    private BattleSound(org.bukkit.Sound sound, float volume, float pitch, long delay) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.delay = delay;
    }

    public static void play(BattleSound[] sounds, Game game, Location location) {
        for (BattleSound sound : sounds) {
            sound.play(game, location);
        }
    }

    private static org.bukkit.Sound getSound(String sound) {
        try {
            return org.bukkit.Sound.valueOf(sound);
        } catch (IllegalArgumentException e) {
            return XSound.getSound(sound).bukkitSound();
        }
    }

    public static BattleSound parseSound(String arg) {
        if (arg == null || arg.length() <= 0) {
            throw new SoundFormatException("Sound argument cannot be null");
        }
        String[] split = arg.split("-");
        if (split.length <= 3) {
            throw new SoundFormatException("Invalid sound format \"" + arg + "\"");
        }
        return new BattleSound(getSound(split[0]), Float.parseFloat(split[1]),
                Float.parseFloat(split[2]), Long.parseLong(split[3]));
    }

    public static BattleSound[] parseSoundArray(String soundString) {
        if (soundString == null || soundString.isEmpty()) {
            return new BattleSound[0];
        }
        try {
            List<BattleSound> list = new ArrayList<>();
            for (String sound : soundString.split(", ")) {
                list.add(BattleSound.parseSound(sound));
            }
            return list.toArray(new BattleSound[(list.size())]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BattleSound[0];
    }

    public long getDelay() {
        return delay;
    }

    public org.bukkit.Sound getBukkitSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void play(final Game game) {
        new BattleRunnable() {
            public void run() {
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), getBukkitSound(), volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }

    public void play(final Game game, final Entity entity) {
        new BattleRunnable() {
            public void run() {
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    gamePlayer.getPlayer().playSound(entity.getLocation(), getBukkitSound(), volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }

    public void play(final Game game, final Location location) {
        new BattleRunnable() {
            public void run() {
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    gamePlayer.getPlayer().playSound(location, getBukkitSound(), volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }

    public void play(final Game game, final Location location, final float pitch) {
        new BattleRunnable() {
            public void run() {
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    gamePlayer.getPlayer().playSound(location, getBukkitSound(), volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }
}