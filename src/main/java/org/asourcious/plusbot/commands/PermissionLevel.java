package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import org.asourcious.plusbot.Constants;

public enum PermissionLevel {

    EVERYONE(0, "Everyone"),
    DJ(1, "DJ"),
    SERVER_MODERATOR(2, "Server Moderator"),
    SERVER_ADMIN(3, "Server Administrator"),
    SERVER_OWNER(4, "Server Owner"),
    OWNER(5, "Bot Owner");

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
        if (member.hasPermission(Permission.ADMINISTRATOR))
            return PermissionLevel.SERVER_ADMIN;
        if (member.hasPermission(Permission.MANAGE_SERVER))
            return PermissionLevel.SERVER_MODERATOR;
        if (member.getGuild().getRolesByName("DJ", false).size() == 1 && member.getRoles().contains(member.getGuild().getRolesByName("DJ", false).get(0)))
            return PermissionLevel.DJ;

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