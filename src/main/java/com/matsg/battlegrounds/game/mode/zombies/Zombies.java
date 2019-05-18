package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.game.BattleComponentContainer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.game.mode.AbstractGameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.objective.EliminationObjective;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.factory.LoadoutFactory;
import com.matsg.battlegrounds.nms.Title;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.MessageHelper;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Zombies extends AbstractGameMode {

    private ComponentContainer<Section> sectionContainer;
    private LoadoutFactory loadoutFactory;
    private MessageHelper messageHelper;
    private Team team;
    private ZombiesConfig config;

    public Zombies(Battlegrounds plugin, Game game, ZombiesConfig config) {
        super(plugin, game);
        this.config = config;
        this.loadoutFactory = new LoadoutFactory();
        this.messageHelper = new MessageHelper();
        this.sectionContainer = new BattleComponentContainer<>();
        this.team = new BattleTeam(1, "Players", null, ChatColor.WHITE);

        setName(messageHelper.create(TranslationKey.ZOMBIES_NAME));
        setShortName(messageHelper.create(TranslationKey.ZOMBIES_SHORT));

        objectives.add(new EliminationObjective(game, 1));

        teams.add(team);
    }

    public ZombiesConfig getConfig() {
        return config;
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
        ZombiesDataLoader dataLoader = new ZombiesDataLoader(this, game, arena, itemFinder);
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
                .setDisplayName(messageHelper.create(TranslationKey.BARRICADE_TOOL))
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

    public void spawnPlayers(GamePlayer... players) {
        for (GamePlayer gamePlayer : team.getPlayers()) {
            Spawn spawn = game.getArena().getRandomSpawn();
            spawn.setGamePlayer(gamePlayer);
            gamePlayer.getPlayer().teleport(spawn.getLocation());
        }
    }

    public void start() {
        super.start();

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            Title title = EnumTitle.ZOMBIES_START.getTitle();
            title.send(gamePlayer.getPlayer());
        }

        Section section = getFirstSection();
        section.setLocked(false);

        BattleSound.THUNDER.play(game);
    }

    public void startCountdown() {
        start();
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
        return null;
    }
}
