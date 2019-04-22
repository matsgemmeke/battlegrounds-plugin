package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Help extends Command {

    private Command[] subCommands;

    public Help(Battlegrounds plugin, Command[] subCommands) {
        super(plugin);
        this.subCommands = subCommands;
        setAliases("?");
        setName("help");
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(createMessage(TranslationKey.HELP_MENU));
        sender.sendMessage(" ");

        if (sender instanceof Player) {
            Player player = (Player) sender;

            for (Command subCommand : getCommands(false)) {
                plugin.getVersion().sendJSONMessage(player, " ● /" + subCommand.getUsage(), "/" + ChatColor.GRAY + subCommand.getUsage(), subCommand.getDescription());
            }

            sender.sendMessage(" ");

            for (Command subCommand : getCommands(true)) {
                if (player.hasPermission(subCommand.getPermissionNode())) {
                    plugin.getVersion().sendJSONMessage(player, " ● /" + subCommand.getUsage(), "/" + ChatColor.GRAY + subCommand.getUsage(), subCommand.getDescription());
                }
            }
        } else {
            for (Command subCommand : getCommands(false)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
            }

            sender.sendMessage(" ");

            for (Command subCommand : getCommands(true)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
            }
        }
    }

    private Command[] getCommands(boolean permission) {
        List<Command> list = new ArrayList<>();
        for (Command subCommand : subCommands) {
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
        return list.toArray(new Command[list.size()]);
    }
}
