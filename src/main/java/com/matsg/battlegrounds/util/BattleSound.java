package com.matsg.battlegrounds.util;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.ReflectionUtils.EnumVersion;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleSound implements com.matsg.battlegrounds.api.util.Sound {

    public static final BattleSound
            COUNTDOWN_NOTE = new BattleSound("BLOCK_NOTE_HAT", 1, 1),
            EXPLOSION = new BattleSound("ENTITY_GENERIC_EXPLODE", 10, (float) 0.75),
            EXPLOSIVE_THROW = new BattleSound("ENTITY_ARROW_SHOOT", 1, (float) 0.5),
            KNIFE_THROW = new BattleSound("ENTITY_ARROW_SHOOT", 1, (float) 1.25);
    public static final BattleSound[]
            GUN_SCOPE = new BattleSound[] { new BattleSound("ENTITY_PLAYER_SMALL_FALL", 1, 1), new BattleSound("ENTITY_HORSE_SADDLE", 1, 2) },
            ITEM_EQUIP = new BattleSound[] { new BattleSound("ENTITY_BAT_TAKEOFF", 1, 1), new BattleSound("ENTITY_HORSE_SADDLE", 1, 1) };

    private boolean cancelled;
    private float pitch, volume;
    private long delay;
    private Sound sound;

    private BattleSound(String sound, float volume, float pitch) {
        this.sound = getSound(sound);
        this.volume = volume;
        this.pitch = pitch;
    }

    private BattleSound(Sound sound, float volume, float pitch, long delay) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.delay = delay;
    }

    private static Sound getSound(String arg) {
        Sound sound;
        if (ReflectionUtils.ENUM_VERSION.getValue() > EnumVersion.V1_8_R3.getValue()) {
            sound = Sound.valueOf(arg);
        } else {
            sound = searchSoundDictionary(arg);
        }
        return sound;
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

    private static Sound searchSoundDictionary(String arg) {
        String sound = getSoundDictionary().get(arg);
        if (sound == null) {
            sound = arg;
        }
        return Sound.valueOf(sound);
    }

    private static Map<String, String> getSoundDictionary() {
        Map<String, String> map = new HashMap<>();
        map.put("BLOCK_ANVIL_HIT", "ANVIL_LAND");
        map.put("BLOCK_FIRE_EXTINGUISH", "FIZZ");
        map.put("BLOCK_IRON_DOOR_CLOSE", "DOOR_CLOSE");
        map.put("BLOCK_IRON_DOOR_OPEN", "DOOR_OPEN");
        map.put("BLOCK_NOTE_HAT", "NOTE_STICKS");
        map.put("BLOCK_PISTON_CONTRACT", "PISTON_RETRACT");
        map.put("BLOCK_PISTON_EXTEND", "PISTON_EXTEND");
        map.put("BLOCK_STONE_BUTTON_CLICK_OFF", "CLICK");
        map.put("BLOCK_STONE_BUTTON_CLICK_ON", "CLICK");
        map.put("BLOCK_WOODEN_DOOR_CLOSE", "DOOR_CLOSE");
        map.put("BLOCK_WOODEN_DOOR_OPEN", "DOOR_OPEN");
        map.put("ENTITY_ARROW_SHOOT", "SHOOT_ARROW");
        map.put("ENTITY_BAT_TAKEOFF", "BAT_TAKEOFF");
        map.put("ENTITY_BLAZE_HURT", "BLAZE_HIT");
        map.put("ENTITY_FIREWORK_BLAST", "FIREWORK_BLAST");
        map.put("ENTITY_GENERIC_EXPLODE", "EXPLODE");
        map.put("ENTITY_HORSE_SADDLE", "HORSE_SADDLE");
        map.put("ENTITY_IRONGOLEM_HURT", "IRONGOLEM_HIT");
        map.put("ENTITY_PLAYER_SMALL_FALL", "FALL_BIG");
        map.put("ENTITY_SKELETON_AMBIENT", "SKELETON_IDLE");
        map.put("ENTITY_SKELETON_STEP", "SKELETON_WALK");
        map.put("ENTITY_ZOMBIE_ATTACK_DOOR_WOOD", "ZOMBIE_WOODBREAK");
        map.put("ENTITY_ZOMBIE_ATTACK_IRON_DOOR", "ZOMBIE_METAL");
        return map;
    }
}