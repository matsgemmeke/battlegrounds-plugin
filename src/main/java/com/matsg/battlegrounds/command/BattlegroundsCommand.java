package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattlegroundsCommand extends Command {

    public BattlegroundsCommand(BattlegroundsPlugin plugin) {
        super(plugin, "battlegrounds", null, false, "b", "bg", "battleground");

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
            EnumMessage.INVALID_ARGUMENTS.send(sender);
            return;
        }

        if (subCommand.isPlayerOnly() && !(sender instanceof Player)) {
            EnumMessage.INVALID_SENDER.send(sender);
            return;
        }

        String permissionNode = subCommand.getPermissionNode();

        if (permissionNode != null && permissionNode.length() > 0 && !sender.hasPermission(permissionNode)) {
            EnumMessage.NO_PERMISSION.send(sender);
            return;
        }

        try {
            subCommand.execute(sender, args);
        } catch (Exception e) {
            EnumMessage.COMMAND_ERROR.send(sender);
            e.printStackTrace();
        }
    }
}