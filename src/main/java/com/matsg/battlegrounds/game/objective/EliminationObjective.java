package com.matsg.battlegrounds.game.objective;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.EnumTitle;

public class EliminationObjective implements Objective {

    private Game game;
    private int minPlayers;
    private String id;
    private Title title;

    public EliminationObjective(Game game, int minPlayers) {
        this.game = game;
        this.minPlayers = minPlayers;
        this.id = "Elimination";
        this.title = EnumTitle.OBJECTIVE_ELIMINATION.getTitle();
    }

    public String getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public boolean isAchieved() {
        return game.getPlayerManager().getLivingPlayers().length < minPlayers;
    }
}
