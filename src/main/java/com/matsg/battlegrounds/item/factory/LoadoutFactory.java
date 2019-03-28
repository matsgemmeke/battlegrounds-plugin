package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.item.BattleLoadout;

import java.util.Arrays;

public class LoadoutFactory {

    /**
     * Make a new loadout based on the given input.
     *
     * @param loadoutNr The loadout number identifier.
     * @param name The name of the loadout.
     * @param primary The primary firearm.
     * @param secondary The secondary firearm.
     * @param equipment The equipment.
     * @param meleeWeapon The melee weapon.
     * @param primaryAttachments The primary firearm attachments.
     * @param secondaryAttachments The secondary firearm attachments.
     * @return A new loadout instance containing the given weapons.
     */
    public Loadout make(
            int loadoutNr,
            String name,
            Firearm primary,
            Firearm secondary,
            Equipment equipment,
            MeleeWeapon meleeWeapon,
            Attachment[] primaryAttachments,
            Attachment[] secondaryAttachments
    ) {
        if (primaryAttachments.length > 0 && primary instanceof Gun) {
            ((Gun) primary).getAttachments().addAll(Arrays.asList(primaryAttachments));
        }

        if (secondaryAttachments.length > 0 && secondary instanceof Gun) {
            ((Gun) secondary).getAttachments().addAll(Arrays.asList(secondaryAttachments));
        }

        return new BattleLoadout(loadoutNr, name, primary, secondary, equipment, meleeWeapon);
    }
}
