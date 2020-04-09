package com.matsg.battlegrounds.mode.shared;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.gui.view.View;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import com.matsg.battlegrounds.mode.AbstractGameMode;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.Result;
import com.matsg.battlegrounds.gui.view.SelectLoadoutView;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.SelectLoadout;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class for classic gamemodes which are to be played in a default
 * arena. Contains functions which apply to these gamemodes by default.
 */
public abstract class ClassicGameMode extends AbstractGameMode {

    private TaskRunner taskRunner;
    private ViewFactory viewFactory;

    public ClassicGameMode(
            Battlegrounds plugin,
            GameModeType gameModeType,
            Game game,
            SpawningBehavior spawningBehavior,
            TaskRunner taskRunner,
            Translator translator,
            ViewFactory viewFactory
    ) {
        super(plugin, gameModeType, game, translator, spawningBehavior);
        this.taskRunner = taskRunner;
        this.viewFactory = viewFactory;
    }

    public void onDisable() {
        Arena arena = game.getArena();

        if (arena != null) {
            for (Entity entity : arena.getWorld().getEntitiesByClass(Item.class)) {
                if (arena.contains(entity.getLocation())) {
                    entity.remove();
                }
            }
        }
    }

    public ArenaComponent getComponent(int id) {
        // Classic game modes have no additional components by default
        return null;
    }

    public ArenaComponent getComponent(Location location) {
        // Classic game modes have no additional components by default
        return null;
    }

    public Collection<ArenaComponent> getComponents() {
        // Classic game modes have no additional components by default
        return Collections.EMPTY_SET;
    }

    public int getComponentCount() {
        // Classic game modes have no additional components by default
        return 0;
    }

    public BattleEntity[] getNearbyEnemies(Location location, Team team, double range) {
        List<BattleEntity> list = new ArrayList<>();
        for (Entity entity : game.getArena().getWorld().getNearbyEntities(location, range, range, range)) {
            if (entity instanceof Player) {
                GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer((Player) entity);
                if (gamePlayer != null && gamePlayer.getTeam() != team) {
                    list.add(gamePlayer);
                }
            }
        }
        return list.toArray(new BattleEntity[list.size()]);
    }

    public BattleEntity[] getNearbyEntities(Location location, double range) {
        List<BattleEntity> list = new ArrayList<>();
        for (Entity entity : game.getArena().getWorld().getNearbyEntities(location, range, range, range)) {
            if (entity instanceof Player) {
                GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer((Player) entity);
                if (gamePlayer != null) {
                    list.add(gamePlayer);
                }
            }
        }
        return list.toArray(new BattleEntity[list.size()]);
    }

    public Location getRespawnLocation(GamePlayer gamePlayer) {
        Spawn spawn = getRespawnPoint(gamePlayer);
        return spawn.getLocation();
    }

    public abstract Spawn getRespawnPoint(GamePlayer gamePlayer);

    public boolean hasBloodEffectDisplay(BattleEntityType entityType) {
        return plugin.getBattlegroundsConfig().getDisplayBloodEffect(entityType.toString());
    }

    public abstract Countdown makeCountdown();

    public void preparePlayer(GamePlayer gamePlayer) {
        super.preparePlayer(gamePlayer);

        Player player = gamePlayer.getPlayer();
        player.getInventory().setArmorContents(new ItemStack[] {
                null,
                null,
                new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                        .addItemFlags(ItemFlag.values())
                        .setColor(gamePlayer.getTeam().getArmorColor())
                        .setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.ARMOR_VEST.getPath()))
                        .setUnbreakable(true)
                        .build(),
                new ItemStackBuilder(Material.LEATHER_HELMET)
                        .addItemFlags(ItemFlag.values())
                        .setColor(gamePlayer.getTeam().getArmorColor())
                        .setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.ARMOR_HELMET.getPath()))
                        .setUnbreakable(true)
                        .build()
        });

        String selectLoadoutDisplayName = ChatColor.WHITE + translator.translate(TranslationKey.CHANGE_LOADOUT.getPath());
        SelectLoadout selectLoadout = new SelectLoadout(selectLoadoutDisplayName, game, viewFactory);

        game.getItemRegistry().addItem(selectLoadout);
        gamePlayer.getItems().add(selectLoadout);
        player.getInventory().setItem(ItemSlot.MISCELLANEOUS.getSlot(), selectLoadout.getItemStack());
    }

    public void respawnPlayer(GamePlayer gamePlayer) {
        if (gamePlayer.getSelectedLoadout() != null && !gamePlayer.getLoadout().equals(gamePlayer.getSelectedLoadout())) {
            game.getPlayerManager().changeLoadout(gamePlayer, gamePlayer.getSelectedLoadout().clone());
        }

        Spawn spawn = getRespawnPoint(gamePlayer);
        spawn.setOccupant(gamePlayer);

        // Wait 5 seconds before resetting the spawn state
        taskRunner.runTaskLater(() -> spawn.setOccupant(null), 100);
    }

    public Countdown startCountdown() {
        game.setState(game.getState().next());
        game.updateSign();

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            if (gamePlayer.getLoadout() == null) {
                LoadoutFactory loadoutFactory = new LoadoutFactory();

                View view = viewFactory.make(SelectLoadoutView.class, instance -> {
                    instance.setGame(game);
                    instance.setGamePlayer(gamePlayer);
                    instance.setLoadoutFactory(loadoutFactory);
                });
                view.openInventory(gamePlayer.getPlayer());
           }
        }

        return makeCountdown();
    }

    public void stop() {
        Objective objective = getAchievedObjective();

        for (Team team : teams) {
            Result result = Result.getResult(team, getSortedTeams());
            if (result != null) {
                for (GamePlayer gamePlayer : team.getPlayers()) {
                    String resultText = translator.translate(result.getTranslationKey().getPath());

                    Title title = objective.getTitle().clone();
                    title.setTitleText(translator.createSimpleMessage(title.getTitleText(), new Placeholder("bg_result", resultText)));
                    title.setSubText(translator.createSimpleMessage(title.getSubText(), new Placeholder("bg_result", resultText)));
                    title.send(gamePlayer.getPlayer());
                }
            }
        }
    }

    public void tick() {
        GameScoreboard scoreboard = getScoreboard();

        if (scoreboard != null) {
            scoreboard.display(game);
        }

        for (Objective objective : objectives) {
            if (objective.isAchieved()) {
                plugin.getEventDispatcher().dispatchExternalEvent(new GameEndEvent(game, objective, null, getSortedTeams()));
                game.stop();
                break;
            }
        }
    }
}
