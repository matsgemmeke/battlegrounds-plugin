package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join extends SubCommand {

    public Join(Battlegrounds plugin) {
        super(plugin);
        setAliases("j");
        setDescription(createMessage(TranslationKey.DESCRIPTION_JOIN));
        setName("join");
        setPlayerOnly(true);
        setUsage("bg join [id]");
    }

    public void executeSubCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (plugin.getGameManager().getGame(player) != null) {
            player.sendMessage(createMessage(TranslationKey.ALREADY_PLAYING));
            return;
        }

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

        if (!plugin.getBattlegroundsConfig().joinableGamestates.contains(game.getState().toString())) {
            player.sendMessage(createMessage(TranslationKey.IN_PROGRESS));
            return;
        }

        if (game.getPlayerManager().getPlayers().size() >= game.getConfiguration().getMaxPlayers()) {
            player.sendMessage(createMessage(TranslationKey.SPOTS_FULL));
            return;
        }

        game.getPlayerManager().addPlayer(player);
    }
}
