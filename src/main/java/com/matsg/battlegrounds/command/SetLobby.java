package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobby extends Command {

    public SetLobby(Battlegrounds plugin) {
        super(plugin);
        setAliases("sl");
        setDescription(createMessage(TranslationKey.DESCRIPTION_SETLOBBY));
        setName("setlobby");
        setPermissionNode("battlegrounds.setlobby");
        setPlayerOnly(true);
        setUsage("bg setlobby [id]");

        registerValidator(new GameIdValidator(plugin));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Game game = plugin.getGameManager().getGame(Integer.parseInt(args[1]));

        game.getDataFile().setLocation("lobby", player.getLocation(), false);
        game.getDataFile().save();

        player.sendMessage(createMessage(TranslationKey.LOBBY_SET, new Placeholder("bg_game", game.getId())));
    }
}
