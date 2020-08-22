package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;

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

    public void clear() {
        for (GamePlayer gamePlayer : playerPerks.keySet()) {
            for (Perk perk : playerPerks.get(gamePlayer)) {
                perk.remove();
            }
        }

        playerPerks.clear();
    }

    public int getPerkCount(GamePlayer gamePlayer) {
        if (!playerPerks.containsKey(gamePlayer)) {
            return 0;
        }

        return playerPerks.get(gamePlayer).size();
    }

    public boolean hasPerkEffect(GamePlayer gamePlayer, PerkEffectType effectType) {
        if (!playerPerks.containsKey(gamePlayer)) {
            return false;
        }

        for (Perk perk : playerPerks.get(gamePlayer)) {
            if (perk.getEffect().getType().equals(effectType)) {
                return true;
            }
        }

        return false;
    }

    public void removePerk(GamePlayer gamePlayer, Perk perk) {
        if (!playerPerks.containsKey(gamePlayer)) {
            return;
        }

        perk.getEffect().remove();

        playerPerks.get(gamePlayer).remove(perk);
    }

    public void removePerks(GamePlayer gamePlayer) {
        for (Perk perk : playerPerks.get(gamePlayer)) {
            perk.getEffect().remove();
            perk.remove();
        }

        playerPerks.remove(gamePlayer);
    }
}
