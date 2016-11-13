package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.utils.FormatUtil;

public class ChannelInfo extends Command {

    public ChannelInfo(PlusBot plusBot) {
        super(plusBot);
        this.name = "ChannelInfo";
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

        String msg = "";
        msg += "Name: **" + target.getName() + "**\n";
        msg += "ID: **" + target.getId() + "**\n";
        msg += "Topic: **" + (target.getTopic() == null ? target.getTopic() :"None") + "**\n";
        msg += "Position: **" + target.getPosition() + "**\n";
        msg += "Creation Time: **" + FormatUtil.getFormattedTime(target.getCreationTime()) + "**";

        channel.sendMessage(msg).queue();
    }
}