package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.utils.DiscordUtil;
import org.asourcious.plusbot.utils.FormatUtil;

import java.awt.Color;
import java.util.List;

public class UserInfo extends Command {
    public UserInfo(PlusBot plusBot) {
        super(plusBot);
        this.name = "UserInfo";
        this.help = "Gives information about you, a mentioned user, or the user whose id you provide (As long as they are in this server)";
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (DiscordUtil.getTrimmedMentions(message).size() > 1)
            return "You can only mention up to one user!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        List<User> users = DiscordUtil.getTrimmedMentions(message);

        Member target;
        if (users.isEmpty() && stripped.length() == 0) {
            target = guild.getMember(author);
        } else if (users.isEmpty()) {
            target = guild.getMemberById(stripped);
            if (target == null) {
                channel.sendMessage("No member with id \"" + stripped + "\" exists!").queue();
                return;
            }
        } else {
            target = guild.getMember(users.get(0));
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(Color.GREEN)
                .setThumbnail(target.getUser().getAvatarUrl())
                .addField("Name", target.getEffectiveName(), true)
                .addField("ID", target.getUser().getId(), true)
                .addField("Online Status", target.getOnlineStatus().toString(), true)
                .addField("Voice Status", target.getVoiceState().inVoiceChannel() ? "Connected" : "Disconnected", true)
                .addField("Game", target.getGame() != null ? target.getGame().getName() : "None", true)
                .addField("Account Type", target.getUser().isBot() ? "Bot" : "User", true)
                .addField("Creation Time", FormatUtil.getFormattedTime(target.getUser().getCreationTime()), true)
                .addField("Join Date", FormatUtil.getFormattedTime(target.getJoinDate()), true);

        channel.sendMessage(new MessageBuilder().setEmbed(embedBuilder.build()).build()).queue();
    }
}
