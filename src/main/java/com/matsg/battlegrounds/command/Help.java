package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.Version;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Help extends Command {

    private Command[] subCommands;
    private Version version;

    public Help(Translator translator, Command[] subCommands, Version version) {
        super(translator);
        this.subCommands = subCommands;
        this.version = version;
        setAliases("?");
        setName("help");
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(createMessage(TranslationKey.HELP_MENU));
        sender.sendMessage(" ");

        if (sender instanceof Player) {
            Player player = (Player) sender;

            for (Command subCommand : getCommands(player, false)) {
                version.sendJSONMessage(player, " ● /" + subCommand.getUsage(), "/" + ChatColor.GRAY + subCommand.getUsage(), subCommand.getDescription());
            }

            sender.sendMessage(" ");

            for (Command subCommand : getCommands(player, true)) {
                if (player.hasPermission(subCommand.getPermissionNode())) {
                    version.sendJSONMessage(player, " ● /" + subCommand.getUsage(), "/" + ChatColor.GRAY + subCommand.getUsage(), subCommand.getDescription());
                }
            }
        } else {
            for (Command subCommand : getCommands(sender, false)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
            }

            sender.sendMessage(" ");

            for (Command subCommand : getCommands(sender, true)) {
                sender.sendMessage(" ● /" + subCommand.getUsage() + " " + ChatColor.GRAY + subCommand.getDescription());
            }
        }
    }

    private Command[] getCommands(CommandSender sender, boolean permission) {
        List<Command> list = new ArrayList<>();

        for (Command subCommand : subCommands) {
            if ((permission
                    && subCommand.getPermissionNode() != null
                    && sender.hasPermission(subCommand.getPermissionNode()))
                    || (!permission && subCommand.getPermissionNode() == null)) {
                list.add(subCommand);
            }
        }

        return list.toArray(new Command[list.size()]);
    }
}
