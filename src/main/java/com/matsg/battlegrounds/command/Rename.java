package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rename extends SubCommand {

    public Rename(Battlegrounds plugin) {
        super(plugin, "rename", Message.create(TranslationKey.DESCRIPTION_RENAME),
                "loadout rename [loadoutid] [name]", null, true, new String[0]);
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            player.sendMessage(Message.create(TranslationKey.SPECIFY_LOADOUT));
            return;
        }

        int loadoutNumber;

        try {
            loadoutNumber = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(Message.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        Loadout loadout = plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getLoadout(loadoutNumber);

        if (loadout == null) {
            player.sendMessage(Message.create(TranslationKey.INVALID_LOADOUT));
            return;
        }

        if (args.length == 2) {
            player.sendMessage(Message.create(TranslationKey.SPECIFY_NAME));
            return;
        }

        String old = loadout.getName();
        loadout.setName(args[2]);
        plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadoutNumber, loadout);

        player.sendMessage(Message.create(TranslationKey.RENAME_LOADOUT,
                new Placeholder("bg_loadout_old", old),
                new Placeholder("bg_loadout_new", loadout.getName())));
    }
}