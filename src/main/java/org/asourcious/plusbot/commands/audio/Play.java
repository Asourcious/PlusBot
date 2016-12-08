package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.handle.audio.Player;

import java.util.regex.Pattern;

public class Play extends Command {

    private static final Pattern URL = Pattern.compile("^(http(s)?://)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&/=]*)");

    public Play(PlusBot plusBot) {
        super(plusBot);
        this.help = "Adds audio from the provided url to the queue.";
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);
        player.setUpdateChannel(channel);

        if (URL.matcher(stripped).matches()) {
            player.queue(stripped);
        } else {
            player.queue("ytsearch:" + stripped);
        }
    }
}
