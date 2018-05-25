package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.LoadoutManagerView;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadoutCommand extends Command {

    private int minLevel;

    public LoadoutCommand(BattlegroundsPlugin plugin) {
        super(plugin, "loadout", null, true, "loadoutcreator");
        this.minLevel = plugin.getBattlegroundsConfig().loadoutCreationLevel;
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getExp()) < minLevel) {
            EnumMessage.CUSTOM_LOADOUT_LOCKED.send(player, new Placeholder("bg_level", minLevel));
            return;
        }

        player.openInventory(new LoadoutManagerView(plugin, player).getInventory());
    }
}