package com.andre601.purrbot.commands.fun;

import com.andre601.purrbot.listeners.ReadyListener;
import com.andre601.purrbot.util.HttpUtil;
import com.andre601.purrbot.util.PermUtil;
import com.andre601.purrbot.util.constants.API;
import com.andre601.purrbot.util.constants.Emotes;
import com.andre601.purrbot.commands.Command;
import com.github.rainestormee.jdacommand.CommandAttribute;
import com.github.rainestormee.jdacommand.CommandDescription;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.andre601.purrbot.util.messagehandling.EmbedUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.andre601.purrbot.core.PurrBot.waiter;

@CommandDescription(
        name = "Neko",
        description =
                "Gives you a lovely neko (catgirl)\n" +
                "\n" +
                "You can use additional args in the command.\n" +
                "`--gif` for a gif\n" +
                "`--slide` for a slideshow with 30 images\n" +
                "Both arguments can be combined.",
        triggers = {"neko", "catgirl"},
        attributes = {
                @CommandAttribute(key = "fun"),
                @CommandAttribute(key = "usage", value = "neko [--gif] [--slide]")
        }
)
public class CmdNeko implements Command {

    private Slideshow.Builder sBuilder =
            new Slideshow.Builder().setEventWaiter(waiter).setTimeout(1, TimeUnit.MINUTES);
    private static List<String> nekoUserID = new ArrayList<>();

    @Override
    public void execute(Message msg, String args) {
        Guild guild = msg.getGuild();
        TextChannel tc = msg.getTextChannel();

        if(PermUtil.check(tc, Permission.MESSAGE_MANAGE))
            msg.delete().queue();

        if(args.toLowerCase().contains("--slide")){
            if(nekoUserID.contains(msg.getAuthor().getId())){
                EmbedUtil.error(msg,
                        "Only one slideshow per user!\n" +
                        "Please use or close your other one."
                );
                return;
            }
            tc.sendTyping().queue();

            nekoUserID.add(msg.getAuthor().getId());
            String urls;
            if(args.toLowerCase().contains("--gif")){
                urls = HttpUtil.getImage(API.GIF_NEKO, 20);
            }else{
                urls = HttpUtil.getImage(API.IMG_NEKO, 20);
            }

            if(urls == null){
                EmbedUtil.error(msg, "Couldn't reach the API! Try again later.");
                return;
            }

            Slideshow slideshow = sBuilder
                    .setUsers(msg.getAuthor(), guild.getOwner().getUser())
                    .setText("Neko-slideshow!")
                    .setDescription(String.format(
                            "Use the reactions to navigate through the images!\n" +
                            "Only the author of the command (`%s`) and the Guild-Owner (`%s`) " +
                            "can use the navigation!\n" +
                            "\n" +
                            "__**Slideshows may take a while to update!**__",
                            msg.getAuthor().getAsTag().replace("`", "'"),
                            guild.getOwner().getUser().getAsTag().replace("`", "'")
                    ))
                    .setUrls(urls.replace("\"", "").split(","))
                    .setFinalAction(message -> {
                        if(message != null) message.delete().queue();
                        nekoUserID.remove(msg.getAuthor().getId());
                    })
                    .build();
            slideshow.display(tc);
            return;
        }

        if(args.toLowerCase().contains("--gif")){
            String link = HttpUtil.getImage(API.GIF_NEKO, 0);
            if(link == null){
                EmbedUtil.error(msg, "Couldn't reach the API! Try again later.");
                return;
            }
            EmbedBuilder nekogif = EmbedUtil.getEmbed(msg.getAuthor())
                    .setTitle("Neko [Gif]", link)
                    .setImage(link);

            tc.sendMessage(String.format(
                    "%s Getting a cute neko-gif...",
                    Emotes.ANIM_LOADING.getEmote()
            )).queue(message -> {
                message.editMessage(EmbedBuilder.ZERO_WIDTH_SPACE).embed(nekogif.build()).queue();
            });
            return;
        }

        String link = HttpUtil.getImage(API.IMG_NEKO, 0);

        if(link == null){
            EmbedUtil.error(msg, "Couldn't reach the API! Try again later.");
            return;
        }

        tc.sendMessage(String.format(
                "%s Getting a cute neko...",
                Emotes.ANIM_LOADING.getEmote()
        )).queue(message -> {
            EmbedBuilder neko = EmbedUtil.getEmbed(msg.getAuthor())
                    .setTitle("Neko [Img]", link)
                    .setImage(link);

            if(link.equals("https://cdn.nekos.life/v3/sfw/img/neko/neko_079.jpg")){
                if(PermUtil.isBeta()){
                    neko.setDescription("That is me! >w<");
                    message.addReaction("❤").queue();
                }else{
                    Emote snuggle = ReadyListener.getShardManager().getEmoteById(Emotes.SNUGGLE.getId());
                    neko.setDescription("That is my little sister!");

                    if(PermUtil.check(message.getTextChannel(), Permission.MESSAGE_EXT_EMOJI))
                        message.addReaction(snuggle).queue();
                }
            }else
            if(link.equals("https://cdn.nekos.life/v3/sfw/img/neko/neko_139.png")){
                if(PermUtil.isBeta()){
                    Emote purr = ReadyListener.getShardManager().getEmoteById(Emotes.PURR.getId());
                    neko.setDescription("That is my big sister!");

                    if(PermUtil.check(message.getTextChannel(), Permission.MESSAGE_EXT_EMOJI))
                        message.addReaction(purr).queue();
                }else{
                    neko.setDescription("T-that is me! OwO");
                    message.addReaction("❤").queue();
                }
            }

            //  Editing the message to add the image ("should" prevent issues with empty embeds)
            message.editMessage(EmbedBuilder.ZERO_WIDTH_SPACE).embed(neko.build()).queue();
        });
    }
}
