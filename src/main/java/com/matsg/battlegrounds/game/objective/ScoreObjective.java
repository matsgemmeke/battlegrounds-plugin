package com.matsg.battlegrounds.game.objective;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Objective;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.EnumTitle;

public class ScoreObjective implements Objective {

    private int goal;
    private String id;
    private Title title;

    public ScoreObjective(int goal) {
        this.goal = goal;
        this.id = "Score";
        this.title = EnumTitle.OBJECTIVE_SCORE.getTitle();
    }

    public String getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public boolean isReached(Game game) {
        return game.getGameMode().getTopTeam().getScore() >= goal;
    }
}