package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.ArenaNameValidator;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import org.bukkit.command.CommandSender;

public class RemoveComponent extends Command {

    public RemoveComponent(Battlegrounds plugin) {
        super(plugin);
        setAliases("remove", "rc");
        setDescription(createMessage(TranslationKey.DESCRIPTION_REMOVECOMPONENT));
        setName("removecomponent");
        setPermissionNode("battlegrounds.removecomponent");
        setUsage("bg removecomponent [id] [arena] [component]");

        registerValidator(new GameIdValidator(plugin, plugin.getTranslator(), true));
        registerValidator(new ArenaNameValidator(plugin, plugin.getTranslator(), true));
    }

    public void execute(CommandSender sender, String[] args) {
        Game game = plugin.getGameManager().getGame(Integer.parseInt(args[1]));
        Arena arena = plugin.getGameManager().getArena(game, args[2].replaceAll("_", " "));

        if (args.length == 3) {
            sender.sendMessage(createMessage(TranslationKey.SPECIFY_COMPONENT_ID));
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[3]);
        } catch (Exception e) {
            sender.sendMessage(createMessage(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[3])));
            return;
        }

        ArenaComponent component;

        if ((component = arena.getComponent(id)) == null && (component = game.getGameMode().getComponent(id)) == null) {
            sender.sendMessage(createMessage(TranslationKey.COMPONENT_NOT_EXISTS,
                    new Placeholder("bg_arena", arena.getName()),
                    new Placeholder("bg_component_id", id)
            ));
            return;
        }

        arena.removeComponent(component);

        for (GameMode gameMode : game.getConfiguration().getGameModes()) {
            gameMode.removeComponent(component);
        }

        game.getDataFile().set("arena." + arena.getName() + ".component." + id, null);
        game.getDataFile().save();

        sender.sendMessage(createMessage(TranslationKey.COMPONENT_REMOVE,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", id)
        ));
    }
}
