package com.matsg.battlegrounds.gui.view;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.item.EquipmentType;
import com.matsg.battlegrounds.item.FirearmType;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class SelectWeaponTypeView implements View {

    public Gui gui;
    private List<Weapon> weapons;
    private Loadout loadout;
    private Translator translator;
    private ViewFactory viewFactory;
    private View previousView;

    public SelectWeaponTypeView setLoadout(Loadout loadout) {
        this.loadout = loadout;
        return this;
    }

    public SelectWeaponTypeView setPreviousView(View previousView) {
        this.previousView = previousView;
        return this;
    }

    public SelectWeaponTypeView setTranslator(Translator translator) {
        this.translator = translator;
        return this;
    }

    public SelectWeaponTypeView setViewFactory(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        return this;
    }

    public SelectWeaponTypeView setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
        return this;
    }

    public void backButtonClick(InventoryClickEvent event) {
        previousView.openInventory(event.getWhoClicked());
    }

    public void openInventory(HumanEntity entity) {
        gui.show(entity);
    }

    public void populateWeaponTypes(OutlinePane pane) {
        for (FirearmType firearmType : FirearmType.values()) {
            Weapon firearm = weapons.stream().filter(w -> w.getType() == firearmType).findFirst().orElse(null);
            if (firearm != null) {
                addWeaponItem(pane, firearm, firearmType);
            }
        }
        for (EquipmentType equipmentType : EquipmentType.values()) {
            Weapon equipment = weapons.stream().filter(w -> w.getType() == equipmentType).findFirst().orElse(null);
            if (equipment != null) {
                addWeaponItem(pane, equipment, equipmentType);
            }
        }

        Weapon meleeWeapon = weapons.stream().filter(w -> w instanceof MeleeWeapon).findFirst().orElse(null);
        if (meleeWeapon != null) {
            addWeaponItem(pane, meleeWeapon, meleeWeapon.getType());
        }
    }

    private void addWeaponItem(OutlinePane pane, Weapon weapon, ItemType itemType) {
        weapon.update();

        ItemStack itemStack = new ItemStackBuilder(weapon.getItemStack().clone())
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.WHITE + translator.translate(weapon.getType().getNameKey()))
                .setLore(new String[0])
                .setUnbreakable(true)
                .build();

        pane.addItem(new GuiItem(itemStack, event -> {
            List<Weapon> weaponList = weapons.stream().filter(w -> w.getType() == itemType).collect(Collectors.toList());

            View view = viewFactory.make(SelectWeaponView.class, instance -> {
                instance.setLoadout(loadout);
                instance.setLoadoutView(previousView);
                instance.setPlayerUUID(event.getWhoClicked().getUniqueId());
                instance.setPreviousView(this);
                instance.setWeapons(weaponList);
            });
            view.openInventory(event.getWhoClicked());
        }));
    }
}
