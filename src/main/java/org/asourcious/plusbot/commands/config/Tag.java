package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.commands.SubCommand;
import org.asourcious.plusbot.config.source.GuildTags;

public class Tag extends Command {

    public Tag(PlusBot plusBot) {
        super(plusBot);
        this.help = "";
        this.children = new Command[] {
                new Create(plusBot),
                new Delete(plusBot),
                new Edit(plusBot)
        };
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {

    }

    private class Create extends SubCommand {
        Create(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            GuildTags tags = settings.getGuildTags();
            String[] args = stripped.split("\\s+", 2);

            if (tags.existsWithName(guild.getId(), args[0]))  {
                channel.sendMessage("There is already a tag with that name! Edit or remove the existing tag!").queue();
                return;
            }

            if (plusBot.getCommandHandler().hasCommand(args[0])) {
                channel.sendMessage("A command exists with that name, that isn't allowed!").queue();
                return;
            }

            tags.add(guild.getId(), new Pair<>(args[0], args[1]));
            channel.sendMessage("Added tag \"" + args[0]  + "\"").queue();
        }
    }

    private class Delete extends SubCommand {
        Delete(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            GuildTags tags = settings.getGuildTags();

            if (!tags.existsWithName(guild.getId(), stripped)) {
                channel.sendMessage("That tag doesn't exist!").queue();
                return;
            }

            tags.removeByName(guild.getId(), stripped);
            channel.sendMessage("Successfully removed tag!").queue();
        }
    }

    private class Edit extends SubCommand {
        Edit(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            GuildTags tags = settings.getGuildTags();
            String[] args = stripped.split("\\s+", 2);

            if (!tags.existsWithName(guild.getId(), args[0])) {
                channel.sendMessage("That tag doesn't exist!").queue();
                return;
            }

            tags.edit(guild.getId(), args[0], args[1]);
            channel.sendMessage("Edited tag \"" + args[0] + "\"").queue();
        }
    }
}
