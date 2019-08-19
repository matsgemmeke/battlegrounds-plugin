package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.mode.zombies.component.*;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.mode.zombies.item.factory.PerkFactory;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import com.matsg.battlegrounds.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

public class ZombiesDataLoader {

    private Arena arena;
    private Game game;
    private ItemFinder itemFinder;
    private Logger logger;
    private PerkFactory perkFactory;
    private Translator translator;
    private Zombies zombies;

    public ZombiesDataLoader(Zombies zombies, Game game, Arena arena, ItemFinder itemFinder, PerkFactory perkFactory, Translator translator) {
        this.zombies = zombies;
        this.game = game;
        this.arena = arena;
        this.itemFinder = itemFinder;
        this.perkFactory = perkFactory;
        this.translator = translator;
        this.logger = Bukkit.getLogger();
    }

    public void load() {
        CacheYaml data = game.getDataFile();
        ConfigurationSection configurationSection = data.getConfigurationSection("arena." + arena.getName() + ".component");

        if (configurationSection == null) {
            return;
        }

        // Make sure the sections are loaded before the other components are being added.
        for (String componentId : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(componentId + ".type");

            if (type.equals("section")) {
                String name = data.getString("arena." + arena.getName() + ".component." + componentId + ".name");
                int price = configurationSection.getInt(componentId + ".price");
                boolean unlocked = configurationSection.getBoolean(componentId + ".unlocked");

                Section section = new ZombiesSection(Integer.parseInt(componentId), name, unlocked);
                section.setPrice(price);

                zombies.getSectionContainer().add(section);
            }
        }

        // Load other zombie component
        for (String componentId : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(componentId + ".type");

            if (type.equals("door")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add door with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                String locationPath = "arena." + arena.getName() + ".component." + componentId;
                Location max = data.getLocation(locationPath + ".max");
                Location min = data.getLocation(locationPath + ".min");
                Material material = Material.valueOf(configurationSection.getString(componentId + ".material"));

                ZombiesDoor door = new ZombiesDoor(
                        Integer.parseInt(componentId),
                        game,
                        section,
                        arena.getWorld(),
                        max,
                        min,
                        material
                );

                section.getDoorContainer().add(door);
            }

            if (type.equals("itemchest")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add itemchest with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Chest chest = (Chest) location.getBlock().getState();
                int price = configurationSection.getInt(componentId + ".price");

                Weapon weapon = itemFinder.findWeapon(configurationSection.getString(componentId + ".item"));
                weapon.update();

                ItemChest itemChest = new ZombiesItemChest(
                        Integer.parseInt(componentId),
                        game,
                        translator,
                        chest,
                        weapon,
                        weapon.getName(),
                        weapon.getItemStack(),
                        weapon.getType(),
                        price
                );

                section.getItemChestContainer().add(itemChest);
            }

            if (type.equals("mobspawn")) {
                String sectionName = configurationSection.getString(componentId + ".section");
                Section section = zombies.getSection(sectionName);

                if (section == null) {
                    logger.warning("Unable to add mob spawn with id " + componentId + ": Section " + sectionName + " does not exist");
                    continue;
                }

                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                MobSpawn mobSpawn = new ZombiesMobSpawn(Integer.parseInt(componentId), location);

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
                int price = configurationSection.getInt(componentId + ".price");

                MysteryBox mysteryBox = new ZombiesMysteryBox(
                        Integer.parseInt(componentId),
                        game,
                        price,
                        new Pair<>(left, right)
                );

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
                PerkEffectType perkEffectType = PerkEffectType.valueOf(configurationSection.getString(componentId + ".effect"));

                PerkMachine perkMachine = new ZombiesPerkMachine(
                        Integer.parseInt(componentId),
                        game,
                        sign,
                        perkFactory.make(perkEffectType),
                        zombies.getPerkManager(),
                        translator,
                        price,
                        maxBuys
                );
                perkMachine.setSignLayout(zombies.getConfig().getPerkSignLayout());
                perkMachine.updateSign();

                section.getPerkMachineContainer().add(perkMachine);
            }
        }
    }
}
