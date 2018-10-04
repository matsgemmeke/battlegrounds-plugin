package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rename extends SubCommand {

    public Rename(Battlegrounds plugin) {
        super(plugin, "rename", EnumMessage.DESCRIPTION_RENAME.getMessage(),
                "loadout rename [loadoutid] [name]", null, true, new String[0]);
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            EnumMessage.SPECIFY_LOADOUT.send(player);
            return;
        }

        int loadoutId;

        try {
            loadoutId = Integer.parseInt(args[1]);
        } catch (Exception e) {
            EnumMessage.INVALID_ARGUMENT_TYPE.send(sender, new Placeholder("bg_arg", args[1]));
            return;
        }

        Loadout loadout = plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getLoadout(loadoutId);

        if (loadout == null) {
            EnumMessage.INVALID_LOADOUT.send(player);
            return;
        }

        if (args.length == 2) {
            EnumMessage.SPECIFY_NAME.send(player);
            return;
        }

        String old = loadout.getName();
        loadout.setName(args[2]);
        plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadout);

        EnumMessage.RENAME_LOADOUT.send(player, new Placeholder("bg_loadout_old", old), new Placeholder("bg_loadout_new", loadout.getName()));
    }
}