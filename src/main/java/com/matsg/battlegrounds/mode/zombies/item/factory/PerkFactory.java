package com.matsg.battlegrounds.mode.zombies.item.factory;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import com.matsg.battlegrounds.mode.zombies.item.ZombiesPerk;
import com.matsg.battlegrounds.mode.zombies.item.perk.*;
import org.bukkit.inventory.ItemStack;

public class PerkFactory {

    private Translator translator;

    public PerkFactory(Translator translator) {
        this.translator = translator;
    }

    public Perk make(PerkEffectType perkEffectType) {
        PerkEffect perkEffect;

        switch (perkEffectType) {
            case DOUBLE_TAP:
                perkEffect = new DoubleTap(translator.translate(TranslationKey.PERK_DOUBLE_TAP));
                break;
            case JUGGERNOG:
                perkEffect = new Juggernog(translator.translate(TranslationKey.PERK_JUGGERNOG));
                break;
            case QUICK_REVIVE:
                perkEffect = new QuickRevive(translator.translate(TranslationKey.PERK_QUICK_REVIVE));
                break;
            case SPEED_COLA:
                perkEffect = new SpeedCola(translator.translate(TranslationKey.PERK_SPEED_COLA));
                break;
            case STAMIN_UP:
                perkEffect = new StaminUp(translator.translate(TranslationKey.PERK_STAMIN_UP));
                break;
            default:
                throw new IllegalArgumentException("Invalid perk effect type");
        }

        String id = perkEffectType.toString();
        String name = perkEffect.getName();

        ItemMetadata metadata = new ItemMetadata(id, name, null);
        ItemStack itemStack = perkEffect.getItemStack();

        return new ZombiesPerk(metadata, itemStack, perkEffect);
    }
}
