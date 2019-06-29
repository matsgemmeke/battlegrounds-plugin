package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class Rename extends Command {

    public Rename(Battlegrounds plugin) {
        super(plugin);
        setDescription(createMessage(TranslationKey.DESCRIPTION_RENAME));
        setName("rename");
        setPlayerOnly(true);
        setUsage("loadout rename [loadoutid] [name]");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_LOADOUT_ID));
            return;
        }

        int loadoutNr;

        try {
            loadoutNr = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(createMessage(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        Map<String, String> loadoutSetup = plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getLoadoutSetup(loadoutNr);

        if (loadoutSetup == null) {
            player.sendMessage(createMessage(TranslationKey.INVALID_LOADOUT));
            return;
        }

        if (args.length == 2) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_LOADOUT_NAME));
            return;
        }

        String old = loadoutSetup.get("loadout_name");
        loadoutSetup.replace("loadout_name", args[2]);
        plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).saveLoadout(loadoutNr, loadoutSetup);

        player.sendMessage(createMessage(TranslationKey.RENAME_LOADOUT,
                new Placeholder("bg_loadout_old", old),
                new Placeholder("bg_loadout_new", loadoutSetup.get("loadout_name"))
        ));
    }
}
