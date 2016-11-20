package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.commands.SubCommand;
import org.asourcious.plusbot.utils.DiscordUtil;

import java.awt.Color;
import java.util.List;

public class AutoRole extends Command {

    public AutoRole(PlusBot plusBot) {
        super(plusBot);
        this.help = "Modifies roles to be automatically assigned to members when they join the server";
        this.children = new Command[] {
                new Human(plusBot),
                new Bot(plusBot)
        };
        this.requiredPermission = PermissionLevel.SERVER_MODERATOR;
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (stripped.isEmpty())
            return null;

        String[] args = stripped.toLowerCase().split("\\s+", 2);

        if (args.length != 2)
            return "You must use two arguments!";

        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        DiscordUtil.checkForMissingAutoRoles(plusBot.getSettings(), guild);

        Role autoBotRole = guild.getRoleById(settings.getProfile(guild).getAutoBotRole());
        Role autoHumanRole = guild.getRoleById(settings.getProfile(guild).getAutoHumanRole());

        embedBuilder
                .setColor(Color.green)
                .setDescription("Auto Roles for " + guild.getName())
                .addField("Human", autoHumanRole == null ? "None" : autoHumanRole.getName(), false)
                .addField("Bots", autoBotRole == null ? "None" : autoBotRole.getName(), false);

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

    private class Human extends SubCommand {
        public Human(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            if (stripped.equals("clear")) {
                settings.getProfile(guild).removeAutoHumanRole();
                channel.sendMessage("Removed auto human role").queue();
                return;
            }

            Role target = getTargetRole(stripped, channel, message, guild);
            if (target == null)
                return;

            settings.getProfile(guild).setAutoHumanRole(target.getId());
            channel.sendMessage("Updated auto roles").queue();
        }
    }

    private class Bot extends SubCommand {
        public Bot(PlusBot plusBot) {
            super(plusBot);
        }

        @Override
        public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
            if (stripped.equals("clear")) {
                settings.getProfile(guild).removeAutoBotRole();
                channel.sendMessage("Removed auto bot role").queue();
                return;
            }

            Role target = getTargetRole(stripped, channel, message, guild);
            if (target == null)
                return;

            settings.getProfile(guild).setAutoBotRole(target.getId());
            channel.sendMessage("Updated auto roles").queue();
        }
    }
}
