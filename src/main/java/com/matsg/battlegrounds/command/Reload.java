package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {

    public Reload(Battlegrounds plugin) {
        super(plugin, "reload", EnumMessage.DESCRIPTION_RELOAD.getMessage(),
                "bg reload", "battlegrounds.reload", false, "rel");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!plugin.loadConfigs()) {
            EnumMessage.RELOAD_FAILED.send(sender);
            return;
        }
        sender.sendMessage(EnumMessage.RELOAD_SUCCESS.getMessage());

        Player player = (Player) sender;
        Game game = plugin.getGameManager().getGame(player);
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        FireArm fireArm = plugin.getFireArmConfig().get(args[1].replace("_", " "));

        fireArm.setGame(game);
        fireArm.setItemSlot(ItemSlot.FIREARM_PRIMARY);
        fireArm.setGamePlayer(gamePlayer);
        fireArm.update();

        gamePlayer.getLoadout().setPrimary(fireArm);
        game.getItemRegistry().addItem(fireArm);
    }
}