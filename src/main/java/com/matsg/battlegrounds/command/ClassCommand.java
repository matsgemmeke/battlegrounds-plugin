package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.gui.ClassManagerView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand extends Command {

    public ClassCommand(BattlegroundsPlugin plugin) {
        super(plugin, "class", null, true, "classcreator");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        player.openInventory(new ClassManagerView(plugin, player).getInventory());
    }
}