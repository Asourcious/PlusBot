package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.Constants;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Ping extends NoArgumentCommand {

    public Ping(PlusBot plusBot) {
        super(plusBot);
        this.name = "Ping";
        this.help = "Measures the response time of " + Constants.NAME;
        this.rateLimit = new RateLimit(2, 30, TimeUnit.SECONDS);
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        channel.sendMessage("Ping: ...").queue(msg ->
                msg.editMessage("Ping: " + message.getCreationTime().until(msg.getCreationTime(), ChronoUnit.MILLIS) + "ms").queue()
        );
    }
}
