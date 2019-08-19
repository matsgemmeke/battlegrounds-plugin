package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.*;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventChannel;
import com.matsg.battlegrounds.api.event.GameEndEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.game.BattleComponentContainer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.mode.AbstractGameMode;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.mode.zombies.handler.*;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import com.matsg.battlegrounds.mode.zombies.item.factory.PerkFactory;
import com.matsg.battlegrounds.mode.zombies.item.factory.PowerUpFactory;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Zombies extends AbstractGameMode {

    private ComponentContainer<Section> sectionContainer;
    private LoadoutFactory loadoutFactory;
    private PerkManager perkManager;
    private PowerUpManager powerUpManager;
    private Team team;
    private Wave wave;
    private WaveFactory waveFactory;
    private ZombiesConfig config;

    public Zombies(
            Battlegrounds plugin,
            Game game,
            Translator translator,
            SpawningBehavior spawningBehavior,
            PerkManager perkManager,
            PowerUpManager powerUpManager,
            Version version,
            ZombiesConfig config
    ) {
        super(plugin, game, translator, spawningBehavior);
        this.perkManager = perkManager;
        this.powerUpManager = powerUpManager;
        this.config = config;
        this.loadoutFactory = new LoadoutFactory();
        this.name = translator.translate(TranslationKey.ZOMBIES_NAME);
        this.sectionContainer = new BattleComponentContainer<>();
        this.shortName = translator.translate(TranslationKey.ZOMBIES_SHORT);
        this.team = new BattleTeam(1, "Players", null, ChatColor.WHITE);
        this.waveFactory = new WaveFactory(sectionContainer, version, config);

        teams.add(team);
    }

    public void onCreate() {
        PowerUpFactory powerUpFactory = new PowerUpFactory(plugin, this, translator);

        // Register gamemode specific event handlers
        plugin.getEventDispatcher().registerEventChannel(GamePlayerDamageEntityEvent.class, new EventChannel<>(
                new ZombiesDamageEventHandler(this)
        ));
        plugin.getEventDispatcher().registerEventChannel(GamePlayerKillEntityEvent.class, new EventChannel<>(
                new ZombiesKillEventHandler(this, powerUpFactory)
        ));
        plugin.getEventDispatcher().registerEventChannel(PlayerInteractEvent.class, new EventChannel<>(
                new ZombiesInteractEventHandler(game, this)
        ));
        plugin.getEventDispatcher().registerEventChannel(PlayerMoveEvent.class, new EventChannel<>(
                new ZombiesMoveEventHandler(game, this)
        ));
    }

    public void onDisable() {
        Arena arena = game.getArena();
        // Remove all instances of mob entities
        for (Entity entity : arena.getWorld().getEntities()) {
            Mob mob = game.getMobManager().findMob(entity);
            if (mob != null) {
                mob.remove();
            } else if (arena.contains(entity.getLocation()) && (!(entity instanceof Player))) {
                entity.remove();
            }
        }
    }

    public ZombiesConfig getConfig() {
        return config;
    }

    public PerkManager getPerkManager() {
        return perkManager;
    }

    public PowerUpManager getPowerUpManager() {
        return powerUpManager;
    }

    public ComponentContainer<Section> getSectionContainer() {
        return sectionContainer;
    }

    public GameModeType getType() {
        return GameModeType.ZOMBIES;
    }

    public Wave getWave() {
        return wave;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        team.addPlayer(gamePlayer);
    }

    public MysteryBox getActiveMysteryBox() {
        for (Section section : sectionContainer.getAll()) {
            for (MysteryBox mysteryBox : section.getMysteryBoxContainer().getAll()) {
                if (mysteryBox.isActive()) {
                    return mysteryBox;
                }
            }
        }
        return null;
    }

    public ArenaComponent getComponent(int id) {
        for (Section section : sectionContainer.getAll()) {
            if (section.getId() == id) {
                return section;
            }
            for (ArenaComponent component : section.getComponents()) {
                if (component.getId() == id) {
                    return component;
                }
            }
        }
        return null;
    }

    public ArenaComponent getComponent(Location location) {
        for (Section section : sectionContainer.getAll()) {
            ArenaComponent component = section.getComponent(location);

            if (component != null) {
                return component;
            }
        }
        return null;
    }

    public int getComponentCount() {
        int count = sectionContainer.getAll().size();
        for (Section section : sectionContainer.getAll()) {
            count += section.getComponentCount();
        }
        return count;
    }

    public Collection<ArenaComponent> getComponents() {
        List<ArenaComponent> list = new ArrayList<>();
        list.addAll(sectionContainer.getAll());

        for (Section section : sectionContainer.getAll()) {
            list.addAll(section.getComponents());
        }

        return Collections.unmodifiableList(list);
    }

    public Mob[] getNearbyEntities(Location location, Team team, double range) {
        List<Mob> list = new ArrayList<>();

        for (Entity entity : location.getWorld().getNearbyEntities(location, range, range, range)) {
            Mob mob = game.getMobManager().findMob(entity);
            if (mob != null && !mob.getBukkitEntity().isDead()) {
                list.add(mob);
            }
        }

        return list.toArray(new Mob[list.size()]);
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return null;
    }

    public GameScoreboard getScoreboard() {
        return new ZombiesScoreboard(game, this);
    }

    public Section getSection(String name) {
        for (Section section : sectionContainer.getAll()) {
            if (section.getName().equals(name)) {
                return section;
            }
        }
        return null;
    }

    public void loadData(Arena arena) {
        ItemFinder itemFinder = new ItemFinder(plugin);
        PerkFactory perkFactory = new PerkFactory(plugin, translator);

        ZombiesDataLoader dataLoader = new ZombiesDataLoader(this, game, arena, itemFinder, perkFactory, translator);
        dataLoader.load();
    }

    public void preparePlayer(GamePlayer gamePlayer) {
        super.preparePlayer(gamePlayer);

        Map<String, String> defaultLoadout = config.getDefaultLoadout();

        Firearm primary = defaultLoadout.get("primary") != null ? plugin.getFirearmFactory().make(defaultLoadout.get("primary")) : null;
        Equipment equipment = defaultLoadout.get("equipment") != null ? plugin.getEquipmentFactory().make(defaultLoadout.get("equipment")) : null;
        MeleeWeapon meleeWeapon = defaultLoadout.get("melee-weapon") != null ? plugin.getMeleeWeaponFactory().make(defaultLoadout.get("melee-weapon")) : null;

        if (primary != null) {
            primary.setAmmo(config.getDefaultMagazines() * primary.getMagazineSize());
        }

        ItemStack barricadeTool = new ItemStackBuilder(XMaterial.OAK_FENCE.parseMaterial())
                .setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.BARRICADE_TOOL))
                .build();

        Loadout loadout = loadoutFactory.make(
                1,
                null,
                primary,
                null,
                equipment,
                meleeWeapon,
                new Attachment[0],
                new Attachment[0],
                game,
                gamePlayer
        );

        gamePlayer.getPlayer().getInventory().setItem(ItemSlot.MISCELLANEOUS.getSlot(), barricadeTool);
        gamePlayer.setLoadout(loadout);
        gamePlayer.setPoints(config.getDefaultPoints());
        loadout.updateInventory();
    }

    public boolean removeComponent(ArenaComponent component) {
        int id = component.getId();
        if (sectionContainer.get(id) != null) {
            sectionContainer.remove(id);
            return true;
        }
        return false;
    }

    public void removePlayer(GamePlayer gamePlayer) {
        team.removePlayer(gamePlayer);
    }

    public void start() {
        super.start();

        if (sectionContainer.getAll().size() <= 0) {
            plugin.getLogger().severe("Unable to start gamemode Zombies in game " + game.getId());
            return;
        }

        for (Section section : sectionContainer.getAll()) {
            section.setLocked(!section.isUnlockedByDefault());
        }

        startWave(config.getStartingRound());

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            Title title = EnumTitle.ZOMBIES_START.getTitle();
            title.send(gamePlayer.getPlayer());
        }

        BattleSound.THUNDER.play(game);
    }

    public void startCountdown() {
        // Skip the countdown and start the game
        game.startGame();
        // Skip one game state and go straight to the ingame state
        game.setState(game.getState().next());
    }

    public void startWave(int round) {
        if (round > 1) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                if (!gamePlayer.getState().isAlive()) {
                    game.getPlayerManager().setVisible(gamePlayer, true);

                    gamePlayer.setState(PlayerState.ACTIVE);
                    gamePlayer.getState().apply(game, gamePlayer);
                }

                Player player = gamePlayer.getPlayer();
                player.setHealth(player.getMaxHealth());

                EnumTitle.ZOMBIES_NEW_WAVE.send(player, new Placeholder("bg_round", round));
            }
        }

        BattleEntityType entityType = getEntityTypeForRound(round);

        wave = waveFactory.make(game, entityType, round, getMobAmount(entityType, round, team.getTeamSize()));
        wave.setRunning(true);

        if (wave.getEntityType() == BattleEntityType.HELLHOUND) {
            BattleSound.HELLHOUND_ROUND_ANNOUNCEMENT.play(game);
        }

        int maxMobs = config.getMaxMobs(team.getTeamSize());
        int waveDelay = config.getWaveDelay() * 20;
        int period = (int) ((100 / round + 10) * config.getSpawnRate());

        new WaveSpawningThread(game, wave, maxMobs).runTaskTimer(waveDelay, period);
    }

    public void stop() {
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            Player player = gamePlayer.getPlayer();

            EnumTitle.ZOMBIES_GAME_OVER.send(player, new Placeholder("bg_round", wave.getRound()));
        }

        perkManager.clear();

        team = new BattleTeam(1, "Players", null, ChatColor.WHITE);
    }

    public void tick() {
        for (Objective objective : objectives) {
            if (objective.isAchieved()) {
                game.callEvent(new GameEndEvent(game, objective, team, teams));
                game.stop();
                break;
            }
        }
    }

    private BattleEntityType getEntityTypeForRound(int round) {
        double hellhoundChance = config.getHellhoundChance();
        Random random = new Random();

        if ((wave != null && wave.getEntityType() == BattleEntityType.ZOMBIE)
                && config.hasHellhoundEnabled()
                && (hellhoundChance > 1.0 && round % hellhoundChance == 0 || hellhoundChance < 1.0 && random.nextDouble() < hellhoundChance)) {
            return BattleEntityType.HELLHOUND;
        }

        return BattleEntityType.ZOMBIE;
    }

    private int getMobAmount(BattleEntityType entityType, int round, int players) {
        int amount = (int) ((Math.round(0.000058 * Math.pow(round, 3) + 0.074032 * Math.pow(round, 2) + 0.718119 * round + 4.738699)) * config.getMobMultiplier(players));
        amount += Math.round((double) amount / 100 * (new Random().nextInt(config.getVariation() * 2) - config.getVariation()));
        amount *= config.getMobAmount(entityType.toString());
        return amount;
    }
}
