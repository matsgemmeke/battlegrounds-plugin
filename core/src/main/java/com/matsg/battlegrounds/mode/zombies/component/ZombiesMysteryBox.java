package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.util.*;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class ZombiesMysteryBox implements MysteryBox {

    private boolean active;
    private boolean locked;
    private byte direction;
    private int id;
    private int price;
    private int rolls;
    private InternalsProvider internals;
    private MysteryBoxState state;
    private Pair<Block, Block> blocks;
    private Translator translator;
    private Weapon currentWeapon;
    private Weapon[] weapons;

    public ZombiesMysteryBox(
            int id,
            Pair<Block, Block> blocks,
            Weapon[] weapons,
            int price,
            InternalsProvider internals,
            Translator translator
    ) {
        this.id = id;
        this.blocks = blocks;
        this.weapons = weapons;
        this.price = price;
        this.internals = internals;
        this.translator = translator;
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
        state.initState();
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public void setWeapons(Weapon[] weapons) {
        this.weapons = weapons;
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

    public Location getItemDropLocation() {
        Block[] blocks = getBlocks();

        return new Location(
                blocks[0].getWorld(),
                ((blocks[0].getX() + 0.5) + (blocks[1].getX() + 0.5)) / 2.0,
                blocks[0].getY() + 1.0,
                ((blocks[0].getZ() + 0.5) + (blocks[1].getZ() + 0.5)) / 2.0
        );
    }

    public boolean onInteract(GamePlayer gamePlayer, Block block) {
        // If the mystery box is locked or is not active, it does not accept interactions.
        if (locked || !active) {
            return false;
        }

        // If the mystery box is in use, make the state handle the interaction.
        if (state.isInUse()) {
            return state.handleInteraction(gamePlayer);
        }

        // If the player does not have enough points they can not open the item chest.
        if (gamePlayer.getPoints() < price) {
            String actionBar = translator.translate(TranslationKey.ACTIONBAR_UNSUFFICIENT_POINTS.getPath());
            internals.sendActionBar(gamePlayer.getPlayer(), actionBar);
            return true;
        }

        return state.handleInteraction(gamePlayer);
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        // If the mystery box is locked or is not active, it does not accept look interactions.
        if (locked || !active) {
            return false;
        }

        return state.handleLookInteraction(gamePlayer);
    }

    public void playChestAnimation(boolean open) {
        internals.playChestAnimation(getBlocks()[0].getLocation(), open);
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
