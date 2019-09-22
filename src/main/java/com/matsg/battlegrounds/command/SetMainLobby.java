package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMainLobby extends Command {

    private CacheYaml cache;

    public SetMainLobby(Translator translator, CacheYaml cache) {
        super(translator);
        this.cache = cache;

        setAliases("sml");
        setDescription(createMessage(TranslationKey.DESCRIPTION_SETMAINLOBBY));
        setName("setmainlobby");
        setPermissionNode("battlegrounds.setmainlobby");
        setPlayerOnly(true);
        setUsage("bg setmainlobby");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        cache.setLocation("mainlobby", player.getLocation(), false);
        cache.save();

        player.sendMessage(createMessage(TranslationKey.MAINLOBBY_SET));
    }
}
