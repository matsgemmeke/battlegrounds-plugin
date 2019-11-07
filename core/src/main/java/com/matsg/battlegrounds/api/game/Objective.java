package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.util.Title;

public interface Objective {

    /**
     * Gets the id of the objective.
     *
     * @return the objective id
     */
    String getId();

    /**
     * Gets the title of the objective.
     *
     * @return the objective title
     */
    Title getTitle();

    /**
     * Gets whether the objective is achieved.
     *
     * @return whether the objective is achieved
     */
    boolean isAchieved();
}
