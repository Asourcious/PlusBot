package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.PermissionLevel;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Eval extends Command {

    public Eval(PlusBot plusBot) {
        super(plusBot);
        this.help = "Evaluates the given code";
        this.permissionLevel = PermissionLevel.OWNER;
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
        engine.put("bot", plusBot);
        engine.put("jda", message.getJDA());
        engine.put("guild", guild);
        engine.put("channel", channel);
        try {
            channel.sendMessage("Evaluated Successfully:\n ```" + engine.eval(stripped) + "```").queue();
        } catch(Exception e) {
            channel.sendMessage("Exception was thrown: ```" + e + "```").queue();
        }
    }
}