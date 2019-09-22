package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.ArenaNameValidator;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemoveArena extends Command {

    private GameManager gameManager;
    private List<CommandSender> senders;
    private TaskRunner taskRunner;

    public RemoveArena(Translator translator, GameManager gameManager, TaskRunner taskRunner) {
        super(translator);
        this.gameManager = gameManager;
        this.taskRunner = taskRunner;
        this.senders = new ArrayList<>();

        setAliases("ra");
        setDescription(createMessage(TranslationKey.DESCRIPTION_REMOVEARENA));
        setName("removearena");
        setPermissionNode("battlegrounds.removearena");
        setUsage("bg removearena [id] [arena]");

        registerValidator(new GameIdValidator(gameManager, translator, true));
        registerValidator(new ArenaNameValidator(gameManager, translator, true));
    }

    public void execute(CommandSender sender, String[] args) {
        Game game = gameManager.getGame(Integer.parseInt(args[1]));
        Arena arena = gameManager.getArena(game, args[2].replaceAll("_", " "));

        if (!senders.contains(sender)) {
            sender.sendMessage(createMessage(TranslationKey.ARENA_CONFIRM_REMOVE, new Placeholder("bg_arena", arena.getName())));

            senders.add(sender);

            taskRunner.runTaskLater(() -> senders.remove(sender), 200);
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
