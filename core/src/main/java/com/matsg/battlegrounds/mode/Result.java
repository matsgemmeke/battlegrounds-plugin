package com.matsg.battlegrounds.mode;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.game.Team;

import java.util.ArrayList;
import java.util.List;

public enum Result {

    VICTORY(1, TranslationKey.RESULT_VICTORY) {
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
    DEFEAT(2, TranslationKey.RESULT_DEFEAT) {
        boolean applies(Team team, List<Team> sortedTeams) {
            return team.getScore() < sortedTeams.get(0).getScore();
        }
    },
    DRAW(3, TranslationKey.RESULT_DRAW) {
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
    private TranslationKey key;

    Result(int id, TranslationKey key) {
        this.id = id;
        this.key = key;
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

    public TranslationKey getTranslationKey() {
        return key;
    }

    abstract boolean applies(Team team, List<Team> sortedTeams);
}
