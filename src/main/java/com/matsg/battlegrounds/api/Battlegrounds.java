package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.config.WeaponConfig;
import com.matsg.battlegrounds.api.dao.PlayerDAOFactory;
import com.matsg.battlegrounds.api.item.Explosive;
import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.Knife;
import org.bukkit.plugin.Plugin;

public interface Battlegrounds extends Plugin {

    CacheYaml getBattlegroundsCache();

    BattlegroundsConfig getBattlegroundsConfig();

    EventManager getEventManager();

    WeaponConfig<Explosive> getExplosiveConfig();

    WeaponConfig<FireArm> getFireArmConfig();

    GameManager getGameManager();

    WeaponConfig<Knife> getKnifeConfig();

    PlayerDAOFactory getPlayerStorage();

    Translator getTranslator();

    boolean loadConfigs();
}