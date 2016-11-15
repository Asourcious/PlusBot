package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandContainer;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.commands.SubCommand;
import org.asourcious.plusbot.config.Configuration;
import org.asourcious.plusbot.utils.DiscordUtil;

import java.util.List;

public class Blacklist extends CommandContainer {

    public Blacklist(PlusBot plusBot) {
        super(plusBot);
        this.name = "Blacklist";
        this.help = "Adds and removes users from the server's blacklist.";
        this.children = new Command[] {
                new Add(plusBot),
                new Remove(plusBot),
                new Clear(plusBot)
        };
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (!stripped.equalsIgnoreCase("add") && !stripped.equalsIgnoreCase("remove") && !stripped.equalsIgnoreCase("clear"))
            return "You must use \"add\" or \"remove\" as an argument!";
        if (message.getMentionedUsers().isEmpty() && !stripped.equalsIgnoreCase("clear"))
            return "You must mention at least one user!";

        return null;
    }

    private class Add extends SubCommand {
        public Add(PlusBot plusBot) {
            super(plusBot);
            this.name = "Add";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            Configuration configuration = settings.getConfiguration(guild);
            List<User> targets = DiscordUtil.getTrimmedMentions(message);
            int numUpdated = 0;

            for (User user : targets) {
                if (!configuration.getBlacklist().contains(user.getId())) {
                    if (PermissionLevel.canInteract(guild.getMember(author), guild.getMember(user))) {
                        configuration.addUserToBlacklist(user.getId());
                        numUpdated++;
                    } else {
                        channel.sendMessage("You don't have the necessary permissions to add **" + user.getName() + "** to the blacklist").queue();
                    }
                }
            }
            channel.sendMessage("Successfully added **" + numUpdated + "** users to the blacklist.").queue();
        }
    }

    private class Remove extends SubCommand {
        public Remove(PlusBot plusBot) {
            super(plusBot);
            this.name = "Remove";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            Configuration configuration = settings.getConfiguration(guild);
            List<User> targets = DiscordUtil.getTrimmedMentions(message);
            int numUpdated = 0;

            for (User user : targets) {
                if (configuration.getBlacklist().contains(user.getId())) {
                    configuration.removeUserFromBlacklist(user.getId());
                    numUpdated++;
                }
            }
            channel.sendMessage("Successfully removed **" + numUpdated + "** users from the blacklist.").queue();
        }
    }

    private class Clear extends SubCommand {
        public Clear(PlusBot plusBot) {
            super(plusBot);
            this.name = "Clear";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            settings.getConfiguration(guild).clearBlacklist();
            channel.sendMessage("Successfully cleared blacklist.").queue();
        }
    }
}
