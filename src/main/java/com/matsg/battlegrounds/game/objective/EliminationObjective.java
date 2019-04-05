package com.matsg.battlegrounds.game.objective;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.api.game.Team;
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

    public boolean isAchieved(Game game) {
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
