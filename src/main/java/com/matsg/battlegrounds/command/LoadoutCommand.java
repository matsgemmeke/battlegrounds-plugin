package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.LoadoutManagerView;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadoutCommand extends Command {

    private int minLevel;

    public LoadoutCommand(BattlegroundsPlugin plugin) {
        super(plugin, "loadout", "loadoutcreator");
        this.minLevel = plugin.getBattlegroundsConfig().loadoutCreationLevel;

        setPlayerOnly(true);

        commands.add(new Rename(plugin));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        StoredPlayer storedPlayer = plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId());

        if (storedPlayer == null && (storedPlayer = plugin.getPlayerStorage().registerPlayer(player.getUniqueId(), player.getName())) == null || plugin.getLevelConfig().getLevel(storedPlayer.getExp()) < minLevel) {
            player.sendMessage(createMessage(TranslationKey.CUSTOM_LOADOUT_LOCKED, new Placeholder("bg_level", minLevel)));
            return;
        }

        if (args.length == 0) {
            player.openInventory(new LoadoutManagerView(plugin, player).getInventory());
            return;
        }

        SubCommand subCommand = getSubCommand(args[0]);

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