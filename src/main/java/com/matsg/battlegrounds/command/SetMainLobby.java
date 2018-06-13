package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMainLobby extends SubCommand {

    public SetMainLobby(Battlegrounds plugin) {
        super(plugin, "setmainlobby", EnumMessage.DESCRIPTION_SETMAINLOBBY.getMessage(),
                "bg setmainlobby", null, true, "sml");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        plugin.getBattlegroundsCache().setLocation("mainlobby", player.getLocation(), true);
        plugin.getBattlegroundsCache().save();

        player.sendMessage(EnumMessage.MAINLOBBY_SET.getMessage());
    }
}