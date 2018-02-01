package com.matsg.battlegrounds.api.item;


import com.matsg.battlegrounds.api.util.Sound;

public enum ReloadType {

    MAGAZINE(0) {
        public void reloadSingle(FireArm fireArm) {
            int magazineSpace = fireArm.getMagazineSize() - fireArm.getMagazine();
            if (magazineSpace > fireArm.getAmmo()) { //In case the magazine cannot be filled completely use the remaining ammo
                fireArm.setMagazine(fireArm.getMagazine() + fireArm.getAmmo());
                fireArm.setAmmo(0);
            } else {
                fireArm.setAmmo(fireArm.getAmmo() - magazineSpace);
                fireArm.setMagazine(fireArm.getMagazineSize());
            }
            fireArm.setReloading(false);
            fireArm.update();
        }
    },
    SHELL(1) {
        public void reloadSingle(FireArm fireArm) {
            fireArm.setAmmo(fireArm.getAmmo() - 1);
            fireArm.setMagazine(fireArm.getMagazine() + 1);
            fireArm.update();
            if (fireArm.getMagazine() < fireArm.getMagazineSize()) { //Play the next reload sound prior to the actual reload
                for (Sound sound : fireArm.getReloadSound()) {
                    sound.play(fireArm.getGame(), fireArm.getGamePlayer().getPlayer());
                }
            }
        }
    };

    private int id;

    ReloadType(int id) {
        this.id = id;
    }

    public static ReloadType getById(int id) {
        for (ReloadType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    public abstract void reloadSingle(FireArm fireArm);
}