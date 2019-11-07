package com.matsg.battlegrounds.mode.zombies.component.mysterybox;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBox;
import com.matsg.battlegrounds.mode.zombies.component.MysteryBoxState;

public class IdleState implements MysteryBoxState {

    private boolean inUse;
    private Game game;
    private InternalsProvider internals;
    private MysteryBox mysteryBox;
    private TaskRunner taskRunner;
    private Translator translator;

    public IdleState(Game game, MysteryBox mysteryBox, InternalsProvider internals, TaskRunner taskRunner, Translator translator) {
        this.game = game;
        this.mysteryBox = mysteryBox;
        this.internals = internals;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.inUse = false;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean handleInteraction(GamePlayer gamePlayer) {
        int points = mysteryBox.getPrice();

        String actionBar = translator.translate(TranslationKey.ACTIONBAR_POINTS_DECREASE.getPath(), new Placeholder("bg_points", points));
        internals.sendActionBar(gamePlayer.getPlayer(), actionBar);

        gamePlayer.setPoints(gamePlayer.getPoints() - mysteryBox.getPrice());
        game.updateScoreboard();

        mysteryBox.setRolls(mysteryBox.getRolls() + 1);
        mysteryBox.setState(new RollingState(game, mysteryBox, gamePlayer, internals, taskRunner, translator));
        return true;
    }

    public boolean handleLookInteraction(GamePlayer gamePlayer) {
        String actionBar = translator.translate(TranslationKey.ACTIONBAR_MYSTERY_BOX.getPath(), new Placeholder("bg_price", mysteryBox.getPrice()));
        internals.sendActionBar(gamePlayer.getPlayer(), actionBar);
        return true;
    }

    public void initState() {
        mysteryBox.setRolls(0);
    }

    public void remove() {
        mysteryBox.setActive(false);
    }
}
