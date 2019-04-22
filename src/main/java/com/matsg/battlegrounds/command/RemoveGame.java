package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.GameIdValidator;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemoveGame extends Command {

    private List<CommandSender> senders;

    public RemoveGame(Battlegrounds plugin) {
        super(plugin);
        this.senders = new ArrayList<>();

        setAliases("rg");
        setDescription(createMessage(TranslationKey.DESCRIPTION_REMOVEGAME));
        setName("removegame");
        setPermissionNode("battlegrounds.removegame");
        setUsage("bg removegame [id]");

        registerValidator(new GameIdValidator(plugin));
    }

    public void execute(final CommandSender sender, String[] args) {
        Game game = plugin.getGameManager().getGame(Integer.parseInt(args[1]));

        if (!senders.contains(sender)) {
            sender.sendMessage(createMessage(TranslationKey.GAME_CONFIRM_REMOVE, new Placeholder("bg_game", game.getId())));

            senders.add(sender);

            new BattleRunnable() {
                public void run() {
                    senders.remove(sender);
                }
            }.runTaskLater(200);
            return;
        }

        game.getDataFile().removeFile();

        plugin.getGameManager().getGames().remove(game);

        sender.sendMessage(createMessage(TranslationKey.GAME_REMOVE, new Placeholder("bg_game", game.getId())));

        senders.remove(sender);
    }
}
