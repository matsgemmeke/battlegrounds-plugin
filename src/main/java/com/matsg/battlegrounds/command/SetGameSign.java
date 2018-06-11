package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameSign;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleGameSign;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGameSign extends SubCommand {

    public SetGameSign(Battlegrounds plugin) {
        super(plugin, "setgamesign", EnumMessage.DESCRIPTION_SETGAMESIGN.getMessage(),
                "bg setgamesign [id]", "battlegrounds.setgamesign", true, "sgs");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            EnumMessage.SPECIFY_ID.send(player);
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            EnumMessage.INVALID_ARGUMENT_TYPE.send(sender, new Placeholder("bg_arg", args[1]));
            return;
        }

        if (!plugin.getGameManager().exists(id)) {
            EnumMessage.GAME_NOT_EXISTS.send(sender, new Placeholder("bg_game", id));
            return;
        }

        BlockState state = player.getTargetBlock(null, 5).getState();
        Game game = plugin.getGameManager().getGame(id);

        if (!(state instanceof Sign)) {
            EnumMessage.INVALID_BLOCK.send(player);
            return;
        }

        GameSign sign = new BattleGameSign(plugin, game, (Sign) state);

        game.getDataFile().setLocation("sign", state.getLocation(), false);
        game.getDataFile().save();
        game.setGameSign(sign);

        sign.update();

        player.sendMessage(EnumMessage.GAMESIGN_SET.getMessage(new Placeholder("bg_game", id)));
    }
}