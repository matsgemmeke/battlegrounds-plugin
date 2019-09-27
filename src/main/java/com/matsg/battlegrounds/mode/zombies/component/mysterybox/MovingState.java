package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovingState implements MysteryBoxState {

    private static final int BOX_MOVE_DISAPPEAR_DELAY = 5;
    private static final int BOX_MOVE_REAPPEAR_DELAY = 20;

    private boolean inUse;
    private Game game;
    private MysteryBox currentBox;
    private TaskRunner taskRunner;

    public MovingState(Game game, MysteryBox currentBox, TaskRunner taskRunner) {
        this.game = game;
        this.currentBox = currentBox;
        this.taskRunner = taskRunner;
        this.inUse = true;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        return true;
    }

    public boolean handleLookInteraction(GamePlayer gamePlayer) {
        return false;
    }

    public void initState() {
        long tick = 20;

        taskRunner.runTaskTimer(new BukkitRunnable() {
            int time = 0;

            public void run() {
                if (time == BOX_MOVE_DISAPPEAR_DELAY) {
                    if (currentBox != null) {
                        currentBox.getLeftSide().getWorld().strikeLightningEffect(currentBox.getItemDropLocation());
                        currentBox.setState(new InactiveState(currentBox));
                    }
                }

                if (time >= BOX_MOVE_REAPPEAR_DELAY) {
                    MysteryBox newBox;
                    Random random = new Random();
                    Zombies zombies = game.getGameMode(Zombies.class);

                    // Create a list of the mystery box collection so the elements can be indexed
                    List<MysteryBox> list = new ArrayList<>();

                    for (Section section : zombies.getSectionContainer().getAll()) {
                        list.addAll(section.getMysteryBoxContainer().getAll());
                    }

                    do {
                        newBox = list.get(random.nextInt(list.size()));
                    } while (newBox == currentBox);

                    newBox.getLeftSide().getWorld().strikeLightningEffect(newBox.getLeftSide().getLocation());
                    newBox.setActive(true);
                    newBox.setState(new IdleState(game, newBox, taskRunner));

                    cancel();
                    return;
                }

                time++;
            }
        }, 0, tick);
    }

    public void remove() { }
}
