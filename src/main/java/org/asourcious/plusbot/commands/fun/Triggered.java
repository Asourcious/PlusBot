package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Triggered extends NoArgumentCommand {

    private Random random;

    public Triggered(PlusBot plusBot) {
        super(plusBot);
        this.name = "Triggered";
        this.help = "Posts a \"triggered\" GIF.";

        this.random = new Random();
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        try {
            channel.sendFile(new File("media/triggered" + (random.nextInt(3) + 1) + ".gif"), null).queue();
        } catch (IOException ignored) {}
    }
}
