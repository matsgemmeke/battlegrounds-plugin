package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombiesPerkManager implements PerkManager {

    private Map<GamePlayer, List<Perk>> playerPerks;

    public ZombiesPerkManager() {
        this.playerPerks = new HashMap<>();
    }

    public void addPerk(GamePlayer gamePlayer, Perk perk) {
        if (!playerPerks.containsKey(gamePlayer)) {
            List<Perk> list = new ArrayList<>();
            list.add(perk);

            playerPerks.put(gamePlayer, list);
        } else {
            playerPerks.get(gamePlayer).add(perk);
        }
    }

    public int getPerkCount(GamePlayer gamePlayer) {
        if (!playerPerks.containsKey(gamePlayer)) {
            return 0;
        }

        return playerPerks.get(gamePlayer).size();
    }

    public boolean hasPerkEffect(GamePlayer gamePlayer, PerkEffect effect) {
        if (!playerPerks.containsKey(gamePlayer)) {
            return false;
        }

        for (Perk perk : playerPerks.get(gamePlayer)) {
            if (perk.getEffect().equals(effect)) {
                return true;
            }
        }

        return false;
    }

    public void removePerk(GamePlayer gamePlayer, Perk perk) {
        if (!playerPerks.containsKey(gamePlayer)) {
            return;
        }

        playerPerks.get(gamePlayer).remove(perk);
    }
}
