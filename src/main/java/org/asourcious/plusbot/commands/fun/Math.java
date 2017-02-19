package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.util.FormatUtils;

public class Math extends Command {

    public Math(PlusBot plusBot) {
        super(plusBot);
        this.help = "Evaluates a mathematical expression";
    }

    @Override
    public String isValid(Message message, String stripped) {
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        Expression expression = new ExpressionBuilder(stripped).build();
        ValidationResult result = expression.validate();

        MessageBuilder messageBuilder = new MessageBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (result.isValid()) {
            messageBuilder.append("Successfully evaluated");
            embedBuilder.addField("Result: ", String.valueOf(expression.evaluate()), false);
        } else {
            messageBuilder.append("Error evaluating");
            embedBuilder.addField("Errors", FormatUtils.getFormatted(result.getErrors()), false);
        }
        messageBuilder.setEmbed(embedBuilder.build());
        channel.sendMessage(messageBuilder.build()).queue();
    }
}
