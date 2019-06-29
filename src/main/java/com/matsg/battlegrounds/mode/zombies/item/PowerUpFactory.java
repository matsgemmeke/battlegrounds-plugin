package com.matsg.battlegrounds.mode.zombies.item;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.powerup.*;
import org.bukkit.inventory.ItemStack;

public class PowerUpFactory {

    private Battlegrounds plugin;
    private Translator translator;
    private Zombies zombies;

    public PowerUpFactory(Battlegrounds plugin, Zombies zombies, Translator translator) {
        this.plugin = plugin;
        this.zombies = zombies;
        this.translator = translator;
    }

    public PowerUp make(PowerUpEffectType effectType, int duration) {
        PowerUpEffect effect;

        switch (effectType) {
            case CARPENTER:
                effect = new Carpenter(translator.translate(TranslationKey.POWERUP_CARPENTER));
                break;
            case DOUBLE_POINTS:
                effect = new DoublePoints(translator.translate(TranslationKey.POWERUP_DOUBLE_POINTS), duration);
                break;
            case INSTA_KILL:
                effect = new InstaKill(translator.translate(TranslationKey.POWERUP_INSTA_KILL), duration);
                break;
            case FIRE_SALE:
                effect = new FireSale(zombies, translator.translate(TranslationKey.POWERUP_FIRE_SALE), duration);
                break;
            case MAX_AMMO:
                effect = new MaxAmmo(translator.translate(TranslationKey.POWERUP_MAX_AMMO));
                break;
            case NUKE:
                effect = new MaxAmmo(translator.translate(TranslationKey.POWERUP_NUKE));
                break;
            default:
                throw new FactoryCreationException("Invalid power up effect type \"" + effectType + "\"");
        }

        ItemStack itemStack = new ItemStack(effect.getMaterial());

        return new ZombiesPowerUp(plugin, zombies, itemStack, effect);
    }
}
