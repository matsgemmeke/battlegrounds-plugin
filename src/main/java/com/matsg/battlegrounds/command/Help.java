package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Help extends SubCommand {

    private SubCommand[] subCommands;

    public Help(Battlegrounds plugin, SubCommand[] subCommands) {
        super(plugin);
        this.subCommands = subCommands;
        setAliases("?");
        setName("help");
    }

    public void executeSubCommand(CommandSender sender, String[] args) {
        sender.sendMessage(createMessage(TranslationKey.HELP_MENU));
        sender.sendMessage(" ");

        if (sender instanceof Player) {
            Player player = (Player) sender;

            for (SubCommand subCommand : getCommands(false)) {
                plugin.getVersion().sendJSONMessage(player, " ● /" + subCommand.getUsage(), "/" + ChatColor.GRAY + subCommand.getUsage(), subCommand.getDescription());
            }

            sender.sendMessage(" ");

            for (SubCommand subCommand : getCommands(true)) {
                if (player.hasPermission(subCommand.getPermissionNode())) {
                    plugin.getVersion().sendJSONMessage(player, " ● /" + subCommand.getUsage(), "/" + ChatColor.GRAY + subCommand.getUsage(), subCommand.getDescription());
                }
            }
        } else {
            for (SubCommand subCommand : getCommands(false)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
            }

            sender.sendMessage(" ");

            for (SubCommand subCommand : getCommands(true)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
            }
        }
    }

    private SubCommand[] getCommands(boolean permission) {
        List<SubCommand> list = new ArrayList<>();
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