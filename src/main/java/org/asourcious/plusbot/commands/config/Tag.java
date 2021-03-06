package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.math3.util.Pair;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandContainer;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.config.source.GuildTags;

public class Tag extends CommandContainer {

    public Tag(PlusBot plusBot) {
        super(plusBot);
        this.help = "Creates, edits, and views \"tags.\" Tags are a way to store information with a name for easy " +
                "recollection.";
        this.children = new Command[] {
                new Create(plusBot),
                new Delete(plusBot),
                new Edit(plusBot)
        };
        this.permissionLevel = PermissionLevel.SERVER_MODERATOR;
    }

    private class Create extends Command {
        Create(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            String[] args = stripped.split("\\s+", 2);
            if (args.length != 2)
                return "You must supply a name and text!";

            return null;
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

            if (tags.get(guild.getId()).size() >= 40) {
                channel.sendMessage("Only up to 20 tags are allowed currently!").queue();
                return;
            }

            tags.add(guild.getId(), new Pair<>(args[0], args[1]));
            channel.sendMessage("Added tag \"" + args[0]  + "\"").queue();
        }
    }

    private class Delete extends Command {
        Delete(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            if (stripped.isEmpty())
                return "You must supply a tag name!";
            return null;
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

    private class Edit extends Command {
        Edit(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public String isValid(Message message, String stripped) {
            if (stripped.isEmpty())
                return "You must supply a tag name!";
            return null;
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
