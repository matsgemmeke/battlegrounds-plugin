package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.LoadoutManagerView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadoutCommand extends Command {

    private int minLevel;

    public LoadoutCommand(BattlegroundsPlugin plugin) {
        super(plugin);
        this.minLevel = plugin.getBattlegroundsConfig().loadoutCreationLevel;

        setName("loadout");
        setPlayerOnly(true);

        subCommands.add(new Rename(plugin));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        StoredPlayer storedPlayer = plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId());

        if (storedPlayer == null && (storedPlayer = plugin.getPlayerStorage().registerPlayer(player)) == null || plugin.getLevelConfig().getLevel(storedPlayer.getExp()) < minLevel) {
            player.sendMessage(createMessage(TranslationKey.CUSTOM_LOADOUT_LOCKED, new Placeholder("bg_level", minLevel)));
            return;
        }

        if (args.length == 0) {
            player.openInventory(new LoadoutManagerView(plugin, plugin.getTranslator(), player).getInventory());
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
