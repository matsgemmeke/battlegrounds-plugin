package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Wand extends Command {

    public Wand(Translator translator) {
        super(translator);
        setAliases("w");
        setDescription(createMessage(TranslationKey.DESCRIPTION_WAND));
        setName("wand");
        setPermissionNode("battlegrounds.wand");
        setPlayerOnly(true);
        setUsage("bg wand");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.getInventory().addItem(new ItemStack(XMaterial.STICK.parseMaterial()));
        player.sendMessage(createMessage(TranslationKey.GIVE_SELECTION_TOOL));
    }
}
