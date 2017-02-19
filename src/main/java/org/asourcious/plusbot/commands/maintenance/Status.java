package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.handle.ShardHandler;
import org.asourcious.plusbot.util.FormatUtils;
import org.asourcious.plusbot.util.SystemUtils;

import java.awt.Color;
import java.time.ZonedDateTime;

public class Status extends NoArgumentCommand {

    public Status(PlusBot civBot) {
        super(civBot);
        this.help = "Returns information about the status of " + Constants.NAME;
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        ShardHandler shardHandler = plusBot.getShardHandler();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
                .setColor(Color.CYAN)
                .addField("Name", Constants.NAME, true)
                .addField("Version", Constants.VERSION, true)
                .addField("Uptime", FormatUtils.getFormattedDuration(Statistics.startTime, ZonedDateTime.now()), true)
                .addField("Threads", String.valueOf(Thread.activeCount()), true)
                .addField("CPU Usage", SystemUtils.getCPUUsage() + "%", true)
                .addField("RAM Usage", SystemUtils.getUsedMemory() + "/" + SystemUtils.getTotalMemory() + "MB", true)
                .addField("Messages Received", String.valueOf(Statistics.numMessages), true)
                .addField("Commands Executed", String.valueOf(Statistics.numCommands), true)
                .addField("Audio Connections", String.valueOf(shardHandler.getNumberOfOpenAudioConnections()), true)
                .addField("Guilds", String.valueOf(shardHandler.getNumberOfGuilds()), true)
                .addField("Text Channels", String.valueOf(shardHandler.getNumberOfTextChannels()), true)
                .addField("Voice Channels", String.valueOf(shardHandler.getNumberOfVoiceChannels()), true)
                .addField("Users", String.valueOf(shardHandler.getNumberOfUsers()), true);

        channel.sendMessage(embedBuilder.build()).queue();
    }
}

