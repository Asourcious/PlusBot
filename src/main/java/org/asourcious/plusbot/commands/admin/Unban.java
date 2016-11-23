package org.asourcious.plusbot.commands.admin;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Unban extends Command {

    public Unban(PlusBot plusBot) {
        super(plusBot);
        this.help = "Unbans the user with the provided ID";
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (stripped.isEmpty())
            return "You must provide an id!";
        if (!NumberUtils.isParsable(stripped))
            return "That is not a valid id!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        try {
            guild.getController().unban(stripped).queue();
            channel.sendMessage("Unbanned user with id " + stripped + "\"").queue();
        } catch (Exception ex) {
            channel.sendMessage("Unable to unban user with id \"" + stripped + "\"").queue();
        }
    }
}
