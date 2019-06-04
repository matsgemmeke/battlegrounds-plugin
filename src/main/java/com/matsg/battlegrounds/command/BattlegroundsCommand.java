package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.command.validator.ValidationResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattlegroundsCommand extends Command {

    public BattlegroundsCommand(Battlegrounds plugin) {
        super(plugin);
        setAliases("b", "bg", "battleground");
        setName("battlegrounds");

        subCommands.add(new AddComponent(plugin));
        subCommands.add(new CreateArena(plugin));
        subCommands.add(new CreateGame(plugin));
        subCommands.add(new Join(plugin));
        subCommands.add(new Leave(plugin));
        subCommands.add(new Reload(plugin));
        subCommands.add(new RemoveArena(plugin));
        subCommands.add(new RemoveComponent(plugin));
        subCommands.add(new RemoveGame(plugin));
        subCommands.add(new SetGameSign(plugin));
        subCommands.add(new SetLobby(plugin));
        subCommands.add(new SetMainLobby(plugin));

        subCommands.add(new Help(plugin, subCommands.toArray(new Command[subCommands.size()])));
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
            e.printStackTrace();
        }
    }
}
