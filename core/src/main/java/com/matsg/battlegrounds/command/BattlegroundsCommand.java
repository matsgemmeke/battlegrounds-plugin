package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.command.validator.ValidationResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattlegroundsCommand extends Command {

    public BattlegroundsCommand(Translator translator) {
        super(translator);
        setAliases("b", "bg", "battleground");
        setName("battlegrounds");
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            getSubCommand("help").execute(sender, args);
            return;
        }

        Command subCommand = getSubCommand(args[0]);

        if (subCommand == null) {
            sender.sendMessage(createMessage(TranslationKey.INVALID_ARGUMENTS));
            return;
        }

        if (subCommand.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(createMessage(TranslationKey.INVALID_SENDER));
            return;
        }

        String permissionNode = subCommand.getPermissionNode();

        if (permissionNode != null && permissionNode.length() > 0 && !sender.hasPermission(permissionNode)) {
            sender.sendMessage(createMessage(TranslationKey.NO_PERMISSION));
            return;
        }

        ValidationResponse response = subCommand.validateInput(args);

        if (!response.passed()) {
            sender.sendMessage(response.getMessage());
            return;
        }

        try {
            subCommand.execute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(createMessage(TranslationKey.COMMAND_ERROR));
        }
    }
}
