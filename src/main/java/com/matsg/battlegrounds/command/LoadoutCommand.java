package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.gui.LoadoutManagerView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadoutCommand extends Command {

    public LoadoutCommand(BattlegroundsPlugin plugin) {
        super(plugin, "loadout", null, true, "loadoutcreator");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        player.openInventory(new LoadoutManagerView(plugin, player).getInventory());
    }
}