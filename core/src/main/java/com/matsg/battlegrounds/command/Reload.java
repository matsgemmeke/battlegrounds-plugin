package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import org.bukkit.command.CommandSender;

public class Reload extends Command {

    private Battlegrounds plugin;

    public Reload(Battlegrounds plugin, Translator translator) {
        super(translator);
        this.plugin = plugin;

        setDescription(createMessage(TranslationKey.DESCRIPTION_RELOAD));
        setName("reload");
        setPermissionNode("battlegrounds.reload");
        setUsage("bg reload");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!plugin.loadConfigs()) {
            sender.sendMessage(createMessage(TranslationKey.RELOAD_FAILED));
            return;
        }

        sender.sendMessage(createMessage(TranslationKey.RELOAD_SUCCESS));
    }
}
