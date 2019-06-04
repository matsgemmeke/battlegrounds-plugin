package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.api.entity.MobType;
import com.matsg.battlegrounds.api.entity.Zombie;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.nms.v1_12_R1.CustomZombie;

public class WaveFactory {

    public Wave make(Game game, MobType mobType, int round, int amountOfMobs) {
        switch (mobType) {
            case HELLHOUND:
                return new HellhoundWave(round);
            case ZOMBIE:
                Wave<Zombie> wave = new ZombiesWave(round);

                for (int i = 0; i < amountOfMobs; i ++) {
                    wave.getMobs().add(new CustomZombie(game));
                }

                return wave;
            default:
                throw new FactoryCreationException("Invalid mob type \"" + mobType + "\"");
        }
    }
}
