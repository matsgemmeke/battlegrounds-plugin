package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.ComponentContainer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.plugin.Plugin;

public class WaveFactory {

    private ComponentContainer<Section> sectionContainer;
    private InternalsProvider internals;
    private Plugin plugin;
    private ZombiesConfig config;

    public WaveFactory(ComponentContainer<Section> sectionContainer, InternalsProvider internals, Plugin plugin, ZombiesConfig config) {
        this.sectionContainer = sectionContainer;
        this.internals = internals;
        this.plugin = plugin;
        this.config = config;
    }

    public Wave make(Game game, BattleEntityType entityType, int round, int mobCount) {
        double attackDamage = config.getMobAttackDamage();
        double followRange = config.getMobFollowRange();
        int targetUpdateInterval = config.getTargetUpdateInterval();
        float health;

        Wave wave;

        if (round < 10) {
            // Up until round 10, the health has a linear increase
            health = (float) (65.0 + 10.0 * round);
        } else {
            // From round 10 onwards, the health increases exponentially
            health = 155.0F;

            for (int i = 0; i < round - 9; i++) {
                health *= 1.05F;
            }
        }

        switch (entityType) {
            case HELLHOUND:
                float hellhoundHealth = (float) Math.min(health, config.getMobMaxHealth() * config.getHellhoundHealth());

                wave = new HellhoundWave(round, mobCount, attackDamage, followRange, hellhoundHealth);

                for (int i = 0; i < mobCount; i++) {
                    Hellhound hellhound = internals.makeHellhound(game, plugin);
                    hellhound.setTargetUpdateInterval(targetUpdateInterval);

                    wave.getMobs().add(hellhound);
                }

                break;
            case ZOMBIE:
                float zombieHealth = (float) Math.min(health, config.getMobMaxHealth());

                wave = new ZombiesWave(round, mobCount, attackDamage, followRange, zombieHealth);

                for (int i = 0; i < mobCount; i++) {
                    Zombie zombie = internals.makeZombie(game, plugin);
                    zombie.setTargetUpdateInterval(targetUpdateInterval);

                    wave.getMobs().add(zombie);
                }

                break;
            default:
                throw new FactoryCreationException("Invalid entity type \"" + entityType + "\"");
        }

        for (Section section : sectionContainer.getAll()) {
            if (!section.isLocked()) {
                wave.getMobSpawns().addAll(section.getMobSpawnContainer().getAll());
            }
        }

        return wave;
    }
}
