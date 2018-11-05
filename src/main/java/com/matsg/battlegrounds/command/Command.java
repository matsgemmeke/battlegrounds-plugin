package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements CommandBase, CommandExecutor {

    protected Battlegrounds plugin;
    protected boolean playerOnly;
    protected List<SubCommand> commands;
    protected String[] aliases;
    protected String name, permissionNode;

    public Command(BattlegroundsPlugin plugin, String name, String permissionNode, boolean playerOnly, String... aliases) {
        this.aliases = aliases;
        this.commands = new ArrayList<>();
        this.name = name;
        this.permissionNode = permissionNode;
        this.playerOnly = playerOnly;
        this.plugin = plugin;

        plugin.getCommand(name).setExecutor(this); //Assign the command to this executor

        for (String alias : aliases) {
            plugin.getCommand(alias).setExecutor(this); //Register all aliases as commands too
        }
    }

    public String getName() {
        return name;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    protected SubCommand getSubCommand(String name) {
        for (SubCommand subCommand : commands) {
            if (subCommand.getName().equalsIgnoreCase(name)) {
                return subCommand;
            }
            for (String alias : subCommand.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return subCommand;
                }
            }
        }
        return null;
    }

    public List<SubCommand> getSubCommands() {
        return commands;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player) && playerOnly) {
            sender.sendMessage(Message.create(TranslationKey.INVALID_SENDER));
            return true;
        }

        if (permissionNode != null && permissionNode.length() > 0 && !sender.hasPermission(permissionNode)) {
            sender.sendMessage(Message.create(TranslationKey.NO_PERMISSION));
            return true;
        }

        execute(sender, args);
        return true;
    }
}