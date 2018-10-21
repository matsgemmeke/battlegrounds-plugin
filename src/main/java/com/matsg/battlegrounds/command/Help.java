package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Help extends SubCommand {

    private SubCommand[] subCommands;

    public Help(Battlegrounds plugin, SubCommand[] subCommands) {
        super(plugin, "help", null, "bg help", null, false, "?");
        this.subCommands = subCommands;
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(" ");
        sender.sendMessage(EnumMessage.HELP_MENU.getMessage());
        sender.sendMessage(" ");

        if (sender instanceof Player) {
            Player player = (Player) sender;

            for (SubCommand subCommand : getCommands(false)) {
                plugin.getVersion().sendJSONMessage(player, " ● /" + subCommand.getUsage(), " " + ChatColor.GRAY + subCommand.getDescription(), "/" + subCommand.getUsage());
            }

            sender.sendMessage(" ");

            for (SubCommand subCommand : getCommands(true)) {
                if (player.hasPermission(subCommand.getPermissionNode())) {
                    plugin.getVersion().sendJSONMessage(player, " ● /" + subCommand.getUsage(), " " + ChatColor.GRAY + subCommand.getDescription(), "/" + subCommand.getUsage());
                }
            }
        } else {
            for (SubCommand subCommand : getCommands(false)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
            }

            sender.sendMessage(" ");

            if (sender.hasPermission("battlegrounds.admin")) {
                for (SubCommand subCommand : getCommands(true)) {
                    sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
                }
            }
        }
        sender.sendMessage(" ");
    }

    private SubCommand[] getCommands(boolean permission) {
        List<SubCommand> list = new ArrayList<SubCommand>();
        for (SubCommand subCommand : subCommands) {
            if (permission) {
                if (subCommand.getPermissionNode() != null && subCommand.getPermissionNode().length() > 0) {
                    list.add(subCommand);
                }
            } else {
                if (subCommand.getPermissionNode() == null || subCommand.getPermissionNode().length() <= 0) {
                    list.add(subCommand);
                }
            }
        }
        return list.toArray(new SubCommand[list.size()]);
    }
}