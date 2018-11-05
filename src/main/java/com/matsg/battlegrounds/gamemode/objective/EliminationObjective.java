package com.matsg.battlegrounds.gamemode.objective;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.gamemode.Objective;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.EnumTitle;

public class EliminationObjective implements Objective {

    private String id;
    private Title title;

    public EliminationObjective() {
        this.id = "Elimination";
        this.title = EnumTitle.OBJECTIVE_ELIMINATION.getTitle();
    }

    public String getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public boolean isReached(Game game) {
        f: for (Team team : game.getGameMode().getTeams()) {
            for (GamePlayer gamePlayer : team.getPlayers()) {
                if (gamePlayer.getStatus().isAlive()) {
                    continue f;
                }
            }
            return true;
        }
        return false;
    }
}