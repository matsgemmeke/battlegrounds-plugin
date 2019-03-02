package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.CommandValidator;
import com.matsg.battlegrounds.command.validate.ValidationResponse;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand implements CommandBase {

    protected Battlegrounds plugin;
    private boolean playerOnly;
    private List<CommandValidator> validators;
    private MessageHelper messageHelper;
    private String description, name, permissionNode, usage;
    private String[] aliases;

    public SubCommand(Battlegrounds plugin) {
        this.plugin = plugin;
        this.aliases = new String[0];
        this.messageHelper = new MessageHelper();
        this.playerOnly = false;
        this.validators = new ArrayList<>();
    }

    public abstract void executeSubCommand(CommandSender sender, String[] args);

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

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String createMessage(TranslationKey key, Placeholder... placeholders) {
        return messageHelper.create(key, placeholders);
    }

    public String createSimpleMessage(String message, Placeholder... placeholders) {
        return messageHelper.createSimple(message, placeholders);
    }

    public void execute(CommandSender sender, String[] args) {
        ValidationResponse response = validateInput(args);

        if (response != null) {
            sender.sendMessage(response.getMessage());
            return;
        }

        executeSubCommand(sender, args);
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
        return null;
    }
}
