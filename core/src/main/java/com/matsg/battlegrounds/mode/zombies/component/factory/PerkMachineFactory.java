package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.gui.ViewFactory;
import com.matsg.battlegrounds.mode.zombies.PerkManager;
import com.matsg.battlegrounds.mode.zombies.ZombiesConfig;
import com.matsg.battlegrounds.mode.zombies.component.PerkMachine;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesPerkMachine;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import org.bukkit.block.Sign;

public class PerkMachineFactory {

    private Game game;
    private InternalsProvider internals;
    private PerkManager perkManager;
    private Translator translator;
    private ViewFactory viewFactory;
    private ZombiesConfig config;

    public PerkMachineFactory(Game game, InternalsProvider internals, PerkManager perkManager, Translator translator, ViewFactory viewFactory, ZombiesConfig config) {
        this.game = game;
        this.internals = internals;
        this.perkManager = perkManager;
        this.translator = translator;
        this.viewFactory = viewFactory;
        this.config = config;
    }

    /**
     * Creates a perk machine component based on the given input.
     *
     * @param id the component id
     * @param sign the sign of the perk machine
     * @param perk the perk the perk machine sells
     * @param maxBuys the maximum amount of buys per player
     * @param price the price of the perk machine
     * @return a perk machine implementation
     */
    public PerkMachine make(int id, Sign sign, Perk perk, int maxBuys, int price) {
        PerkMachine perkMachine = new ZombiesPerkMachine(id, game, sign, perk, perkManager, maxBuys, price, internals, translator, viewFactory);
        perkMachine.setSignLayout(config.getPerkSignLayout());
        perkMachine.updateSign();

        return perkMachine;
    }
}
