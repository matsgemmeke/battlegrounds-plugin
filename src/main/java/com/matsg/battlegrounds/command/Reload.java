package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.command.CommandSender;

public class Reload extends SubCommand {

    public Reload(Battlegrounds plugin) {
        super(plugin);
        setDescription(createMessage(TranslationKey.DESCRIPTION_RELOAD));
        setName("reload");
        setPermissionNode("battlegrounds.reload");
        setUsage("bg reload");
    }

    public void executeSubCommand(CommandSender sender, String[] args) {
        if (!plugin.loadConfigs()) {
            sender.sendMessage(createMessage(TranslationKey.RELOAD_FAILED));
            return;
        }
        sender.sendMessage(createMessage(TranslationKey.RELOAD_SUCCESS));
    }
}