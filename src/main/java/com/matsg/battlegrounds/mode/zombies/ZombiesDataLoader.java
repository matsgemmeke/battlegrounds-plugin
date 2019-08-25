package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.mode.zombies.component.*;
import com.matsg.battlegrounds.mode.zombies.component.factory.*;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.mode.zombies.item.factory.PerkFactory;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import com.matsg.battlegrounds.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;

import java.util.logging.Logger;

public class ZombiesDataLoader {

    private Arena arena;
    private BarricadeFactory barricadeFactory;
    private CacheYaml data;
    private DoorFactory doorFactory;
    private ItemFinder itemFinder;
    private Logger logger;
    private MobSpawnFactory mobSpawnFactory;
    private MysteryBoxFactory mysteryBoxFactory;
    private PerkFactory perkFactory;
    private PerkMachineFactory perkMachineFactory;
    private SectionFactory sectionFactory;
    private WallWeaponFactory wallWeaponFactory;
    private Zombies zombies;

    public ZombiesDataLoader(
            Zombies zombies,
            CacheYaml data,
            Arena arena,
            ItemFinder itemFinder,
            BarricadeFactory barricadeFactory,
            DoorFactory doorFactory,
            MobSpawnFactory mobSpawnFactory,
            MysteryBoxFactory mysteryBoxFactory,
            PerkFactory perkFactory,
            PerkMachineFactory perkMachineFactory,
            SectionFactory sectionFactory,
            WallWeaponFactory wallWeaponFactory
    ) {
        this.zombies = zombies;
        this.data = data;
        this.arena = arena;
        this.itemFinder = itemFinder;
        this.barricadeFactory = barricadeFactory;
        this.doorFactory = doorFactory;
        this.mobSpawnFactory = mobSpawnFactory;
        this.mysteryBoxFactory = mysteryBoxFactory;
        this.perkFactory = perkFactory;
        this.perkMachineFactory = perkMachineFactory;
        this.sectionFactory = sectionFactory;
        this.wallWeaponFactory = wallWeaponFactory;
        this.logger = Bukkit.getLogger();
    }

    public void load() {
        ConfigurationSection configurationSection = data.getConfigurationSection("arena." + arena.getName() + ".component");

        if (configurationSection == null) {
            return;
        }

        // Make sure the sections are loaded before the other components are being added.
        for (String component : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(component + ".type");

            int componentId = Integer.parseInt(component);

            if (type.equals("section")) {
                String name = data.getString("arena." + arena.getName() + ".component." + componentId + ".name");
                int price = configurationSection.getInt(componentId + ".price");
                boolean unlocked = configurationSection.getBoolean(componentId + ".unlocked");

                Section section = sectionFactory.make(componentId, name, price, unlocked);

                zombies.getSectionContainer().add(section);
            }
        }

        // Load other zombies components
        for (String component : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(component + ".type");

            int componentId = Integer.parseInt(component);

            if (type.equals("barricade")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add barricade with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                int mobSpawnId = configurationSection.getInt(componentId + ".mobspawn");
                String locationPath = "arena." + arena.getName() + ".component." + componentId;

                MobSpawn mobSpawn = section.getMobSpawnContainer().get(mobSpawnId);
                Location maximumPoint = data.getLocation(locationPath + ".max");
                Location minimumPoint = data.getLocation(locationPath + ".min");
                Material material = Material.valueOf(configurationSection.getString(componentId + ".material"));
                World world = maximumPoint.getWorld();

                Barricade barricade = barricadeFactory.make(componentId, mobSpawn, maximumPoint, minimumPoint, world, material);

                mobSpawn.setBarricade(barricade);

                section.getBarricadeContainer().add(barricade);
            }

            if (type.equals("door")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add door with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                String locationPath = "arena." + arena.getName() + ".component." + componentId;
                Location maximumPoint = data.getLocation(locationPath + ".max");
                Location minimumPoint = data.getLocation(locationPath + ".min");
                Material material = Material.valueOf(configurationSection.getString(componentId + ".material"));
                World world = arena.getWorld();

                Door door = doorFactory.make(componentId, section, world, maximumPoint, minimumPoint, material);

                section.getDoorContainer().add(door);
            }

            if (type.equals("mobspawn")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add mob spawn with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");

                MobSpawn mobSpawn = mobSpawnFactory.make(componentId, location);

                section.getMobSpawnContainer().add(mobSpawn);
            }

            if (type.equals("mysterybox")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add mystery box with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                Block left = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".leftside").getBlock();
                Block right = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".rightside").getBlock();
                Pair<Block, Block> blocks = new Pair<>(left, right);
                int price = configurationSection.getInt(componentId + ".price");

                MysteryBox mysteryBox = mysteryBoxFactory.make(componentId, blocks, price);

                section.getMysteryBoxContainer().add(mysteryBox);
            }

            if (type.equals("perkmachine")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add perk machine with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Sign sign = (Sign) location.getBlock().getState();
                int maxBuys = configurationSection.getInt(componentId + ".maxbuys");
                int price = configurationSection.getInt(componentId + ".price");
                Perk perk = perkFactory.make(PerkEffectType.valueOf(configurationSection.getString(componentId + ".effect")));

                PerkMachine perkMachine = perkMachineFactory.make(componentId, sign, perk, maxBuys, price);

                section.getPerkMachineContainer().add(perkMachine);
            }

            if (type.equals("wallweapon")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add wall weapon with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Weapon weapon = itemFinder.findWeapon(configurationSection.getString(componentId + ".weapon"));
                int price = configurationSection.getInt(componentId + ".price");

                double range = 1.0;
                ItemFrame itemFrame = null;

                for (Entity entity : location.getWorld().getNearbyEntities(location, range, range, range)) {
                    if (entity.getLocation().getBlock().equals(location.getBlock())) {
                        itemFrame = (ItemFrame) entity;
                    }
                }

                WallWeapon wallWeapon = wallWeaponFactory.make(componentId, itemFrame, weapon, price);

                section.getWallWeaponContainer().add(wallWeapon);
            }
        }
    }
}
