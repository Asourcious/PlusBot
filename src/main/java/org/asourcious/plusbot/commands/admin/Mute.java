package org.asourcious.plusbot.commands.admin;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.DiscordUtil;

import java.awt.Color;
import java.time.ZonedDateTime;
import java.util.List;

public class Mute extends Command {

    public Mute(PlusBot plusBot) {
        super(plusBot);
        this.help = "Mutes the mentioned users for the specified number of minutes, or indefinitely if no number is supplied.";
        this.children = new Command[] {
                new Setup(plusBot),
                new Clear(plusBot)
        };
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (stripped.isEmpty())
            return null;
        if (!NumberUtils.isParsable(stripped))
            return "\"" + stripped + "\" is not a valid number!";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        ZonedDateTime time = null;

        if (!stripped.isEmpty()) {
            time = ZonedDateTime.now().plusMinutes(Long.parseLong(stripped));
        }

        List<Role> roles = guild.getRolesByName("Muted", true);
        List<User> mentions = DiscordUtil.getTrimmedMentions(message);

        if (roles.isEmpty()) {
            channel.sendMessage("No muted role found! Use Mute Setup or create a role with the name \"Muted\"!").queue();
            return;
        }
        if (roles.size() > 1) {
            channel.sendMessage("Multiple roles found with name \"Muted\". Delete some and try again!").queue();
            return;
        }

        if (mentions.isEmpty()) {
            channel.sendMessage("No users were mentioned!").queue();
            return;
        }

        Role muted = roles.get(0);
        GuildController controller = guild.getController();

        for (User user : mentions) {
            controller.modifyMemberRoles(guild.getMember(user), muted).queue();

            if (time != null && !settings.getMutes().has(guild.getId(), user.getId()))
                settings.getMutes().add(guild.getId(), new Pair<>(user.getId(), time));
        }
        channel.sendMessage("Updated mutes").queue();
    }

    private class Setup extends NoArgumentCommand {
        Setup(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            guild.getController().createRole().queue(role -> role.getManagerUpdatable()
                    .getNameField().setValue("Muted")
                    .getColorField().setValue(Color.GRAY)
                    .getPermissionField().setValue(1024L)
                    .update().queue());
        }
    }

    private class Clear extends NoArgumentCommand {
        Clear(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getMutes().clear(guild.getId());
            channel.sendMessage("Cleared mutes!").queue();
        }
    }
}
