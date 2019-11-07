package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Selection;
import com.matsg.battlegrounds.api.SelectionManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BattleSelectionManager implements SelectionManager {

    private Map<Player, Selection> selections;

    public BattleSelectionManager() {
        this.selections = new HashMap<>();
    }

    public Selection getSelection(Player player) {
        if (!selections.containsKey(player)) {
            return null;
        }
        return selections.get(player);
    }

    public void setSelection(Player player, Selection selection) {
        if (!selections.containsKey(player)) {
            selections.put(player, selection);
        } else {
            selections.replace(player, selection);
        }
    }
}
