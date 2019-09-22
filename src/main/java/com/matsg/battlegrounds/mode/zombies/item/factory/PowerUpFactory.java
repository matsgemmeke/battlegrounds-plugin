package com.matsg.battlegrounds.mode.zombies.item.factory;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
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
    private TaskRunner taskRunner;
    private Translator translator;
    private Zombies zombies;

    public PowerUpFactory(Battlegrounds plugin, Game game, Zombies zombies, TaskRunner taskRunner, Translator translator) {
        this.plugin = plugin;
        this.game = game;
        this.zombies = zombies;
        this.taskRunner = taskRunner;
        this.translator = translator;
    }

    public PowerUp make(PowerUpEffectType effectType, int duration) {
        PowerUpEffect effect;

        switch (effectType) {
            case CARPENTER:
                effect = new Carpenter(game, zombies, translator.translate(TranslationKey.POWERUP_CARPENTER), taskRunner);
                break;
            case DOUBLE_POINTS:
                effect = new DoublePoints(plugin, translator.translate(TranslationKey.POWERUP_DOUBLE_POINTS), duration);
                break;
            case INSTA_KILL:
                effect = new InstaKill(plugin, translator.translate(TranslationKey.POWERUP_INSTA_KILL), duration);
                break;
            case FIRE_SALE:
                effect = new FireSale(game, zombies, translator.translate(TranslationKey.POWERUP_FIRE_SALE), duration, taskRunner);
                break;
            case MAX_AMMO:
                effect = new MaxAmmo(game, translator.translate(TranslationKey.POWERUP_MAX_AMMO));
                break;
            case NUKE:
                effect = new Nuke(game, zombies, translator.translate(TranslationKey.POWERUP_NUKE), taskRunner);
                break;
            default:
                throw new FactoryCreationException("Invalid power up effect type \"" + effectType + "\"");
        }

        String id = effect.toString();
        String name = effect.getName();

        ItemMetadata metadata = new ItemMetadata(id, name, null);
        ItemStack itemStack = new ItemStack(effect.getMaterial());
        PowerUpManager powerUpManager = zombies.getPowerUpManager();

        return new ZombiesPowerUp(metadata, itemStack, effect, powerUpManager);
    }
}
