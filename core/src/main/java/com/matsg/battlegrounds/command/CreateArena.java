package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.command.validator.ArenaNameValidator;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import com.matsg.battlegrounds.game.BattleArena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArena extends Command {

    private GameManager gameManager;
    private SelectionManager selectionManager;

    public CreateArena(Translator translator, GameManager gameManager, SelectionManager selectionManager) {
        super(translator);
        this.gameManager = gameManager;
        this.selectionManager = selectionManager;

        setAliases("ca");
        setDescription(createMessage(TranslationKey.DESCRIPTION_CREATEARENA));
        setName("createarena");
        setPermissionNode("battlegrounds.createarena");
        setPlayerOnly(true);
        setUsage("bg createarena [id] [arena]");

        registerValidator(new GameIdValidator(gameManager, translator, true));
        registerValidator(new ArenaNameValidator(gameManager, translator, false));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        int id = Integer.parseInt(args[1]);

        Game game = gameManager.getGame(id);
        Selection selection = selectionManager.getSelection(player);

        if (selection == null) {
            player.sendMessage(createMessage(TranslationKey.NO_SELECTION));
            return;
        }

        Location max = selection.getMaximumPoint();
        Location min = selection.getMinimumPoint();

        String arenaName = args[2].replaceAll("_", " ");
        World world = player.getWorld();
        Arena arena = new BattleArena(arenaName, world, max, min);

        game.getArenaList().add(arena);
        game.getDataFile().set("arena." + arenaName + ".world", world.getName());

        if (max != null && min != null) {
            game.getDataFile().setLocation("arena." + arenaName + ".max", max, true);
            game.getDataFile().setLocation("arena." + arenaName + ".min", min, true);
        }

        game.getDataFile().save();

        if (game.getArena() == null) {
            arena.setActive(true);
            game.setArena(arena);
        }

        player.sendMessage(createMessage(TranslationKey.ARENA_CREATE,
                new Placeholder("bg_game", id),
                new Placeholder("bg_arena", arenaName)
        ));
    }
}
