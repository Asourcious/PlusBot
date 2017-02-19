package org.asourcious.plusbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.config.Settings;

import java.util.concurrent.TimeUnit;

public abstract class Command {

    protected final PlusBot plusBot;
    protected final Settings settings;

    protected String name = getClass().getSimpleName();
    protected String help = "No help is provided. Please contact Asourcious if you see this.";
    protected RateLimit rateLimit;
    protected String[] aliases = new String[0];
    protected Command[] children = new Command[0];
    protected Permission[] requiredPermissions = new Permission[0];
    protected PermissionLevel permissionLevel = PermissionLevel.EVERYONE;

    public Command(PlusBot plusBot) {
        this.plusBot = plusBot;
        this.settings = plusBot.getSettings();
    }

    public abstract String isValid(Message message, String stripped);
    public abstract void execute(String stripped, Message message, User author, TextChannel channel, Guild guild);

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Command[] getChildren() {
        return children;
    }

    public Permission[] getRequiredPermissions() {
        return requiredPermissions;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public class RateLimit {

        private int numUses;
        private int amountTime;
        private TimeUnit unit;

        public RateLimit(int numUses, int amountTime, TimeUnit unit) {
            this.numUses = numUses;
            this.amountTime = amountTime;
            this.unit = unit;
        }

        public int getNumUses() {
            return numUses;
        }

        public int getAmountTime() {
            return amountTime;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            return numUses + " per " + amountTime + " " + unit.toString();
        }
    }
}
