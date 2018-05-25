package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.gui.WeaponsView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WeaponsCommand extends Command {

    public WeaponsCommand(BattlegroundsPlugin plugin) {
        super(plugin, "weapons", "zombies.weapons", true, new String[0]);
    }

    public void execute(CommandSender sender, String[] args) {
        ((Player) sender).openInventory(new WeaponsView(plugin).getInventory());
    }
}