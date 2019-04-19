package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.MobSpawn;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.component.ArenaMobSpawn;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.entity.Player;

public class AddMobSpawn implements ComponentCommand {

    private MessageHelper messageHelper;

    public AddMobSpawn() {
        this.messageHelper = new MessageHelper();
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Arena arena = context.getArena();
        Game game = context.getGame();
        Player player = context.getPlayer();
        Section section = context.getSection();

        if (section == null) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_SECTION_NAME));
            return;
        }

        MobSpawn mobSpawn = new ArenaMobSpawn(componentId, player.getLocation());

        section.getMobSpawnContainer().add(mobSpawn);

        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", player.getLocation(), false);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "mobspawn");
        game.getDataFile().save();

        player.sendMessage(messageHelper.create(TranslationKey.MOBSPAWN_ADD_NO_BARRICADE,
                new Placeholder("bg_id", componentId),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
