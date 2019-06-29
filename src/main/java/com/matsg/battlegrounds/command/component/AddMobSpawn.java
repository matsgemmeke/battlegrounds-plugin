package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.MobSpawn;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesMobSpawn;
import org.bukkit.entity.Player;

public class AddMobSpawn extends ComponentCommand {

    private Translator translator;

    public AddMobSpawn(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;

        registerValidator(new GameModeUsageValidator(plugin, translator, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin, translator, 4));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        Arena arena = context.getArena();
        Game game = context.getGame();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[4]);

        MobSpawn mobSpawn = new ZombiesMobSpawn(componentId, player.getLocation());

        section.getMobSpawnContainer().add(mobSpawn);

        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", player.getLocation(), false);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "mobspawn");
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.MOBSPAWN_ADD_NO_BARRICADE,
                new Placeholder("bg_id", componentId),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
