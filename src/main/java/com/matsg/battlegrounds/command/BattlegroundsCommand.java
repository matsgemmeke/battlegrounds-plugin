package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattlegroundsCommand extends Command {

    public BattlegroundsCommand(BattlegroundsPlugin plugin) {
        super(plugin, "battlegrounds", "b", "bg", "battleground");

        commands.add(new AddSpawn(plugin));
        commands.add(new CreateArena(plugin));
        commands.add(new CreateGame(plugin));
        commands.add(new Join(plugin));
        commands.add(new Leave(plugin));
        commands.add(new Reload(plugin));
        commands.add(new RemoveArena(plugin));
        commands.add(new RemoveGame(plugin));
        commands.add(new RemoveSpawn(plugin));
        commands.add(new SetGameSign(plugin));
        commands.add(new SetLobby(plugin));
        commands.add(new SetMainLobby(plugin));

        commands.add(new Help(plugin, commands.toArray(new SubCommand[commands.size()])));
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            SubCommand help = getSubCommand("help");
            if (help != null) {
                help.execute(sender, args);
            }
            return;
        }

        SubCommand subCommand = getSubCommand(args[0]);

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

        try {
            subCommand.execute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(createMessage(TranslationKey.COMMAND_ERROR));
            e.printStackTrace();
        }
    }
}