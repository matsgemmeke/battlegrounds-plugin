package com.matsg.battlegrounds.api.item;

public enum ReloadType {

    MAGAZINE(0) {
        public void reloadSingle(Firearm firearm, double reloadSpeed) {
            int magazineSpace = firearm.getMagazineSize() - firearm.getMagazine();
            if (magazineSpace > firearm.getAmmo()) { // In case the magazine cannot be filled completely use the remaining ammo
                firearm.setMagazine(firearm.getMagazine() + firearm.getAmmo());
                firearm.setAmmo(0);
            } else {
                firearm.setAmmo(firearm.getAmmo() - magazineSpace);
                firearm.setMagazine(firearm.getMagazineSize());
            }
            firearm.setReloading(false);
            firearm.update();
        }
    },
    SHELL(1) {
        public void reloadSingle(Firearm firearm, double reloadSpeed) {
            firearm.setAmmo(firearm.getAmmo() - 1);
            firearm.setMagazine(firearm.getMagazine() + 1);
            firearm.update();
            if (firearm.getAmmo() > 0 && firearm.getMagazine() < firearm.getMagazineSize()) { // Play the next reload sound prior to the actual reload
                firearm.playReloadSound();
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

    public abstract void reloadSingle(Firearm firearm, double reloadSpeed);
}