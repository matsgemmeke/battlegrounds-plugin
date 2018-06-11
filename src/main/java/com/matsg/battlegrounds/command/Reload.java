package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {

    public Reload(Battlegrounds plugin) {
        super(plugin, "reload", EnumMessage.DESCRIPTION_RELOAD.getMessage(),
                "bg reload", "battlegrounds.reload", false, "rel");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!plugin.loadConfigs()) {
            EnumMessage.RELOAD_FAILED.send(sender);
            return;
        }
        sender.sendMessage(EnumMessage.RELOAD_SUCCESS.getMessage());

        Player player = (Player) sender;
        player.sendMessage(plugin.getLevelConfig().getLevel(plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId()).getExp()) + " ");
    }
}