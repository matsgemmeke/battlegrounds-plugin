package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.PluginSettingsView;
import com.matsg.battlegrounds.gui.view.View;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Settings extends Command {

    private ViewFactory viewFactory;

    public Settings(Translator translator, ViewFactory viewFactory) {
        super(translator);
        this.viewFactory = viewFactory;

        setDescription(createMessage(TranslationKey.DESCRIPTION_SETTINGS));
        setName("settings");
        setPermissionNode("battlegrounds.settings");
        setPlayerOnly(true);
        setUsage("bg settings");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        View view = viewFactory.make(PluginSettingsView.class, instance -> {});
        view.openInventory(player);
    }
}
