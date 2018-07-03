package com.matsg.battlegrounds.gui.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class Line {

    private ChatColor chatColor;
    private int score;
    private Team team;

    public Line(ChatColor chatColor, int score, Team team) {
        this.chatColor = chatColor;
        this.score = score;
        this.team = team;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public int getScore() {
        return score;
    }

    public Team getTeam() {
        return team;
    }

    private String[] format(String string) {
        String[] array = new String[2];
        array[0] = string.substring(0, string.length() < 15 ? string.length() : 15); // First 16 characters
        array[1] = string.substring(string.length() > 15 ? 15 : string.length(), string.length()); // Next 16 characters
        return array;
    }

    public void setText(String text) {
        String[] format = format(text);
        team.setPrefix(format[0]);
        team.setSuffix(format[1]);
    }
}