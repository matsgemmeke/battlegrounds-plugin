package com.matsg.battlegrounds.api.storage;

public interface LevelConfig extends Yaml {

    /**
     * Gets the amount of experience points needed to reach a specific level.
     *
     * @param level the level
     * @return the amount of experience points
     */
    int getExp(int level);

    /**
     * Gets the floating number for the experience bar which displays the progress to the next level.
     *
     * @param exp the amount of experience points
     * @return the floating number for the experience bar.
     */
    float getExpBar(int exp);

    /**
     * Gets the amount of experience points needed to reach a specific level from a specific amount of experience
     * points.
     *
     * @param level the level
     * @param exp the amount of experience points
     * @return the amount of experience points still needed to reach the level
     */
    int getExpNeeded(int level, int exp);

    /**
     * Gets the level that is reached with a specific amount of experience points.
     *
     * @param exp the amount of experience points
     * @return the level reached with the given experience points
     */
    int getLevel(int exp);

    /**
     * Gets the level of which a specific weapon will be unlocked.
     *
     * @param weaponId the id of the weapon
     * @return the level where the weapon is unlocked
     */
    int getLevelUnlocked(String weaponId);

    /**
     * Gets whether a specific weapon is unlockable by reaching a certain level.
     *
     * @param weaponId the id of the weapon
     * @return whether the weapon is unlockable
     */
    boolean isUnlockable(String weaponId);
}
