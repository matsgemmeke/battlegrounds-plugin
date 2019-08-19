package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.util.*;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;

public class ZombiesMysteryBox implements MysteryBox {

    private boolean active;
    private boolean locked;
    private byte direction;
    private Game game;
    private Hologram hologram;
    private int id;
    private int price;
    private int rolls;
    private Item item;
    private MysteryBoxState state;
    private Pair<Block, Block> blocks;
    private Weapon currentWeapon;
    private Weapon[] weapons;
    private World world;

    public ZombiesMysteryBox(int id, Game game, Pair<Block, Block> blocks, World world, Weapon[] weapons, int price) {
        this.id = id;
        this.game = game;
        this.blocks = blocks;
        this.world = world;
        this.weapons = weapons;
        this.price = price;
        this.active = false;
        this.rolls = 0;
        this.locked = true;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public int getId() {
        return id;
    }

    public Block getLeftSide() {
        return blocks.left();
    }

    public Block getRightSide() {
        return blocks.right();
    }

    public int getRolls() {
        return rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = rolls;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public MysteryBoxState getState() {
        return state;
    }

    public void setState(MysteryBoxState state) {
        this.state = state;
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean onInteract(GamePlayer gamePlayer, Block block) {
        // If the mystery box is locked or is not active, it does not accept interactions.
        if (locked || !active) {
            return false;
        }

        // If the player does not have enough points they can not open the item chest.
        if (gamePlayer.getPoints() < price) {
            ActionBar.UNSUFFICIENT_POINTS.send(gamePlayer.getPlayer());
            return true;
        }

        state.handleInteraction(gamePlayer);
        return true;
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        // If the mystery box is locked or is not active, it does not accept look interactions.
        if (locked || !active) {
            return false;
        }

        return true;
    }

    public void playChestAnimation(boolean open) {

    }

    public void setActive(boolean active) {
        this.active = active;

        XMaterial material = active ? XMaterial.CHEST : XMaterial.END_PORTAL_FRAME;

        for (Block block : getBlocks()) {
            block.setType(material.parseMaterial());
        }
    }

    private Block[] getBlocks() {
        return new Block[] { blocks.left(), blocks.right() };
    }
}
