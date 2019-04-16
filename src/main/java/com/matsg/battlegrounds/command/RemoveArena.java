package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.ArenaNameValidator;
import com.matsg.battlegrounds.command.validate.GameIdValidator;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemoveArena extends SubCommand {

    private List<CommandSender> senders;

    public RemoveArena(Battlegrounds plugin) {
        super(plugin);
        this.senders = new ArrayList<>();

        setAliases("ra");
        setDescription(createMessage(TranslationKey.DESCRIPTION_REMOVEARENA));
        setName("removearena");
        setPermissionNode("battlegrounds.removearena");
        setUsage("bg removearena [id] [arena]");

        registerValidator(new GameIdValidator(plugin));
        registerValidator(new ArenaNameValidator(plugin));
    }

    public void executeSubCommand(final CommandSender sender, String[] args) {
        Game game = plugin.getGameManager().getGame(Integer.parseInt(args[1]));
        Arena arena = plugin.getGameManager().getArena(game, args[2].replaceAll("_", " "));

        if (!senders.contains(sender)) {
            sender.sendMessage(createMessage(TranslationKey.ARENA_CONFIRM_REMOVE, new Placeholder("bg_arena", arena.getName())));

            senders.add(sender);

            new BattleRunnable() {
                public void run() {
                    senders.remove(sender);
                }
            }.runTaskLater(200);
            return;
        }

        game.getArenaList().remove(arena);
        game.getDataFile().set("arena." + arena.getName(), null);
        game.getDataFile().save();

        sender.sendMessage(createMessage(TranslationKey.ARENA_REMOVE,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_game", game.getId())
        ));
    }
}
