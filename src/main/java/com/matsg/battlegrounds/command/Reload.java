package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;

public class Reload extends SubCommand {

    public Reload(Battlegrounds plugin) {
        super(plugin, "reload", Message.create(TranslationKey.DESCRIPTION_RELOAD),
                "bg reload", "battlegrounds.reload", false, "rel");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!plugin.loadConfigs()) {
            sender.sendMessage(Message.create(TranslationKey.RELOAD_FAILED));
            return;
        }
        sender.sendMessage(Message.create(TranslationKey.RELOAD_SUCCESS));
    }
}