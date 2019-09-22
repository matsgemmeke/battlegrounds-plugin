package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RemoveGame extends Command {

    private GameManager gameManager;
    private List<CommandSender> senders;
    private TaskRunner taskRunner;

    public RemoveGame(Plugin plugin, Translator translator, GameManager gameManager, TaskRunner taskRunner) {
        super(plugin, translator);
        this.gameManager = gameManager;
        this.taskRunner = taskRunner;
        this.senders = new ArrayList<>();

        setAliases("rg");
        setDescription(createMessage(TranslationKey.DESCRIPTION_REMOVEGAME));
        setName("removegame");
        setPermissionNode("battlegrounds.removegame");
        setUsage("bg removegame [id]");

        registerValidator(new GameIdValidator(gameManager, translator, true));
    }

    public void execute(final CommandSender sender, String[] args) {
        Game game = gameManager.getGame(Integer.parseInt(args[1]));

        if (!senders.contains(sender)) {
            sender.sendMessage(createMessage(TranslationKey.GAME_CONFIRM_REMOVE, new Placeholder("bg_game", game.getId())));

            senders.add(sender);

            taskRunner.runTaskLater(() -> senders.remove(sender), 200);
            return;
        }

        game.getDataFile().removeFile();

        gameManager.getGames().remove(game);

        sender.sendMessage(createMessage(TranslationKey.GAME_REMOVE, new Placeholder("bg_game", game.getId())));

        senders.remove(sender);
    }
}
