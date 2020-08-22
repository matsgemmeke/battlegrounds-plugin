package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.entity.*;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.*;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.game.BattleComponentContainer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.mode.AbstractGameMode;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.shared.NulledCountdown;
import com.matsg.battlegrounds.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.mode.zombies.component.*;
import com.matsg.battlegrounds.mode.zombies.component.factory.*;
import com.matsg.battlegrounds.mode.zombies.component.mysterybox.MovingState;
import com.matsg.battlegrounds.mode.zombies.handler.*;
import com.matsg.battlegrounds.ItemFinder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import com.matsg.battlegrounds.mode.zombies.item.factory.PerkFactory;
import com.matsg.battlegrounds.mode.zombies.item.factory.PowerUpFactory;
import com.matsg.battlegrounds.util.Title;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Zombies extends AbstractGameMode {

    private BarricadeFactory barricadeFactory;
    private ComponentContainer<Section> sectionContainer;
    private DoorFactory doorFactory;
    private InternalsProvider internals;
    private LoadoutFactory loadoutFactory;
    private MobSpawnFactory mobSpawnFactory;
    private MysteryBoxFactory mysteryBoxFactory;
    private PerkMachineFactory perkMachineFactory;
    private PerkManager perkManager;
    private PowerUpManager powerUpManager;
    private SectionFactory sectionFactory;
    private TaskRunner taskRunner;
    private Team team;
    private ViewFactory viewFactory;
    private WallWeaponFactory wallWeaponFactory;
    private Wave wave;
    private WaveFactory waveFactory;
    private ZombiesConfig config;

    public Zombies(
            Battlegrounds plugin,
            Game game,
            Translator translator,
            InternalsProvider internals,
            SpawningBehavior spawningBehavior,
            PerkManager perkManager,
            PowerUpManager powerUpManager,
            TaskRunner taskRunner,
            ViewFactory viewFactory,
            ZombiesConfig config
    ) {
        super(plugin, GameModeType.ZOMBIES, game, translator, spawningBehavior);
        this.internals = internals;
        this.perkManager = perkManager;
        this.powerUpManager = powerUpManager;
        this.taskRunner = taskRunner;
        this.viewFactory = viewFactory;
        this.config = config;
        this.loadoutFactory = new LoadoutFactory();
        this.name = translator.translate(GameModeType.ZOMBIES.getNamePath());
        this.sectionContainer = new BattleComponentContainer<>();
        this.shortName = translator.translate(GameModeType.ZOMBIES.getShortNamePath());
        this.team = new BattleTeam(1, "Players", null, ChatColor.WHITE);
        this.waveFactory = new WaveFactory(sectionContainer, internals, plugin, config);

        teams.add(team);

        registerComponentFactories();
    }

    public void onCreate() {
        PowerUpFactory powerUpFactory = new PowerUpFactory(plugin, game, this, internals, taskRunner, translator);

        // Register gamemode specific event handlers
        plugin.getEventDispatcher().registerEventChannel(BlockPlaceEvent.class, new EventChannel<>(
                new BarricadeRepairHandler(game, this, internals, translator)
        ));
        plugin.getEventDispatcher().registerEventChannel(GamePlayerDamageEntityEvent.class, new EventChannel<>(
                new ZombiesDamageEventHandler(this, internals, translator)
        ));
        plugin.getEventDispatcher().registerEventChannel(GamePlayerDeathEvent.class, new EventChannel<>(
                new GamePlayerDeathEventHandler(game, this, perkManager)
        ));
        plugin.getEventDispatcher().registerEventChannel(GamePlayerKillEntityEvent.class, new EventChannel<>(
                new ZombiesKillEventHandler(this, internals, powerUpFactory, taskRunner, translator)
        ));
        plugin.getEventDispatcher().registerEventChannel(PlayerInteractEntityEvent.class, new EventChannel<>(
                new WallWeaponInteractHandler(game, this)
        ));
        plugin.getEventDispatcher().registerEventChannel(PlayerMoveEvent.class, new EventChannel<>(
                new BarricadePlayerPassHandler(game, this)
        ));
    }

    public void onDisable() {
        for (Section section : sectionContainer.getAll()) {
            for (MysteryBox mysteryBox : section.getMysteryBoxContainer().getAll()) {
                mysteryBox.getState().remove();
            }
        }

        removeMobs();
        perkManager.clear();
        powerUpManager.clear();
    }

    public BarricadeFactory getBarricadeFactory() {
        return barricadeFactory;
    }

    public ZombiesConfig getConfig() {
        return config;
    }

    public DoorFactory getDoorFactory() {
        return doorFactory;
    }

    public MobSpawnFactory getMobSpawnFactory() {
        return mobSpawnFactory;
    }

    public MysteryBoxFactory getMysteryBoxFactory() {
        return mysteryBoxFactory;
    }

    public PerkMachineFactory getPerkMachineFactory() {
        return perkMachineFactory;
    }

    public PowerUpManager getPowerUpManager() {
        return powerUpManager;
    }

    public ComponentContainer<Section> getSectionContainer() {
        return sectionContainer;
    }

    public SectionFactory getSectionFactory() {
        return sectionFactory;
    }

    public GameModeType getType() {
        return GameModeType.ZOMBIES;
    }

    public WallWeaponFactory getWallWeaponFactory() {
        return wallWeaponFactory;
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

    public <T extends ArenaComponent> Collection<T> getComponents(Class<T> componentClass) {
        List<T> list = new ArrayList<>();

        for (ArenaComponent component : getComponents()) {
            if (component.getClass().isAssignableFrom(componentClass)) {
                list.add((T) component);
            }
        }

        return Collections.unmodifiableList(list);
    }

    public BattleEntity[] getNearbyEnemies(Location location, Team team, double range) {
        List<BattleEntity> list = new ArrayList<>();

        for (Entity entity : location.getWorld().getNearbyEntities(location, range, range, range)) {
            Mob mob = game.getMobManager().findMob(entity);
            if (mob != null && !mob.getBukkitEntity().isDead()) {
                list.add(mob);
            }
        }

        return list.toArray(new BattleEntity[list.size()]);
    }

    public BattleEntity[] getNearbyEntities(Location location, double range) {
        List<BattleEntity> list = new ArrayList<>();

        for (Entity entity : location.getWorld().getNearbyEntities(location, range, range, range)) {
            Mob mob = game.getMobManager().findMob(entity);
            if (mob != null && !mob.getBukkitEntity().isDead()) {
                list.add(mob);
            }

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
        return gamePlayer.getLocation();
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

    public boolean hasBloodEffectDisplay(BattleEntityType entityType) {
        return plugin.getBattlegroundsConfig().getDisplayBloodEffect(entityType.toString());
    }

    public void loadData(Arena arena) {
        CacheYaml data = game.getDataFile();
        ItemFinder itemFinder = new ItemFinder(plugin);
        PerkFactory perkFactory = new PerkFactory(translator);

        ZombiesDataLoader dataLoader = new ZombiesDataLoader(
                this,
                data,
                arena,
                itemFinder,
                barricadeFactory,
                doorFactory,
                mobSpawnFactory,
                mysteryBoxFactory,
                perkFactory,
                perkMachineFactory,
                sectionFactory,
                wallWeaponFactory
        );

        dataLoader.load();
    }

    public void preparePlayer(GamePlayer gamePlayer) {
        super.preparePlayer(gamePlayer);

        Map<String, String> defaultLoadout = config.getDefaultLoadout();

        Firearm primary = defaultLoadout.get("primary") != null ? plugin.getFirearmFactory().make(defaultLoadout.get("primary")) : null;
        Equipment equipment = defaultLoadout.get("equipment") != null ? plugin.getEquipmentFactory().make(defaultLoadout.get("equipment")) : null;
        MeleeWeapon meleeWeapon = defaultLoadout.get("melee-weapon") != null ? plugin.getMeleeWeaponFactory().make(defaultLoadout.get("melee-weapon")) : null;

        if (primary != null) {
            primary.setAmmo(config.getDefaultPrimaryMagazines() * primary.getMagazineSize());
        }

        if (equipment != null) {
            equipment.setAmount(config.getDefaultEquipmentAmount());
        }

        ItemStack barricadeTool = new ItemStackBuilder(XMaterial.OAK_FENCE.parseMaterial())
                .setDisplayName(ChatColor.WHITE + translator.translate(TranslationKey.BARRICADE_TOOL.getPath()))
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
                gamePlayer,
                false
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

        for (Section section : sectionContainer.getAll()) {
            if (section.getComponent(id) != null) {
                section.removeComponent(component);
                return true;
            }
        }

        return false;
    }

    public void removeMob(Mob mob) {
        // Remove the mob from the mob manager
        game.getMobManager().getMobs().remove(mob);
        // Remove references of the mob in arena components
        for (Section section : sectionContainer.getAll()) {
            for (Barricade barricade : section.getBarricadeContainer().getAll()) {
                barricade.getMobs().remove(mob);
            }
        }
        // Remove the mob itself
        mob.remove();
    }

    public void removeMobs() {
        // Remove all instances of mob entities
        for (Entity entity : game.getArena().getWorld().getEntities()) {
            Mob mob = game.getMobManager().findMob(entity);
            if (mob != null) {
                removeMob(mob);
            }
        }
    }

    public void removePlayer(GamePlayer gamePlayer) {
        team.removePlayer(gamePlayer);
    }

    public void respawnPlayer(GamePlayer gamePlayer) {
        if (gamePlayer.getState() != PlayerState.ACTIVE) {
            return;
        }
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

        MysteryBox mysteryBox = getRandomMysteryBox();

        if (mysteryBox != null) {
            mysteryBox.setState(new MovingState(game, null, internals, taskRunner, translator));
        }

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            Title title = EnumTitle.ZOMBIES_START.getTitle();
            title.send(gamePlayer.getPlayer());
        }

        BattleSound.THUNDER.play(game);
    }

    public Countdown startCountdown() {
        // Skip the countdown and start the game
        game.startGame();
        // Skip one game state and go straight to the ingame state
        game.setState(game.getState().next());
        // Return a nulled countdown
        return new NulledCountdown();
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

        taskRunner.runTaskTimer(new WaveSpawningThread(game, wave, maxMobs), waveDelay, period);
    }

    public void stop() {
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            Player player = gamePlayer.getPlayer();

            EnumTitle.ZOMBIES_GAME_OVER.send(player, new Placeholder("bg_round", wave.getRound()));
        }

        if (wave != null && wave.isRunning()) {
            wave.setRunning(false);
        }

        perkManager.clear();

        team = new BattleTeam(1, "Players", null, ChatColor.WHITE);

        taskRunner.runTaskLater(() -> {
            removeMobs();
        }, 200);
    }

    public void tick() {
        for (Objective objective : objectives) {
            if (objective.isAchieved()) {
                plugin.getEventDispatcher().dispatchExternalEvent(new GameEndEvent(game, objective, team, teams));
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

    private MysteryBox getRandomMysteryBox() {
        List<MysteryBox> mysteryBoxes = new ArrayList<>();
        Random random = new Random();

        for (Section section : sectionContainer.getAll()) {
            mysteryBoxes.addAll(section.getMysteryBoxContainer().getAll());
        }

        if (mysteryBoxes.size() <= 0) {
            return null;
        }

        return mysteryBoxes.get(random.nextInt(mysteryBoxes.size()));
    }

    private void registerComponentFactories() {
        ItemFinder itemFinder = new ItemFinder(plugin);

        barricadeFactory = new BarricadeFactory(internals, translator);
        doorFactory = new DoorFactory(game, internals, translator);
        mobSpawnFactory = new MobSpawnFactory();
        mysteryBoxFactory = new MysteryBoxFactory(internals, itemFinder, translator, config);
        perkMachineFactory = new PerkMachineFactory(game, internals, perkManager, translator, viewFactory, config);
        sectionFactory = new SectionFactory();
        wallWeaponFactory = new WallWeaponFactory(game, internals, translator, viewFactory);
    }
}
