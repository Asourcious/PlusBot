package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.handle.audio.Player;

public class Select extends Command {

    public Select(PlusBot plusBot) {
        super(plusBot);
        this.help = "Selects a search result";
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (!NumberUtils.isParsable(stripped))
            return "You must enter a valid number";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Player player = plusBot.getPlayerHandler().getPlayer(guild);
        int size = player.getSearchResults().size();
        int index = NumberUtils.toInt(stripped, -1);

        if (size == 0) {
            channel.sendMessage("There are no results, use `play x` first!").queue();
            return;
        }

        if (index < 1 || index > size) {
            channel.sendMessage("You must provide a valid index!").queue();
            return;
        }

        player.chooseTrack(index - 1);
    }
}
