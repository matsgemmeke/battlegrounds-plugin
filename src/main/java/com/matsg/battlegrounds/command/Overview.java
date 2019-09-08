package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.gui.PluginOverviewView;
import com.matsg.battlegrounds.gui.View;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Overview extends Command {

    public Overview(Battlegrounds plugin) {
        super(plugin);
        setDescription(createMessage(TranslationKey.DESCRIPTION_OVERVIEW));
        setName("overview");
        setPermissionNode("battlegrounds.overview");
        setPlayerOnly(true);
        setUsage("bg overview");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        View view = new PluginOverviewView(plugin, plugin.getTranslator());

        player.openInventory(view.getInventory());
    }
}
