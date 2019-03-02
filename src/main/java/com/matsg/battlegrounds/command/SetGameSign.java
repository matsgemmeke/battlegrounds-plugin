package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameSign;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleGameSign;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGameSign extends SubCommand {

    public SetGameSign(Battlegrounds plugin) {
        super(plugin);
        setAliases("sgs");
        setDescription(createMessage(TranslationKey.DESCRIPTION_SETGAMESIGN));
        setName("setgamesign");
        setPermissionNode("battlegrounds.setgamesign");
        setPlayerOnly(true);
        setUsage("bg setgamesign [id]");
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

        BlockState state = player.getTargetBlock(null, 5).getState();
        Game game = plugin.getGameManager().getGame(id);

        if (!(state instanceof Sign)) {
            player.sendMessage(createMessage(TranslationKey.INVALID_BLOCK));
            return;
        }

        GameSign sign = new BattleGameSign(plugin, game, (Sign) state);

        game.getDataFile().setLocation("sign", state.getLocation(), false);
        game.getDataFile().save();
        game.setGameSign(sign);

        sign.update();

        player.sendMessage(createMessage(TranslationKey.GAMESIGN_SET, new Placeholder("bg_game", id)));
    }
}