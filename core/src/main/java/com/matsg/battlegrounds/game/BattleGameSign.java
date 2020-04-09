package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameSign;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BattleGameSign implements GameSign {

    private BattlegroundsConfig config;
    private Game game;
    private Sign sign;
    private Translator translator;

    public BattleGameSign(Game game, Sign sign, Translator translator, BattlegroundsConfig config) {
        this.game = game;
        this.sign = sign;
        this.translator = translator;
        this.config = config;
    }

    public Sign getSign() {
        return sign;
    }

    public void click(Player player) {
        if (game == null
                || game.getPlayerManager().getGamePlayer(player) != null
                || game.getPlayerManager().getPlayers().size() > game.getConfiguration().getMaxPlayers()
                || !config.joinableGameStates.contains(game.getState().toString())) {
            return;
        }
        game.getPlayerManager().addPlayer(player);
    }

    public boolean update() {
        if (sign == null) {
            return false;
        }

        String arena = game.getArena() != null ? game.getArena().getName() : "---";
        String[] layout = config.getGameSignLayout();

        for (int i = 0; i <= 3; i++) {
            sign.setLine(i, translator.createSimpleMessage(layout[i],
                    new Placeholder("bg_arena", arena),
                    new Placeholder("bg_game", game.getId()),
                    new Placeholder("bg_gamemode", game.getGameMode().getShortName()),
                    new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers()),
                    new Placeholder("bg_players", game.getPlayerManager().getPlayers().size()),
                    new Placeholder("bg_state", config.getGameSignState(game.getState().toString().toLowerCase())))
            );
        }

        return sign.update();
    }
}
