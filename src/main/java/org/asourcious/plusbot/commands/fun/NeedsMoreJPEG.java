package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.requests.Requester;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.NoArgumentCommand;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class NeedsMoreJPEG extends NoArgumentCommand {

    public NeedsMoreJPEG(PlusBot plusBot) {
        super(plusBot);
        this.help = "Replies with an image that has a higher concentration of jpeg-ness";
        this.aliases = new String[] { "NeedsMoreJPG" };
        this.rateLimit = new RateLimit(2, 15, TimeUnit.SECONDS);
    }

    @Override
    public void execute(String stripped, Message message, User author, TextChannel channel, Guild guild) {
        if (message.getAttachments().size() != 1) {
            channel.sendMessage("You must provide an attachment").queue();
            return;
        }

        if (!message.getAttachments().get(0).isImage()) {
            channel.sendMessage("You must provide an image!").queue();
            return;
        }

        try {
            URL url = new URL(message.getAttachments().get(0).getUrl());
            URLConnection con = url.openConnection();
            con.addRequestProperty("user-agent", Requester.USER_AGENT);
            BufferedImage image = ImageIO.read(con.getInputStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);

            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(0.02f);
            jpgWriter.setOutput(imageOut);

            jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);

            jpgWriter.dispose();
            channel.sendFile(out.toByteArray(), "needsmorejpeg.png", null).queue();
        } catch (IOException ex) {
            channel.sendMessage("Error generating image!").queue();
            PlusBot.LOG.error("An exception occurred", ex);
        }
    }
}
