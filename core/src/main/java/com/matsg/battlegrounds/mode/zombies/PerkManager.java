package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;

public interface PerkManager {

    void addPerk(GamePlayer gamePlayer, Perk perk);

    void clear();

    int getPerkCount(GamePlayer gamePlayer);

    boolean hasPerkEffect(GamePlayer gamePlayer, PerkEffectType effectType);

    void removePerk(GamePlayer gamePlayer, Perk perk);

    void removePerks(GamePlayer gamePlayer);
}
