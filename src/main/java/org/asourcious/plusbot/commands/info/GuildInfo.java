package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.utils.FormatUtil;

import java.awt.Color;

public class GuildInfo extends NoArgumentCommand {

    public GuildInfo(PlusBot plusBot) {
        super(plusBot);
        this.name = "GuildInfo";
        this.help = "Gives information about the server";
        this.aliases = new String[] { "ServerInfo" };
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setColor(Color.GREEN)
                .setThumbnail(guild.getIconUrl())
                .addField("Name", guild.getName(), true)
                .addField("ID", guild.getId(), true)
                .addField("Owner", guild.getOwner().getEffectiveName(), true)
                .addField("Owner ID", guild.getOwner().getUser().getId(), true)
                .addField("Text Channels", String.valueOf(guild.getTextChannels().size()), true)
                .addField("Voice Channels", String.valueOf(guild.getVoiceChannels().size()), true)
                .addField("Roles", String.valueOf(guild.getRoles().size()), true)
                .addField("Emotes", String.valueOf(guild.getEmotes().size()), true)
                .addField("Region", guild.getRegion().toString(), true)
                .addField("Creation", FormatUtil.getFormattedTime(guild.getCreationTime()), true);

        channel.sendMessage(new MessageBuilder().setEmbed(embedBuilder.build()).build()).queue();
    }
}