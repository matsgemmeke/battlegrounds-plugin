package com.matsg.battlegrounds.util;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private boolean gravity;
    private double spacing;
    private List<ArmorStand> list;
    private Location location;
    private String[] text;

    public Hologram(Location location, String... text) {
        this.gravity = false;
        this.location = location;
        this.text = text;
        this.list = new ArrayList<>();
        this.spacing = 0.25;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String... text) {
        this.text = text;
    }

    public boolean hasGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public void display() {
        for (String string : text) {
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setCanPickupItems(false);
            armorStand.setCustomName(string);
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(gravity);
            armorStand.setVisible(false);

            list.add(armorStand);

            location.subtract(0, spacing, 0); // Make space for a new text line
        }
    }

    public void moveTo(Location location) {
        for (ArmorStand armorStand : list) {
            armorStand.teleport(location.subtract(0, spacing, 0));
        }
    }

    public void remove() {
        for (ArmorStand armorStand : list) {
            armorStand.remove();
        }
    }

    public void update() {
        if (list.isEmpty()) {
            display();
        }
        for (ArmorStand armorStand : list) {
            armorStand.setCustomName(text[list.indexOf(armorStand)]);
            armorStand.setGravity(gravity);
        }
    }
}