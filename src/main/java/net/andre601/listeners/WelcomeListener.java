package net.andre601.listeners;

import net.andre601.commands.server.CmdWelcome;
import net.andre601.util.DBUtil;
import net.andre601.util.ImageUtil;
import net.andre601.util.PermUtil;
import net.andre601.util.Static;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class WelcomeListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        Guild g = e.getGuild();

        if(!DBUtil.getWelcome(g).equals("none")){
            TextChannel tc = CmdWelcome.getChannel(g);

            if(tc != null){
                if(!PermUtil.canWrite(tc))
                    return;

                //  Creating a new message with the MessageBuilder
                Message msg = new MessageBuilder()
                        .append(String.format(
                                "Welcome %s!",
                                e.getUser().getAsMention()
                        )).build();

                if(PermUtil.canUploadImage(tc)){
                    ImageUtil.createWelcomeImg(e.getUser(), g, tc, msg, DBUtil.getImage(g), DBUtil.getColor(g));
                }else{
                    tc.sendMessage(msg).queue();
                }
            }
        }
    }

    public void onTextChannelDelete(TextChannelDeleteEvent e) {
        Guild g = e.getGuild();

        if(!DBUtil.getWelcome(g).equals("none")){
            TextChannel channel = g.getTextChannelById(DBUtil.getWelcome(g));
            if(e.getChannel() == channel){
                CmdWelcome.resetChannel(g);
            }
        }
    }

}