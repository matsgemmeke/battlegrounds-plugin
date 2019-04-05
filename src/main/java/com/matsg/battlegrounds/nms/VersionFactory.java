package com.matsg.battlegrounds.nms;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.nms.v1_12_R1.Version112R1;

public class VersionFactory {

    public Version make(EnumVersion version) {
        switch (version) {
            case V1_12_R1:
                return new Version112R1();
            default:
                throw new FactoryCreationException("Unsupported version enum");
        }
    }
}
