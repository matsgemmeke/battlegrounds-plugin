package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
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
        double maxHealth = config.getMobMaxHealth();

        switch (entityType) {
            case HELLHOUND:
                return new HellhoundWave(round, mobCount);
            case ZOMBIE:
                Wave<Zombie> wave = new ZombiesWave(round, mobCount, attackDamage, followRange, maxHealth);

                for (int i = 0; i < mobCount; i ++) {
                    wave.getMobs().add(version.makeZombie(game));
                }

                return wave;
            default:
                throw new FactoryCreationException("Invalid entity type \"" + entityType + "\"");
        }
    }
}
