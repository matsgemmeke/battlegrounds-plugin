package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.gui.view.PluginOverviewView;
import com.matsg.battlegrounds.gui.view.View;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Overview extends Command {

    private Battlegrounds plugin;
    private TaskRunner taskRunner;

    public Overview(Battlegrounds plugin, TaskRunner taskRunner, Translator translator) {
        super(translator);
        this.plugin = plugin;
        this.taskRunner = taskRunner;

        setDescription(createMessage(TranslationKey.DESCRIPTION_OVERVIEW));
        setName("overview");
        setPermissionNode("battlegrounds.overview");
        setPlayerOnly(true);
        setUsage("bg overview");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        View view = new PluginOverviewView(plugin, taskRunner, translator);

        player.openInventory(view.getInventory());
    }
}
