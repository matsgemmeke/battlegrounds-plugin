package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeaponTypeView implements View {

    private Inventory inventory;
    private WeaponsView weaponsView;

    public WeaponTypeView(Plugin plugin, String title, List<Weapon> weapons) {
        this.inventory = plugin.getServer().createInventory(this, 27, title);

        Collections.sort(weapons, new Comparator<Weapon>() {
            public int compare(Weapon o1, Weapon o2){
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Weapon weapon : weapons) {
            weapon.update();
            addWeapon(weapon);
        }

        inventory.setItem(26, new ItemStackBuilder(new ItemStack(Material.BARRIER, 1)).setDisplayName("§fGo back").build());
    }

    public WeaponTypeView(Plugin plugin, String title, List<Weapon> weapons, WeaponsView weaponsView) {
        this(plugin, title, weapons);
        this.weaponsView = weaponsView;
    }

    private void addWeapon(Weapon weapon) {
        inventory.addItem(new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName("§f" + weapon.getName())
                .setLore(getLore(weapon))
                .setUnbreakable(true)
                .build());
    }

    private String[] getLore(Weapon weapon) {
        List<String> lore = new ArrayList<>(weapon.getItemStack().getItemMeta().getLore());
        lore.add(" ");
        int maxLength = 30;
        Pattern pattern = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(weapon.getDescription());
        while (matcher.find()) {
            lore.add("§f" + matcher.group(1));
        }
        return lore.toArray(new String[lore.size()]);
    }

    public void onClick(Player player, ItemStack itemStack, ClickType clickType) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (itemStack.getType() == Material.BARRIER) {
            player.openInventory(weaponsView.getInventory());
        }
    }

    public boolean onClose() {
        return true;
    }

    public Inventory getInventory() {
        return inventory;
    }
}