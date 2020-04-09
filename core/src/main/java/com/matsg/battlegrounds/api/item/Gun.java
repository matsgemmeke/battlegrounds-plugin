package com.matsg.battlegrounds.api.item;

import java.util.List;
import java.util.Set;

public interface Gun extends Firearm {

    List<Attachment> getAttachments();

    int getBurstRounds();

    Set<String> getCompatibleAttachments();

    int getFireRate();

    boolean isScoped();

    void addAttachments();

    void setScoped(boolean scoped);
}
