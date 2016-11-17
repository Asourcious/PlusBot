package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.commands.SubCommand;
import org.asourcious.plusbot.config.DataSource;
import org.asourcious.plusbot.utils.DiscordUtil;
import org.asourcious.plusbot.utils.FormatUtil;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AutoRole extends Command {

    public AutoRole(PlusBot plusBot) {
        super(plusBot);
        this.name = "AutoRole";
        this.help = "Modifies roles to be automatically assigned to members when they join the server";
        this.children = new Command[] {
                new Add(plusBot),
                new Remove(plusBot)
        };
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (stripped.isEmpty())
            return null;

        String[] args = stripped.toLowerCase().split("\\s+", 3);

        boolean isBot = args[1].equals("bot");

        if (args.length < 1)
            return "You must use at least one argument!";
        if (isBot && args.length < 2)
            return "You must supply at least two arguments if the first argument is bot!";
        if (!args[0].equals("add") && !args[0].equals("remove"))
            return "The first argument must be add or remove!";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        DiscordUtil.checkForMissingAutoRoles(plusBot, guild);

        Set<String> humanRoleNames = settings.getAutoHumanRoles().get(guild.getId())
                .parallelStream().map(id -> guild.getRoleById(id).getName()).collect(Collectors.toSet());
        Set<String> botRoleNames = settings.getAutoBotRoles().get(guild.getId())
                .parallelStream().map(id -> guild.getRoleById(id).getName()).collect(Collectors.toSet());

        embedBuilder
                .setColor(Color.green)
                .setTitle("Auto Roles for " + guild.getName())
                .addField("Humans", FormatUtil.getFormatted(humanRoleNames), false)
                .addField("Bots", FormatUtil.getFormatted(botRoleNames), false);

        channel.sendMessage(new MessageBuilder().setEmbed(embedBuilder.build()).build()).queue();
    }

    private Role getTargetRole(String stripped, TextChannel channel, Message message, Guild guild) {
        if (stripped.isEmpty()) {
            List<Role> roles = message.getMentionedRoles();
            if (roles.size() > 1) {
                channel.sendMessage("Multiple roles mentioned. Only one role can be modified at a time!").queue();
                return null;
            }
            if (roles.isEmpty()) {
                channel.sendMessage("No roles mentioned!").queue();
                return null;
            }
            return roles.get(0);
        } else {
            List<Role> roles = guild.getRolesByName(stripped, true);
            if (roles.size() > 1) {
                channel.sendMessage("Multiple roles found with that name. mention the role instead!").queue();
                return null;
            }
            if (roles.isEmpty()) {
                channel.sendMessage("No roles found with name \"" + stripped + "\"").queue();
                return null;
            }
            return roles.get(0);
        }
    }

    private class Add extends SubCommand {
        public Add(PlusBot plusBot) {
            super(plusBot);
            this.name = "Add";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            boolean isBot = stripped.split("\\s+")[0].equalsIgnoreCase("bot");
            if (isBot)
                stripped = stripped.substring("bot".length()).trim();

            Role target = getTargetRole(stripped, channel, message, guild);
            if (target == null)
                return;

            DataSource autoRoles = isBot ? settings.getAutoBotRoles() : settings.getAutoHumanRoles();
            autoRoles.add(guild.getId(), target.getId());
            channel.sendMessage("Updated auto roles").queue();
        }
    }

    private class Remove extends SubCommand {
        public Remove(PlusBot plusBot) {
            super(plusBot);
            this.name = "Remove";
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            boolean isBot = stripped.split("\\s+")[0].equalsIgnoreCase("bot");
            if (isBot)
                stripped = stripped.substring("bot".length()).trim();

            Role target = getTargetRole(stripped, channel, message, guild);
            if (target == null)
                return;

            DataSource autoRoles = isBot ? settings.getAutoBotRoles() : settings.getAutoHumanRoles();
            autoRoles.remove(guild.getId(), target.getId());
            channel.sendMessage("Updated auto roles").queue();
        }
    }
}
