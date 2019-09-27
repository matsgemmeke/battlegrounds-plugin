package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.LoadoutManagerView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadoutCommand extends Command {

    private Battlegrounds plugin;
    private int minLevel;
    private LevelConfig levelConfig;
    private PlayerStorage playerStorage;

    public LoadoutCommand(Battlegrounds plugin, Translator translator, LevelConfig levelConfig, PlayerStorage playerStorage, int minLevel) {
        super(translator);
        this.plugin = plugin;
        this.levelConfig = levelConfig;
        this.playerStorage = playerStorage;
        this.minLevel = minLevel;

        setName("loadout");
        setPlayerOnly(true);
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        StoredPlayer storedPlayer = playerStorage.getStoredPlayer(player.getUniqueId());

        if (storedPlayer == null && (storedPlayer = playerStorage.registerPlayer(player)) == null || levelConfig.getLevel(storedPlayer.getExp()) < minLevel) {
            player.sendMessage(createMessage(TranslationKey.CUSTOM_LOADOUT_LOCKED, new Placeholder("bg_level", minLevel)));
            return;
        }

        if (args.length == 0) {
            player.openInventory(new LoadoutManagerView(plugin, translator, player).getInventory());
            return;
        }

        Command subCommand = getSubCommand(args[0]);

        if (subCommand == null) {
            return;
        }

        try {
            subCommand.execute(sender, args);
        } catch (Exception e) {
            player.sendMessage(createMessage(TranslationKey.COMMAND_ERROR));
            e.printStackTrace();
        }
    }
}
