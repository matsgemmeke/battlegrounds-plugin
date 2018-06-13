package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.util.EnumMessage;

import java.util.ArrayList;
import java.util.List;

public enum Result {

    VICTORY(1, EnumMessage.RESULT_VICTORY.getMessage()) {
        boolean applies(Team team, List<Team> sortedTeams) {
            int score = sortedTeams.get(0).getScore();
            List<Team> topscorers = new ArrayList<>();
            for (Team other : sortedTeams) {
                if (other.getScore() >= score) {
                    topscorers.add(other);
                }
            }
            return topscorers.size() == 1 && topscorers.contains(team);
        }
    },
    DEFEAT(2, EnumMessage.RESULT_DEFEAT.getMessage()) {
        boolean applies(Team team, List<Team> sortedTeams) {
            return team.getScore() < sortedTeams.get(0).getScore();
        }
    },
    DRAW(3, EnumMessage.RESULT_DRAW.getMessage()) {
        boolean applies(Team team, List<Team> sortedTeams) {
            int score = sortedTeams.get(0).getScore();
            List<Team> topscorers = new ArrayList<>();
            for (Team other : sortedTeams) {
                if (other.getScore() >= score) {
                    topscorers.add(other);
                }
            }
            return topscorers.size() > 1 && topscorers.contains(team);
        }
    };

    private int id;
    private String title;

    Result(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Result getResult(Team team, List<Team> teams) {
        for (Result result : values()) {
            if (result.applies(team, teams)) {
                return result;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    abstract boolean applies(Team team, List<Team> sortedTeams);
}