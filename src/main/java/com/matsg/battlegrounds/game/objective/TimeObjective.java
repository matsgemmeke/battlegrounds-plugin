package com.matsg.battlegrounds.game.objective;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.EnumTitle;

public class TimeObjective implements Objective {

    private EnumTitle enumTitle;
    private Game game;
    private int timeLimit;
    private String id;

    public TimeObjective(Game game, int timeLimit) {
        this.game = game;
        this.timeLimit = timeLimit;
        this.enumTitle = EnumTitle.OBJECTIVE_TIME;
        this.id = "Time";
    }

    public String getId() {
        return id;
    }

    public Title getTitle() {
        return enumTitle.getTitle();
    }

    public boolean isAchieved() {
        return game.getTimeControl().getTime() >= timeLimit;
    }
}
