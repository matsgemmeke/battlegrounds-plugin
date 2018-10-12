package com.matsg.battlegrounds.gamemode.objective;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.gamemode.Objective;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.Title;

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
        return false;
        //return game.getPlayerManager().getLivingPlayers().length <= 1;
    }
}