package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameSign;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BattleGameSign implements GameSign {

    private Battlegrounds plugin;
    private Game game;
    private Sign sign;

    public BattleGameSign(Battlegrounds plugin, Game game, Sign sign) {
        this.plugin = plugin;
        this.game = game;
        this.sign = sign;
    }

    public Sign getSign() {
        return sign;
    }

    public void click(Player player) {
        if (game == null
                || game.getPlayerManager().getGamePlayer(player) != null
                || !plugin.getBattlegroundsConfig().joinableGamestates.contains(game.getState().toString())
                || game.getPlayerManager().getPlayers().size() > game.getConfiguration().getMaxPlayers()) {
            return;
        }
        game.getPlayerManager().addPlayer(player);
    }

    public boolean update() {
        if (sign == null) {
            return false;
        }
        String arena = game.getArena() != null ? game.getArena().getName() : "---";
        String[] layout = plugin.getBattlegroundsConfig().getGameSignLayout();
        for (int i = 0; i <= 3; i ++) {
            sign.setLine(i, Message.createSimple(layout[i],
                    new Placeholder("bg_arena", arena),
                    new Placeholder("bg_game", game.getId()),
                    new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers()),
                    new Placeholder("bg_players", game.getPlayerManager().getPlayers().size()),
                    new Placeholder("bg_state", plugin.getBattlegroundsConfig().getGameSignState(game.getState()))));
        }
        return sign.update();
    }
}