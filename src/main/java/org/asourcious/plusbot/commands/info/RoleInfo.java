package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.utils.FormatUtil;

import java.awt.Color;

public class RoleInfo extends Command {
    public RoleInfo(PlusBot plusBot) {
        super(plusBot);
        this.name = "RoleInfo";
        this.help = "Gives information about the specified role";
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (message.getMentionedRoles().isEmpty() && stripped.isEmpty())
            return "You must either mention a role or provide a role name!";
        if (!message.getMentionedRoles().isEmpty() && !stripped.isEmpty() || message.getMentionedRoles().size() > 1)
            return "You can only specify one role!";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        if (guild.getRolesByName(stripped, true).size() > 1 && message.getMentionedRoles().isEmpty()) {
            channel.sendMessage("Multiple roles exist with that name! Please mention the role instead.").queue();
            return;
        }

        Role target = message.getMentionedRoles().isEmpty()
                ? guild.getRolesByName(stripped, true).get(0)
                : message.getMentionedRoles().get(0);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(Color.GREEN)
                .setThumbnail(guild.getIconUrl())
                .addField("Name", target.getName(), true)
                .addField("ID", target.getId(), true)
                .addField("Position", String.valueOf(target.getPosition()), true)
                .addField("Creation Time", FormatUtil.getFormattedTime(target.getCreationTime()), true);

        channel.sendMessage(new MessageBuilder().setEmbed(embedBuilder.build()).build()).queue();
    }
}