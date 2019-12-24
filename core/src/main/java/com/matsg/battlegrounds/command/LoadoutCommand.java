package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.LoadoutManagerView;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadoutCommand extends Command {

    private int minLevel;
    private LevelConfig levelConfig;
    private PlayerStorage playerStorage;
    private ViewFactory viewFactory;

    public LoadoutCommand(Translator translator, LevelConfig levelConfig, PlayerStorage playerStorage, ViewFactory viewFactory, int minLevel) {
        super(translator);
        this.levelConfig = levelConfig;
        this.playerStorage = playerStorage;
        this.viewFactory = viewFactory;
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
            LoadoutFactory loadoutFactory = new LoadoutFactory();

            View view = viewFactory.make(LoadoutManagerView.class, instance -> {
                instance.setLoadoutFactory(loadoutFactory);
                instance.setPlayer(player);
            });
            view.openInventory(player);
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
