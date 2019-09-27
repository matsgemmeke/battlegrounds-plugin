package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.game.ComponentContainer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.plugin.Plugin;

public class WaveFactory {

    private ComponentContainer<Section> sectionContainer;
    private Plugin plugin;
    private Version version;
    private ZombiesConfig config;

    public WaveFactory(ComponentContainer<Section> sectionContainer, Plugin plugin, Version version, ZombiesConfig config) {
        this.sectionContainer = sectionContainer;
        this.plugin = plugin;
        this.version = version;
        this.config = config;
    }

    public Wave make(Game game, BattleEntityType entityType, int round, int mobCount) {
        double attackDamage = config.getMobAttackDamage();
        double followRange = config.getMobFollowRange();
        float health;

        Wave wave;

        if (round < 10) {
            // Up until round 10, the health has a linear increase
            health = (float) (5.0 + 10.0 * round);
        } else {
            // From round 10 onwards, the health increases exponentially
            health = Math.round(Math.pow(1.2, round) + (15.0 * round));
        }

        switch (entityType) {
            case HELLHOUND:
                float hellhoundHealth = (float) Math.min(health, config.getMobMaxHealth() * config.getHellhoundHealth());

                wave = new HellhoundWave(round, mobCount, attackDamage, followRange, hellhoundHealth);

                for (int i = 0; i < mobCount; i++) {
                    wave.getMobs().add(version.makeHellhound(game, plugin));
                }

                break;
            case ZOMBIE:
                float zombieHealth = (float) Math.min(health, config.getMobMaxHealth());

                wave = new ZombiesWave(round, mobCount, attackDamage, followRange, zombieHealth);

                for (int i = 0; i < mobCount; i++) {
                    wave.getMobs().add(version.makeZombie(game, plugin));
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
