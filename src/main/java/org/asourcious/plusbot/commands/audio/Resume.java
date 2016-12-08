package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;

public class Resume extends NoArgumentCommand {

    public Resume(PlusBot plusBot) {
        super(plusBot);
        this.help = "Resumes playback of the audio queue";
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        plusBot.getPlayerHandler().getPlayer(guild).play();
        plusBot.getPlayerHandler().getPlayer(guild).setUpdateChannel(channel);
        channel.sendMessage("Resumed playback.").queue();
    }
}
