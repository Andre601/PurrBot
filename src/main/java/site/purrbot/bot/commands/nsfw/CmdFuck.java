package site.purrbot.bot.commands.nsfw;

import ch.qos.logback.classic.Logger;
import com.github.rainestormee.jdacommand.CommandAttribute;
import com.github.rainestormee.jdacommand.CommandDescription;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import org.slf4j.LoggerFactory;
import site.purrbot.bot.PurrBot;
import site.purrbot.bot.commands.Command;
import site.purrbot.bot.constants.API;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@CommandDescription(
        name = "Fuck",
        description =
                "Wanna fuck someone?\n" +
                "Mention a user, to send a request.\n" +
                "The mentioned user can accept it by clicking on the ✅, deny it by clicking on ❌ or let it time out.",
        triggers = {"fuck", "sex"},
        attributes = {
                @CommandAttribute(key = "category", value = "nsfw"),
                @CommandAttribute(key = "usage", value = "{p}fuck @user")
        }
)
public class CmdFuck implements Command{

    private Logger logger = (Logger)LoggerFactory.getLogger(CmdFuck.class);

    private PurrBot manager;

    public CmdFuck(PurrBot manager){
        this.manager = manager;
    }

    private static ArrayList<String> alreadyInQueue = new ArrayList<>();

    private int getRandomPercent(){
        return manager.getRandom().nextInt(10);
    }

    private EmbedBuilder getFuckEmbed(Member member1, Member member2, String url){
        return manager.getEmbedUtil().getEmbed()
                .setDescription(String.format(
                        "%s and %s are having sex!",
                        member1.getEffectiveName(),
                        member2.getEffectiveName()
                ))
                .setImage(url);
    }

    @Override
    public void execute(Message msg, String s){
        TextChannel tc = msg.getTextChannel();
        Member author = msg.getMember();
        Guild guild = msg.getGuild();

        if(msg.getMentionedUsers().isEmpty()){
            manager.getEmbedUtil().sendError(tc, msg.getAuthor(), "Please mention a user to fuck.");
            return;
        }

        User user = msg.getMentionedUsers().get(0);

        if(user == msg.getJDA().getSelfUser()){
            if(manager.isBeta()){
                tc.sendMessage(String.format(
                        "\\*Slaps %s* Nononononono! Not with me!",
                        author.getAsMention()
                )).queue();
                return;
            }
            if(manager.getPermUtil().isSpecial(msg.getAuthor().getId())){
                int random = getRandomPercent();

                if(random >= 1 && random <= 3) {
                    tc.sendMessage(String.format(
                            manager.getMessageUtil().getRandomAcceptFuckMsg(),
                            author.getAsMention()
                    )).queue();
                    return;
                }else{
                    tc.sendMessage(String.format(
                            manager.getMessageUtil().getRandomDenyFuckMsg(),
                            author.getAsMention()
                    )).queue();
                    return;
                }
            }else{
                tc.sendMessage(String.format(
                        "\\*Slaps %s* Nononononono! Not with me!",
                        msg.getAuthor().getAsMention()
                )).queue();
                return;
            }
        }

        if(user == msg.getAuthor()){
            tc.sendMessage(String.format(
                    "%s How can you actually fuck yourself?! (And no. Masturbation is not a valid answer)",
                    msg.getAuthor().getAsMention()
            )).queue();
            return;
        }

        if(user.isBot()){
            manager.getEmbedUtil().sendError(tc, msg.getAuthor(), "You can't fuck with bots! >-<");
            return;
        }

        if(alreadyInQueue.contains(author.getUser().getId())){
            tc.sendMessage(String.format(
                    "You already have an open request for someone to fuck with you %s!\n" +
                    "Please wait until the person accepts or denies it, or the request times out.",
                    author.getAsMention()
            )).queue();
            return;
        }

        alreadyInQueue.add(author.getUser().getId());
        tc.sendMessage(String.format(
                "Hey %s!\n" +
                "%s wants to have sex with you. Do you want that too?\n" +
                "Click ✅ or ❌ to accept or deny the request.\n" +
                "\n" +
                "**This request will time out in 1 minute!**",
                user.getAsMention(),
                msg.getMember().getEffectiveName()
        )).queue(message -> {
            message.addReaction("✅").queue();
            message.addReaction("❌").queue(emote -> {
                EventWaiter waiter = manager.getWaiter();
                waiter.waitForEvent(
                        GuildMessageReactionAddEvent.class,
                        ev -> {
                            MessageReaction.ReactionEmote emoji = ev.getReactionEmote();
                            if(!emoji.getName().equals("✅") && !emoji.getName().equals("❌")) return false;
                            if(ev.getUser().isBot()) return false;
                            if(!ev.getUser().equals(user)) return false;

                            return ev.getMessageId().equals(message.getId());
                        },
                        ev -> {
                            if(ev.getReactionEmote().getName().equals("❌")){
                                try{
                                    message.delete().queue();
                                }catch(Exception ex){
                                    logger.warn(String.format(
                                            "Couldn't delete a own message... Reason: %s",
                                            ex.getMessage()
                                    ));
                                }

                                alreadyInQueue.remove(author.getUser().getId());

                                ev.getChannel().sendMessage(String.format(
                                        "%s doesn't want to lewd with you %s. >.<",
                                        guild.getMember(user).getEffectiveName(),
                                        author.getAsMention()
                                )).queue();
                                return;
                            }

                            if(ev.getReactionEmote().getName().equals("✅")){
                                try{
                                    message.delete().queue();
                                }catch(Exception ex){
                                    logger.warn(String.format(
                                            "Couldn't delete a own message... Reason: %s",
                                            ex.getMessage()
                                    ));
                                }

                                alreadyInQueue.remove(author.getUser().getId());

                                String link = manager.getHttpUtil().getImage(API.GIF_FUCK_LEWD);

                                ev.getChannel().sendMessage(String.format(
                                        "%s accepted your invite %s! 0w0",
                                        guild.getMember(user).getEffectiveName(),
                                        author.getAsMention()
                                )).queue(del -> del.delete().queueAfter(5, TimeUnit.SECONDS));

                                if(link == null){
                                    ev.getChannel().sendMessage(String.format(
                                            "%s and %s are having sex!",
                                            msg.getMember().getEffectiveName(),
                                            guild.getMember(user).getEffectiveName()
                                    )).queue();
                                    return;
                                }

                                ev.getChannel().sendMessage(
                                        getFuckEmbed(author, guild.getMember(user), link).build()
                                ).queue();

                            }
                        }, 1, TimeUnit.MINUTES,
                        () -> {
                            try {
                                message.delete().queue();
                            }catch (Exception ex){
                                logger.warn("Couldn't delete a own message. ._.");
                            }

                            alreadyInQueue.remove(author.getUser().getId());

                            tc.sendMessage(String.format(
                                    "Looks like he/she doesn't want to have sex with you %s ;-;",
                                    author.getAsMention()
                            )).queue();
                        });
            });
        });
    }
}