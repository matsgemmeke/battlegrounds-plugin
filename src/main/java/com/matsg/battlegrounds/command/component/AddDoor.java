package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Selection;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class AddDoor implements ComponentCommand {

    private Battlegrounds plugin;
    private MessageHelper messageHelper;

    public AddDoor(Battlegrounds plugin) {
        this.plugin = plugin;
        this.messageHelper = new MessageHelper();
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        Selection selection = plugin.getSelectionManager().getSelection(player);

        if (!selection.isComplete()) {
            player.sendMessage(messageHelper.create(TranslationKey.NO_SELECTION));
            return;
        }

        Block block = selection.getCenter().getBlock();
    }
}
