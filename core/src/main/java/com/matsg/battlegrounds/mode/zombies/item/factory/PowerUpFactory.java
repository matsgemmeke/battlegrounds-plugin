package com.matsg.battlegrounds.mode.zombies.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.mode.zombies.PowerUpManager;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.PowerUp;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.mode.zombies.item.ZombiesPowerUp;
import com.matsg.battlegrounds.mode.zombies.item.powerup.*;
import org.bukkit.inventory.ItemStack;

public class PowerUpFactory {

    private Battlegrounds plugin;
    private Game game;
    private InternalsProvider internals;
    private TaskRunner taskRunner;
    private Translator translator;
    private Zombies zombies;

    public PowerUpFactory(
            Battlegrounds plugin,
            Game game,
            Zombies zombies,
            InternalsProvider internals,
            TaskRunner taskRunner,
            Translator translator
    ) {
        this.plugin = plugin;
        this.game = game;
        this.zombies = zombies;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
    }

    public PowerUp make(PowerUpEffectType effectType, int duration) {
        PowerUpEffect effect;
        String name;

        switch (effectType) {
            case CARPENTER:
                name = translator.translate(TranslationKey.POWERUP_CARPENTER.getPath());
                effect = new Carpenter(game, zombies, name, internals, taskRunner, translator);
                break;
            case DOUBLE_POINTS:
                name = translator.translate(TranslationKey.POWERUP_DOUBLE_POINTS.getPath());
                effect = new DoublePoints(plugin, name, duration);
                break;
            case INSTA_KILL:
                name = translator.translate(TranslationKey.POWERUP_INSTA_KILL.getPath());
                effect = new InstaKill(plugin, name, duration);
                break;
            case FIRE_SALE:
                name = translator.translate(TranslationKey.POWERUP_FIRE_SALE.getPath());
                effect = new FireSale(game, zombies, name, duration, taskRunner);
                break;
            case MAX_AMMO:
                name = translator.translate(TranslationKey.POWERUP_MAX_AMMO.getPath());
                effect = new MaxAmmo(game, name);
                break;
            case NUKE:
                name = translator.translate(TranslationKey.POWERUP_NUKE.getPath());
                effect = new Nuke(game, zombies, name, internals, taskRunner, translator);
                break;
            default:
                throw new FactoryCreationException("Invalid power up effect type \"" + effectType + "\"");
        }

        String id = effect.toString();

        ItemMetadata metadata = new ItemMetadata(id, name, null);
        ItemStack itemStack = new ItemStack(effect.getMaterial());
        PowerUpManager powerUpManager = zombies.getPowerUpManager();

        return new ZombiesPowerUp(metadata, itemStack, effect, powerUpManager);
    }
}
