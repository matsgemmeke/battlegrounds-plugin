package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.command.validator.CommandValidator;
import com.matsg.battlegrounds.command.validator.ValidationResponse;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements CommandExecutor {

    protected Battlegrounds plugin;
    protected List<Command> subCommands;
    private boolean playerOnly;
    private List<CommandValidator> validators;
    private String[] aliases;
    private String description, name, permissionNode, usage;
    private Translator translator;

    public Command(Battlegrounds plugin) {
        this.plugin = plugin;
        this.aliases = new String[0];
        this.playerOnly = false;
        this.subCommands = new ArrayList<>();
        this.translator = plugin.getTranslator();
        this.validators = new ArrayList<>();
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public void setPermissionNode(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public String createMessage(TranslationKey key, Placeholder... placeholders) {
        return translator.translate(key, placeholders);
    }

    public String createSimpleMessage(String message, Placeholder... placeholders) {
        return translator.translate(message, placeholders);
    }

    public void execute(CommandSender sender, String[] args) { }

    protected Command getSubCommand(String name) {
        for (Command subCommand : subCommands) {
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

        ValidationResponse response = validateInput(args);

        if (!response.passed()) {
            sender.sendMessage(response.getMessage());
            return true;
        }

        execute(sender, args);
        return true;
    }

    public void registerValidator(CommandValidator validator) {
        validators.add(validator);
    }

    public ValidationResponse validateInput(String[] args) {
        for (CommandValidator validator : validators) {
            ValidationResponse response = validator.validate(args);
            if (!response.passed()) {
                return response;
            }
        }
        return ValidationResponse.PASSED;
    }
}
