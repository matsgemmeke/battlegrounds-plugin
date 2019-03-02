package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobby extends SubCommand {

    public SetLobby(Battlegrounds plugin) {
        super(plugin);
        setAliases("sl");
        setDescription(createMessage(TranslationKey.DESCRIPTION_SETLOBBY));
        setName("setlobby");
        setPermissionNode("battlegrounds.setlobby");
        setPlayerOnly(true);
        setUsage("bg setlobby [id]");
    }

    public void executeSubCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_ID));
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(createMessage(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        if (!plugin.getGameManager().exists(id)) {
            player.sendMessage(createMessage(TranslationKey.GAME_NOT_EXISTS, new Placeholder("bg_game", id)));
            return;
        }

        Game game = plugin.getGameManager().getGame(id);
        game.getDataFile().setLocation("lobby", player.getLocation(), true);
        game.getDataFile().save();

        player.sendMessage(createMessage(TranslationKey.LOBBY_SET, new Placeholder("bg_game", id)));
    }
}