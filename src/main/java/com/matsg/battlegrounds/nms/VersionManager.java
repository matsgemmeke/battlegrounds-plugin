package com.matsg.battlegrounds.nms;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.nms.version.*;

import java.util.ArrayList;
import java.util.List;

public class VersionManager {

    private List<Version> versions;
    private Version version;

    public VersionManager() {
        this.versions = new ArrayList<>();

        versions.add(new Version1());
        versions.add(new Version2());
        versions.add(new Version3());

        updateVersion();
    }

    public Version getVersion() {
        return version;
    }

    private void updateVersion() {
        for (Version version : versions) {
            if (version.supports(ReflectionUtils.getVersion())) {
                this.version = version;
                break;
            }
        }
    }
}
