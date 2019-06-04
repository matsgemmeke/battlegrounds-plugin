package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.entity.MobType;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.game.BattleComponentContainer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.game.mode.AbstractGameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.mode.shared.SpawningBehavior;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import com.matsg.battlegrounds.item.factory.PerkFactory;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Zombies extends AbstractGameMode {

    private ComponentContainer<Section> sectionContainer;
    private LoadoutFactory loadoutFactory;
    private MobManager mobManager;
    private Team team;
    private Wave<? extends Mob> wave;
    private WaveFactory waveFactory;
    private ZombiesConfig config;

    public Zombies(Battlegrounds plugin, Game game, Translator translator, SpawningBehavior spawningBehavior, ZombiesConfig config) {
        super(plugin, game, translator, spawningBehavior);
        this.config = config;
        this.loadoutFactory = new LoadoutFactory();
        this.mobManager = new MobManager();
        this.name = translator.translate(TranslationKey.ZOMBIES_NAME);
        this.sectionContainer = new BattleComponentContainer<>();
        this.shortName = translator.translate(TranslationKey.ZOMBIES_SHORT);
        this.team = new BattleTeam(1, "Players", null, ChatColor.WHITE);
        this.waveFactory = new WaveFactory();

        teams.add(team);
    }

    public ZombiesConfig getConfig() {
        return config;
    }

    public MobManager getMobManager() {
        return mobManager;
    }

    public ComponentContainer<Section> getSectionContainer() {
        return sectionContainer;
    }

    public GameModeType getType() {
        return GameModeType.ZOMBIES;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        team.addPlayer(gamePlayer);
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
            Mob mob = mobManager.findMob(entity);
            if (mob != null) {
                list.add(mob);
            }
        }

        return list.toArray(new Mob[list.size()]);
    }

    public Spawn getRespawnPoint(GamePlayer gamePlayer) {
        return null;
    }

    public GameScoreboard getScoreboard() {
        return null;
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

    public void onDeath(GamePlayer gamePlayer, GamePlayerDeathEvent.DeathCause deathCause) {

    }

    public void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox) {

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
                .setDisplayName(translator.translate(TranslationKey.BARRICADE_TOOL))
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

        Section section = getFirstSection();

        if (section == null) {
            plugin.getLogger().severe("Unable to start gamemode Zombies in game " + game.getId());
            return;
        }

        section.setLocked(false);

        wave = waveFactory.make(game, MobType.ZOMBIE, 1, getMobAmount(1, team.getTeamSize()));
        wave.getMobSpawns().addAll(section.getMobSpawnContainer().getAll());
        wave.setRunning(true);

        new WaveSpawningThread(this, wave, config.getMaxMobs(team.getTeamSize())).runTaskTimer(0, 100);

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

    public void stop() {

    }

    public void tick() {

    }

    private Section getFirstSection() {
        for (Section section : sectionContainer.getAll()) {
            if (section.isUnlockedByDefault()) {
                return section;
            }
        }
        return sectionContainer.getAll().iterator().next();
    }

    private int getMobAmount(int round, int players) {
        int zombies = (int) ((Math.round(0.000058 * Math.pow(round, 3) + 0.074032 * Math.pow(round, 2) + 0.718119 * round + 4.738699)) * config.getMobMultiplier(players));
        zombies += Math.round((double) zombies / 100 * (new Random().nextInt(config.getVariation() * 2) - config.getVariation()));
        return zombies;
    }
}
