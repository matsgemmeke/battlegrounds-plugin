package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.Hellhound;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.Game;

public class WaveFactory {

    private Version version;
    private ZombiesConfig config;

    public WaveFactory(Version version, ZombiesConfig config) {
        this.version = version;
        this.config = config;
    }

    public Wave<? extends Mob> make(Game game, BattleEntityType entityType, int round, int mobCount) {
        double attackDamage = config.getMobAttackDamage();
        double followRange = config.getMobFollowRange();
        float roundHealth;

        if (round < 10) {
            // Up until round 10, the health has a linear increase
            roundHealth = (float) (5.0 + 10.0 * round);
        } else {
            // From round 10 onwards, the health increases exponentially
            roundHealth = Math.round(Math.pow(1.2, round) + (15.0 * round));
        }

        switch (entityType) {
            case HELLHOUND: {
                float health = (float) Math.min(roundHealth, config.getMobMaxHealth() * config.getHellhoundHealth());

                Wave<Hellhound> wave = new HellhoundWave(round, mobCount, attackDamage, followRange, health);

                for (int i = 0; i < mobCount; i++) {
                    wave.getMobs().add(version.makeHellhound(game));
                }

                return wave;
            }
            case ZOMBIE: {
                float health = (float) Math.min(roundHealth, config.getMobMaxHealth());

                Wave<Zombie> wave = new ZombiesWave(round, mobCount, attackDamage, followRange, health);

                for (int i = 0; i < mobCount; i++) {
                    wave.getMobs().add(version.makeZombie(game));
                }

                return wave;
            }
            default:
                throw new FactoryCreationException("Invalid entity type \"" + entityType + "\"");
        }
    }
}
