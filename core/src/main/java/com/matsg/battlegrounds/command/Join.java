package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join extends Command {

    private BattlegroundsConfig config;
    private GameManager gameManager;

    public Join(Translator translator, GameManager gameManager, BattlegroundsConfig config) {
        super(translator);
        this.gameManager = gameManager;
        this.config = config;

        setAliases("j");
        setDescription(createMessage(TranslationKey.DESCRIPTION_JOIN));
        setName("join");
        setPlayerOnly(true);
        setUsage("bg join [id]");

        registerValidator(new GameIdValidator(gameManager, translator, true));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (gameManager.getGame(player) != null) {
            player.sendMessage(createMessage(TranslationKey.ALREADY_PLAYING));
            return;
        }

        Game game = gameManager.getGame(Integer.parseInt(args[1]));

        if (!config.joinableGameStates.contains(game.getState().toString())) {
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
