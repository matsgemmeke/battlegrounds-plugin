package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.gui.view.PluginSettingsView;
import com.matsg.battlegrounds.gui.view.View;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Settings extends Command {

    private Battlegrounds plugin;
    private TaskRunner taskRunner;

    public Settings(Battlegrounds plugin, TaskRunner taskRunner, Translator translator) {
        super(translator);
        this.plugin = plugin;
        this.taskRunner = taskRunner;

        setDescription(createMessage(TranslationKey.DESCRIPTION_SETTINGS));
        setName("settings");
        setPermissionNode("battlegrounds.settings");
        setPlayerOnly(true);
        setUsage("bg settings");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        View view = new PluginSettingsView(plugin, taskRunner, translator);

        player.openInventory(view.getInventory());
    }
}
