package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameSign;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import com.matsg.battlegrounds.game.BattleGameSign;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGameSign extends Command {

    private BattlegroundsConfig config;
    private GameManager gameManager;

    public SetGameSign(Translator translator, GameManager gameManager, BattlegroundsConfig config) {
        super(translator);
        this.gameManager = gameManager;
        this.config = config;

        setAliases("sgs");
        setDescription(createMessage(TranslationKey.DESCRIPTION_SETGAMESIGN));
        setName("setgamesign");
        setPermissionNode("battlegrounds.setgamesign");
        setPlayerOnly(true);
        setUsage("bg setgamesign [id]");

        registerValidator(new GameIdValidator(gameManager, translator, true));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        BlockState state = player.getTargetBlock(null, 5).getState();
        Game game = gameManager.getGame(Integer.parseInt(args[1]));

        if (!(state instanceof Sign)) {
            player.sendMessage(createMessage(TranslationKey.INVALID_BLOCK));
            return;
        }

        Sign sign = (Sign) state;
        GameSign gameSign = new BattleGameSign(game, sign, translator, config);

        game.getDataFile().setLocation("sign", sign.getLocation(), true);
        game.getDataFile().save();
        game.setGameSign(gameSign);

        gameSign.update();

        player.sendMessage(createMessage(TranslationKey.GAMESIGN_SET, new Placeholder("bg_game", game.getId())));
    }
}
