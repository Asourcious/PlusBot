package org.asourcious.plusbot.commands.maintenance;

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
import org.asourcious.plusbot.utils.FormatUtil;
import org.asourcious.plusbot.utils.SystemUtil;

import java.time.OffsetDateTime;

public class Status extends NoArgumentCommand {

    public Status(PlusBot civBot) {
        super(civBot);
        this.name = "Status";
        this.help = "Returns information about the status of " + Constants.NAME;
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        ShardHandler shardHandler = plusBot.getShardHandler();

        String msg = "```diff\n";
        msg += "- General\n";
        msg += "+ Name: " + Constants.NAME + "\n";
        msg += "+ Version: " + Constants.VERSION + "\n";
        msg += "+ Uptime: " + FormatUtil.getFormattedDuration(Statistics.startTime, OffsetDateTime.now()) + "\n";
        msg += "\n";

        msg += "- System\n";
        msg += "+ Threads: " + Thread.activeCount() + "\n";
        msg += "+ CPU Usage: " + SystemUtil.getCPUUsage() + "%\n";
        msg += "+ RAM Usage: " + SystemUtil.getUsedMemory() + "/" + SystemUtil.getTotalMemory() + "MB\n";
        msg += "\n";

        msg += "- Statistics\n";
        msg += "+ Messages Received: " + Statistics.numMessages + "\n";
        msg += "+ Commands Executed: " + Statistics.numCommands + "\n";
        msg += "+ Guilds: " + shardHandler.getNumberOfGuilds() + "\n";
        msg += "+ TC's: " + shardHandler.getNumberOfTextChannels() + "\n";
        msg += "+ VC's: " + shardHandler.getNumberOfVoiceChannels() + "\n";
        msg += "+ Users: " + shardHandler.getNumberOfUsers() + "\n";

        msg += "```";

        channel.sendMessage(msg).queue();
    }
}

