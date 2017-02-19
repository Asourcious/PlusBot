package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.util.FormatUtils;

import java.awt.Color;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class CommandContainer extends Command {
    public CommandContainer(PlusBot plusBot) {
        super(plusBot);
    }

    @Override
    public final String isValid(Message message, String stripped) {
        if (!stripped.isEmpty())
            return "Those are unacceptable arguments!";
        return null;
    }

    @Override
    public final void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
            .setColor(Color.GREEN)
            .setAuthor("Help for " + name, null, null)
            .addField("Name", name, true)
            .addField("Help", help, true)
            .addField("Ratelimit", rateLimit != null ? rateLimit.toString() : "None", true)
            .addField("Aliases", FormatUtils.getFormatted(aliases), true)
            .addField("Children", FormatUtils.getFormatted(Arrays.stream(children).map(command -> command.name).collect(Collectors.toList())), true)
            .addField("Required Permission", permissionLevel.toString(), true);

        channel.sendMessage(embedBuilder.build()).queue();
    }
}
