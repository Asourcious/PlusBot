package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Shard extends NoArgumentCommand {

    public Shard(PlusBot plusBot) {
        super(plusBot);
        this.name = "Shard";
        this.help = "Returns information about the current shard.";
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        String msg = "```\n";
        msg += plusBot.getShardHandler().getInformation(message.getJDA());
        msg += "\n```";

        channel.sendMessage(msg).queue();
    }
}
