package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Shards extends NoArgumentCommand {

    public Shards(PlusBot plusBot) {
        super(plusBot);
        this.name = "Shards";
        this.help = "Returns information about the current shard.";
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public void execute(String stripped, Message message, User author, MessageChannel channel, Guild guild) {
        String msg = "";
        msg += "Shards:\n```";
        for (JDA shard : plusBot.getShardHandler().getShards()) {
            msg += plusBot.getShardHandler().getInformation(shard) + "\n";
        }

        msg += "Shards" + plusBot.getShardHandler().getNumberOfShards()
                + " Guilds: " + plusBot.getShardHandler().getNumberOfGuilds()
                + " TC's: " + plusBot.getShardHandler().getNumberOfTextChannels()
                + " VC's: " + plusBot.getShardHandler().getNumberOfVoiceChannels()
                + " Users:" + plusBot.getShardHandler().getNumberOfUsers();
        msg += "```";

        channel.sendMessage(msg).queue();
    }
}
