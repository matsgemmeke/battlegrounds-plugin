package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.util.BattleRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovingState implements MysteryBoxState {

    private static final long BOX_MOVE_DELAY = 400;

    private Game game;
    private MysteryBox currentBox;

    public MovingState(Game game, MysteryBox currentBox) {
        this.game = game;
        this.currentBox = currentBox;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        return false;
    }

    public void initState() {
        currentBox.setActive(false);
        currentBox.setCurrentWeapon(null);
        currentBox.setRolls(0);
        currentBox.setState(null);

        new BattleRunnable() {
            public void run() {
                MysteryBox newBox;
                Random random = new Random();
                Zombies zombies = game.getGameMode(Zombies.class);

                // Create a list of the mystery box collection so the elements can be indexed
                List<MysteryBox> list = new ArrayList<>(zombies.getComponents(MysteryBox.class));

                do {
                    newBox = list.get(random.nextInt(list.size()));
                } while (newBox == currentBox);

                newBox.getLeftSide().getWorld().strikeLightningEffect(newBox.getLeftSide().getLocation());
                newBox.setActive(true);
                newBox.setState(new IdleState(game, newBox));
            }
        }.runTaskLater(BOX_MOVE_DELAY);
    }
}
