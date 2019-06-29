package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.util.Hologram;
import com.matsg.battlegrounds.util.Pair;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;

public class ZombiesMysteryBox implements MysteryBox {

    private boolean active, locked;
    private byte direction;
    private Game game;
    private GamePlayer currentUser;
    private Hologram hologram;
    private int hits, id, price;
    private Item item;
    private Pair<Block, Block> blocks;
    private Weapon[] weapons;

    public ZombiesMysteryBox(int id, Game game, int price, Pair<Block, Block> blocks) {
        this.id = id;
        this.game = game;
        this.price = price;
        this.blocks = blocks;
        this.active = false;
        this.hits = 0;
        this.locked = true;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

        return false;
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        // If the mystery box is locked or is not active, it does not accept look interactions.
        if (locked || !active) {
            return false;
        }

        return true;
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

    private void startWeaponRotation(GamePlayer gamePlayer) {
        currentUser = gamePlayer;
        hits ++;
    }
}
