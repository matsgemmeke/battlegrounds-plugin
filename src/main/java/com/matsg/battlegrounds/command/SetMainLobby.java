package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMainLobby extends SubCommand {

    public SetMainLobby(Battlegrounds plugin) {
        super(plugin, "setmainlobby", Message.create(TranslationKey.DESCRIPTION_SETMAINLOBBY),
                "bg setmainlobby", "battlegrounds.setmainlobby", true, "sml");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        plugin.getBattlegroundsCache().setLocation("mainlobby", player.getLocation(), true);
        plugin.getBattlegroundsCache().save();

        player.sendMessage(Message.create(TranslationKey.MAINLOBBY_SET));
    }
}