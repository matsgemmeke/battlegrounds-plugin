package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.MessageHelper;
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
    private MessageHelper messageHelper;

    public Command(BattlegroundsPlugin plugin, String name, String... aliases) {
        this.plugin = plugin;
        this.commands = new ArrayList<>();
        this.messageHelper = new MessageHelper();
        this.playerOnly = false;

        plugin.getCommand(name).setExecutor(this); // Assign the command to this executor

        for (String alias : aliases) {
            plugin.getCommand(alias).setExecutor(this); // Register all aliases as commands too
        }
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getName() {
        return name;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public void setPermissionNode(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public List<SubCommand> getSubCommands() {
        return commands;
    }

    public String createMessage(TranslationKey key, Placeholder... placeholders) {
        return messageHelper.create(key, placeholders);
    }

    public String createSimpleMessage(String message, Placeholder... placeholders) {
        return messageHelper.createSimple(message, placeholders);
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

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player) && playerOnly) {
            sender.sendMessage(createMessage(TranslationKey.INVALID_SENDER));
            return true;
        }

        if (permissionNode != null && permissionNode.length() > 0 && !sender.hasPermission(permissionNode)) {
            sender.sendMessage(createMessage(TranslationKey.NO_PERMISSION));
            return true;
        }

        execute(sender, args);
        return true;
    }
}
