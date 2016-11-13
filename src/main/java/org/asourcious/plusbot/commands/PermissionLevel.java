package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import org.asourcious.plusbot.Constants;

public enum PermissionLevel {

    EVERYONE(0, "Everyone"),
    SERVER_MODERATOR(1, "Server Moderator"),
    SERVER_OWNER(2, "Server Owner"),
    OWNER(3, "Bot Owner");

    private final int value;
    private final String name;

    PermissionLevel(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static PermissionLevel getPermissionLevel(Member member) {
        if (member.getUser().getId().equals(Constants.OWNER_ID))
            return PermissionLevel.OWNER;
        if (member.isOwner())
            return PermissionLevel.SERVER_OWNER;
        if (member.hasPermission(Permission.MANAGE_SERVER))
            return PermissionLevel.SERVER_MODERATOR;

        return PermissionLevel.EVERYONE;
    }

    public static boolean canInteract(Member issuer, Member target) {
        return getPermissionLevel(issuer).value >= getPermissionLevel(target).value;
    }

    public static boolean hasPermission(Member member, PermissionLevel level) {
        return getPermissionLevel(member).value >= level.value;
    }

    @Override
    public String toString() {
        return name;
    }
}