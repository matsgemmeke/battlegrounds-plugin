package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.JSONMessage;
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
                new JSONMessage(" ● /" + subCommand.getUsage(), "§7" + subCommand.getDescription(), "/" + subCommand.getUsage()).send(player);
            }

            sender.sendMessage(" ");

            for (SubCommand subCommand : getCommands(true)) {
                if (player.hasPermission(subCommand.getPermissionNode())) {
                    new JSONMessage(" ● /" + subCommand.getUsage(), "§7" + subCommand.getDescription(), "/" + subCommand.getUsage()).send(player);
                }
            }
        } else {
            for (SubCommand subCommand : getCommands(false)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " §7" + subCommand.getDescription());
            }

            sender.sendMessage(" ");

            if (sender.hasPermission("zombies.admin")) {
                for (SubCommand subCommand : getCommands(true)) {
                    sender.sendMessage(" ● /" + subCommand.getUsage() + " §7" /*+ subCommand.getDescription()*/);
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