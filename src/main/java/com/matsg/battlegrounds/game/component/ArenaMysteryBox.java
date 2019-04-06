package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.MysteryBox;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.util.Pair;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;

public class ArenaMysteryBox implements MysteryBox {

    private boolean active, locked;
    private byte direction;
    private Game game;
    private GamePlayer currentUser;
    private int hits, id;
    private Item item;
    private Pair<Block, Block> blocks;
    private Weapon[] weapons;

    public ArenaMysteryBox(int id, Game game, Pair<Block, Block> blocks) {
        this.id = id;
        this.game = game;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean onInteract(GamePlayer gamePlayer, Block block) {
        return false;
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        return false;
    }

    private void startWeaponRotation(GamePlayer gamePlayer) {
        currentUser = gamePlayer;
        hits ++;
    }
}
