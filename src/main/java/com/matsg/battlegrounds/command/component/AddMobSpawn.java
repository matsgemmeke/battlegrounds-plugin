package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.MobSpawn;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validate.SectionNameValidator;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.game.mode.zombies.ZombiesMobSpawn;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.entity.Player;

public class AddMobSpawn extends ComponentCommand {

    private MessageHelper messageHelper;

    public AddMobSpawn(Battlegrounds plugin) {
        super(plugin);
        this.messageHelper = new MessageHelper();

        registerValidator(new GameModeUsageValidator(plugin, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        Arena arena = context.getArena();
        Game game = context.getGame();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[3]);

        MobSpawn mobSpawn = new ZombiesMobSpawn(componentId, player.getLocation());

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
