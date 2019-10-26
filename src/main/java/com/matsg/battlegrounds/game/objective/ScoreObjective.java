package com.matsg.battlegrounds.game.objective;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.EnumTitle;

public class ScoreObjective implements Objective {

    private EnumTitle enumTitle;
    private Game game;
    private int goal;
    private String id;

    public ScoreObjective(Game game, int goal) {
        this.game = game;
        this.goal = goal;
        this.enumTitle = EnumTitle.OBJECTIVE_SCORE;
        this.id = "Score";
    }

    public String getId() {
        return id;
    }

    public Title getTitle() {
        return enumTitle.getTitle();
    }

    public boolean isAchieved() {
        return game.getGameMode().getTopTeam().getScore() >= goal;
    }
}
