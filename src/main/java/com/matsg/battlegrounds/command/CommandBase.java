package com.matsg.battlegrounds.command;

import org.bukkit.command.CommandSender;

public interface CommandBase {

    void execute(CommandSender sender, String[] args);

    String getName();

    String getPermissionNode();

    boolean isPlayerOnly();
}