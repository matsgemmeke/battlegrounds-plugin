package com.matsg.battlegrounds.api.item;

import java.util.List;

public interface Gun extends FireArm {

    List<Attachment> getAttachments();

    int getBurstRounds();

    int getFireRate();

    WeaponType getType();

    void setScoped(boolean scoped);
}