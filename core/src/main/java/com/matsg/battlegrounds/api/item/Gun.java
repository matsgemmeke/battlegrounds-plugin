package com.matsg.battlegrounds.api.item;

import java.util.List;
import java.util.Set;

public interface Gun extends Firearm {

    List<Attachment> getAttachments();

    int getBurstRounds();

    Set<String> getCompatibleAttachments();

    int getFireRate();

    void addAttachments();

    Gun clone();

    void setScoped(boolean scoped);
}
