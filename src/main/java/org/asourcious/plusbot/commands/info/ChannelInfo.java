package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.util.FormatUtils;

import java.awt.Color;

public class ChannelInfo extends Command {

    public ChannelInfo(PlusBot plusBot) {
        super(plusBot);
        this.help = "Gives information about the current or specified channel";
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (message.getMentionedChannels().size() > 1)
            return "You may only mention up to one channel!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        TextChannel target;
        if (stripped.isEmpty() && message.getMentionedChannels().isEmpty()) {
            target = channel;
        } else if (stripped.isEmpty()) {
            target = message.getMentionedChannels().get(0);
        } else {
            target = guild.getTextChannelById(stripped);
            if (target == null) {
                channel.sendMessage("No channel exists with the provided id!").queue();
                return;
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(Color.GREEN)
                .setThumbnail(guild.getIconUrl())
                .addField("Name", target.getName(), true)
                .addField("ID", target.getId(), true)
                .addField("Topic", target.getTopic() == null ? target.getTopic() : "None", true)
                .addField("Position", String.valueOf(target.getPosition()), true)
                .addField("Creation Time", FormatUtils.getFormattedTime(target.getCreationTime()), false);

        channel.sendMessage(new MessageBuilder().setEmbed(embedBuilder.build()).build()).queue();
    }
}