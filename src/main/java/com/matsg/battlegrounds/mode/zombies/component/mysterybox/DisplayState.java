package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.Hologram;
import org.bukkit.entity.Item;

public class DisplayState implements MysteryBoxState {

    private static final long WEAPON_DISPLAY_DELAY = 0;
    private static final long WEAPON_DISPLAY_DURATION = 2; // The hologram changes every 2 ticks or 0.1 seconds

    private Game game;
    private GamePlayer gamePlayer;
    private Hologram hologram;
    private Item item;
    private MysteryBox mysteryBox;
    private String weaponName;

    public DisplayState(Game game, MysteryBox mysteryBox, GamePlayer gamePlayer, Hologram hologram, Item item, String weaponName) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.gamePlayer = gamePlayer;
        this.hologram = hologram;
        this.item = item;
        this.weaponName = weaponName;
    }

    public void handleInteraction(GamePlayer gamePlayer) {
        new BattleRunnable() {
            int time = 99;

            public void run() {
                if (DisplayState.this.gamePlayer != gamePlayer) {
                    hologram.remove();
                    item.remove();
                    mysteryBox.playChestAnimation(false);
                    cancel();
                    return;
                }

                hologram.setText((double) time / 10 + "s", weaponName);
                hologram.update();
            }
        }.runTaskTimer(WEAPON_DISPLAY_DELAY, WEAPON_DISPLAY_DURATION);
    }
}
