package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.LoadoutManagerView;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadoutCommand extends Command {

    private int minLevel;

    public LoadoutCommand(BattlegroundsPlugin plugin) {
        super(plugin, "loadout", null, true, "loadoutcreator");
        this.minLevel = plugin.getBattlegroundsConfig().loadoutCreationLevel;

        commands.add(new Rename(plugin));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        StoredPlayer storedPlayer = plugin.getPlayerStorage().getStoredPlayer(player.getUniqueId());

        if (storedPlayer == null || plugin.getLevelConfig().getLevel(storedPlayer.getExp()) < minLevel) {
            EnumMessage.CUSTOM_LOADOUT_LOCKED.send(player, new Placeholder("bg_level", minLevel));
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
            EnumMessage.COMMAND_ERROR.send(sender);
            e.printStackTrace();
        }
    }
}