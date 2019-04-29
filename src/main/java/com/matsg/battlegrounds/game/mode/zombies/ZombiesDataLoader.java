package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.item.factory.PerkFactory;
import com.matsg.battlegrounds.item.perk.PerkEffectType;
import com.matsg.battlegrounds.util.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

public class ZombiesDataLoader {

    private Arena arena;
    private Game game;
    private ItemFinder itemFinder;
    private PerkFactory perkFactory;
    private Zombies zombies;

    public ZombiesDataLoader(Zombies zombies, Game game, Arena arena, ItemFinder itemFinder) {
        this.zombies = zombies;
        this.game = game;
        this.arena = arena;
        this.itemFinder = itemFinder;
        this.perkFactory = new PerkFactory();
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

                Section section = new ZombiesSection(Integer.parseInt(componentId), name);
                section.setPrice(price);

                zombies.getSectionContainer().add(section);
            }
        }

        // Load other zombie component
        for (String componentId : configurationSection.getKeys(false)) {
            String type = configurationSection.getString(componentId + ".type");

            if (type.equals("door")) {
                String locationPath = "arena." + arena.getName() + ".component." + componentId;
                Location max = data.getLocation(locationPath + ".max");
                Location min = data.getLocation(locationPath + ".min");
                Material material = Material.valueOf(configurationSection.getString(componentId + ".material"));
                Section section = zombies.getSection(configurationSection.getString(componentId + ".section"));

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
                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Chest chest = (Chest) location.getBlock().getState();
                int price = configurationSection.getInt(componentId + ".price");
                Section section = zombies.getSection(configurationSection.getString(componentId + ".section"));
                Weapon weapon = itemFinder.findWeapon(configurationSection.getString(componentId + ".item"));

                ItemChest itemChest = new ZombiesItemChest(
                        Integer.parseInt(componentId),
                        chest,
                        weapon,
                        weapon.getName(),
                        weapon.getItemStack(),
                        price
                );

                section.getItemChestContainer().add(itemChest);
            }

            if (type.equals("mobspawn")) {
                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Section section = zombies.getSection(configurationSection.getString(componentId + ".section"));

                MobSpawn mobSpawn = new ZombiesMobSpawn(Integer.parseInt(componentId), location);

                section.getMobSpawnContainer().add(mobSpawn);
            }

            if (type.equals("mysterybox")) {
                Block left = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".leftside").getBlock();
                Block right = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".rightside").getBlock();
                int price = configurationSection.getInt(componentId + ".price");
                Section section = zombies.getSection(configurationSection.getString(componentId + ".section"));

                MysteryBox mysteryBox = new ZombiesMysteryBox(
                        Integer.parseInt(componentId),
                        game,
                        price,
                        new Pair<>(left, right)
                );

                section.getMysteryBoxContainer().add(mysteryBox);
            }

            if (type.equals("perkmachine")) {
                Location location = data.getLocation("arena." + arena.getName() + ".component." + componentId + ".location");
                Sign sign = (Sign) location.getBlock().getState();
                int maxBuys = configurationSection.getInt(componentId + ".maxbuys");
                int price = configurationSection.getInt(componentId + ".price");
                PerkEffectType perkEffectType = PerkEffectType.valueOf(configurationSection.getString(componentId + ".effect"));
                Section section = zombies.getSection(configurationSection.getString(componentId + ".section"));

                PerkMachine perkMachine = new ZombiesPerkMachine(
                        Integer.parseInt(componentId),
                        game,
                        sign,
                        perkFactory.make(perkEffectType),
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
