package com.matsg.battlegrounds.item.factory;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.item.BattleLoadout;

import java.util.Arrays;

public class LoadoutFactory {

    /**
     * Makes a new loadout based on the given input.
     *
     * @param loadoutNr the loadout number identifier
     * @param name the name of the loadout
     * @param primary the primary firearm
     * @param secondary the secondary firearm
     * @param equipment the equipment
     * @param meleeWeapon the melee weapon
     * @param primaryAttachments the primary firearm attachments
     * @param secondaryAttachments the secondary firearm attachments
     * @param game the game to assign the weapons to
     * @param gamePlayer the player to assign the weapons to
     * @param droppable whether items in the loadout should be droppable
     * @return a new loadout instance containing the given weapons
     */
    public Loadout make(
            int loadoutNr,
            String name,
            Firearm primary,
            Firearm secondary,
            Equipment equipment,
            MeleeWeapon meleeWeapon,
            Attachment[] primaryAttachments,
            Attachment[] secondaryAttachments,
            Game game,
            GamePlayer gamePlayer,
            boolean droppable
    ) {
        if (primary != null) {
            primary.setDroppable(droppable);
            primary.setGame(game);
            primary.setGamePlayer(gamePlayer);
            primary.setItemSlot(ItemSlot.FIREARM_PRIMARY);
        }

        if (secondary != null) {
            secondary.setDroppable(droppable);
            secondary.setGame(game);
            secondary.setGamePlayer(gamePlayer);
            secondary.setItemSlot(ItemSlot.FIREARM_SECONDARY);
        }

        if (equipment != null) {
            equipment.setDroppable(droppable);
            equipment.setGame(game);
            equipment.setGamePlayer(gamePlayer);
            equipment.setItemSlot(ItemSlot.EQUIPMENT);
        }

        if (meleeWeapon != null) {
            meleeWeapon.setDroppable(droppable);
            meleeWeapon.setGame(game);
            meleeWeapon.setGamePlayer(gamePlayer);
            meleeWeapon.setItemSlot(ItemSlot.MELEE_WEAPON);
        }

        if (primaryAttachments.length > 0 && primary instanceof Gun) {
            ((Gun) primary).getAttachments().addAll(Arrays.asList(primaryAttachments));
        }

        if (secondaryAttachments.length > 0 && secondary instanceof Gun) {
            ((Gun) secondary).getAttachments().addAll(Arrays.asList(secondaryAttachments));
        }

        Loadout loadout = new BattleLoadout(loadoutNr, name, primary, secondary, equipment, meleeWeapon);

        if (game != null) {
            for (Weapon weapon : loadout.getWeapons()) {
                if (weapon != null) {
                    game.getItemRegistry().addItem(weapon);
                    weapon.setContext(game.getGameMode());
                }
            }
        }

        return loadout;
    }
}
