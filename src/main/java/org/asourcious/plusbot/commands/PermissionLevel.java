package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.asourcious.plusbot.Constants;

public enum PermissionLevel {

    EVERYONE(0),
    SERVER_MODERATOR(1),
    SERVER_OWNER(2),
    OWNER(3);

    private final int value;

    PermissionLevel(int value) {
        this.value = value;
    }

    public static PermissionLevel getPermissionLevel(Member member) {
        if (member.getUser().getId().equals(Constants.OWNER_ID))
            return PermissionLevel.OWNER;
        if (member.getGuild().getOwner().equals(member))
            return PermissionLevel.SERVER_OWNER;
        if (PermissionUtil.checkPermission(member.getGuild(), member, Permission.MANAGE_SERVER))
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
        switch (this) {
            case EVERYONE:
                return "Everyone";
            case SERVER_MODERATOR:
                return "Moderator";
            case SERVER_OWNER:
                return "Owner";
            case OWNER:
                return "Bot Owner";
            default:
                return "UNKNOWN";
        }
    }
}