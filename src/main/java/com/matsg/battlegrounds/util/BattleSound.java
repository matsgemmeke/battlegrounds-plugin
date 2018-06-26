package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BattleSound implements com.matsg.battlegrounds.api.util.Sound {

    public static final BattleSound
            COUNTDOWN_NOTE = new BattleSound(Sound.BLOCK_NOTE_HAT, 1, 1),
            DOOR_OPEN = new BattleSound(Sound.BLOCK_WOODEN_DOOR_OPEN, 1, (float) 0.75),
            EXPLOSION = new BattleSound(Sound.ENTITY_GENERIC_EXPLODE, 10, (float) 0.75),
            EXPLOSIVE_THROW = new BattleSound(Sound.ENTITY_ARROW_SHOOT, 1, (float) 0.5),
            KNIFE_THROW = new BattleSound(Sound.ENTITY_ARROW_SHOOT, 1, (float) 1.25),
            NEW_ROUND = new BattleSound(Sound.ENTITY_LIGHTNING_THUNDER, 20, 1),
            PLING = new BattleSound(Sound.BLOCK_NOTE_PLING, 1, 1),
            POWER_SWITCH_ACTIVATE = new BattleSound(Sound.BLOCK_LEVER_CLICK, 1, (float) 0.75);
    public static final BattleSound[]
            GUN_SCOPE = new BattleSound[] { new BattleSound(Sound.ENTITY_PLAYER_SMALL_FALL, 1, 1), new BattleSound(Sound.ENTITY_HORSE_SADDLE, 1, 2) },
            ITEM_EQUIP = new BattleSound[] { new BattleSound(Sound.ENTITY_BAT_TAKEOFF, 1, 1), new BattleSound(Sound.ENTITY_HORSE_SADDLE, 1, 1) };

    private boolean cancelled;
    private float pitch, volume;
    private long delay;
    private Sound sound;

    public BattleSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public BattleSound(Sound sound, float volume, float pitch, long delay) {
        this(sound, volume, pitch);
        this.delay = delay;
    }

    public static BattleSound parseSound(String arg) {
        if (arg == null || arg.length() <= 0) {
            throw new SoundFormatException("Sound argument cannot be null");
        }
        String[] split = arg.split("-");
        if (split.length <= 3) {
            throw new SoundFormatException("Invalid sound format \"" + arg + "\"");
        }
        return new BattleSound(Sound.valueOf(split[0]), Float.parseFloat(split[1]),
                Float.parseFloat(split[2]), Long.parseLong(split[3]));
    }

    public static BattleSound[] parseSoundArray(String soundString) {
        try {
            List<BattleSound> list = new ArrayList<>();
            for (String sound : soundString.split(", ")) {
                list.add(BattleSound.parseSound(sound));
            }
            return list.toArray(new BattleSound[(list.size())]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getDelay() {
        return delay;
    }

    public Sound getEnumSound() {
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
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), sound, volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }

    public void play(final Game game, final Entity entity) {
        new BattleRunnable() {
            public void run() {
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    gamePlayer.getPlayer().playSound(entity.getLocation(), sound, volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }

    public void play(final Game game, final Location location) {
        new BattleRunnable() {
            public void run() {
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    gamePlayer.getPlayer().playSound(location, sound, volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }

    public void play(final Game game, final Location location, final float pitch) {
        new BattleRunnable() {
            public void run() {
                for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                    gamePlayer.getPlayer().playSound(location, sound, volume, pitch);
                }
            }
        }.runTaskLater(delay);
    }
}