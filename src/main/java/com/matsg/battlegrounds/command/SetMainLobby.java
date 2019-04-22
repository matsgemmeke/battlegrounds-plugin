package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMainLobby extends Command {

    public SetMainLobby(Battlegrounds plugin) {
        super(plugin);
        setAliases("sml");
        setDescription(createMessage(TranslationKey.DESCRIPTION_SETMAINLOBBY));
        setName("setmainlobby");
        setPermissionNode("battlegrounds.setmainlobby");
        setPlayerOnly(true);
        setUsage("bg setmainlobby");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        plugin.getBattlegroundsCache().setLocation("mainlobby", player.getLocation(), false);
        plugin.getBattlegroundsCache().save();

        player.sendMessage(createMessage(TranslationKey.MAINLOBBY_SET));
    }
}
