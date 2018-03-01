package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;

public abstract class SubCommand implements CommandBase {

    protected Battlegrounds plugin;
    protected boolean playerOnly;
    protected String description, name, permissionNode, usage;
    protected String[] aliases;

    public SubCommand(Battlegrounds plugin, String name, String description, String usage, String permissionNode, boolean playerOnly, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permissionNode = permissionNode;
        this.playerOnly = playerOnly;
        this.plugin = plugin;
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }
}