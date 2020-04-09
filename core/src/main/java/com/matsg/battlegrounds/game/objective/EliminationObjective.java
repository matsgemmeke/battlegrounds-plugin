package com.matsg.battlegrounds.game.objective;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.util.Title;
import com.matsg.battlegrounds.util.EnumTitle;

public class EliminationObjective implements Objective {

    private EnumTitle enumTitle;
    private Game game;
    private int minPlayers;
    private String id;

    public EliminationObjective(Game game, int minPlayers) {
        this.game = game;
        this.minPlayers = minPlayers;
        this.enumTitle = EnumTitle.OBJECTIVE_ELIMINATION;
        this.id = "Elimination";
    }

    public String getId() {
        return id;
    }

    public Title getTitle() {
        return enumTitle.getTitle();
    }

    public boolean isAchieved() {
        return false;
        //return game.getPlayerManager().getLivingPlayers().length < minPlayers;
    }
}
