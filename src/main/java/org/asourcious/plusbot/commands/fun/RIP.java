package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.utils.DiscordUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RIP extends Command {

    public RIP(PlusBot plusBot) {
        super(plusBot);
        this.name = "RIP";
        this.help = "";
        this.rateLimit = new RateLimit(4, 1, TimeUnit.MINUTES);
    }

    @Override
    public String isValid(Message message, String stripped) {
        if (DiscordUtil.getTrimmedMentions(message).isEmpty() && stripped.isEmpty())
            return "You must either mention a user or provide text!";
        if (!DiscordUtil.getTrimmedMentions(message).isEmpty() && !stripped.isEmpty())
            return "You can't mention a user and provide text!";
        if (DiscordUtil.getTrimmedMentions(message).size() > 1)
            return "You may only mention one user!";
        return null;
    }

    @Override
    public void execute(String stripped, Message message, User author, MessageChannel channel, Guild guild) {
        boolean isMention = stripped.isEmpty();
        String text = isMention
                ? guild.getMember(DiscordUtil.getTrimmedMentions(message).get(0)).getEffectiveName()
                : stripped;


        try {
            BufferedImage image = ImageIO.read(new File("media/tombstone.png"));

            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(scaleFont(text, new Rectangle(70, 460, 450, 100), g2d));
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, 70, 470);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);

            channel.sendFile(out.toByteArray(), "rip.png", null).queue();
        } catch (IOException e) {
            channel.sendMessage("Error creating tombstone!").queue();
        }
    }

    private Font scaleFont(String text, Rectangle rect, Graphics g) {
        float fontSize = 10.0f;

        Font font = g.getFont().deriveFont(fontSize);
        int width = g.getFontMetrics(font).stringWidth(text);
        fontSize = (rect.width / width ) * fontSize;
        font = g.getFont().deriveFont(fontSize);
        if (font.getSize() > 160)
            font = font.deriveFont(160.0f);
        return font;
    }
}