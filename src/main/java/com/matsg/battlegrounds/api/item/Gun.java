package com.matsg.battlegrounds.api.item;

import java.util.List;
import java.util.Set;

public interface Gun extends FireArm, DropListener {

    void addAttachments();

    Gun clone();

    List<Attachment> getAttachments();

    int getBurstRounds();

    Set<String> getCompatibleAttachments();

    int getFireRate();

    void setScoped(boolean scoped);
}