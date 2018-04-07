package net.Andre601.util;

import net.Andre601.core.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;

import java.awt.Color;

public class MessageUtil {

    public static String isBot(User user){
        if(user.isBot()){
            return "Yes";
        }
        return "No";
    }

    public static String getLevel(Guild g){

        switch (g.getVerificationLevel().toString().toLowerCase()){

            case "high":
                return "(╯°□°）╯︵ ┻━┻";

            case "very_high":
                return "┻━┻ ミ ヽ(ಠ益ಠ)ﾉ 彡 ┻━┻";

            default:
                return g.getVerificationLevel().toString().toLowerCase();
        }
    }

    private static String getGameStatus(Member member){
        return (member != null && member.getGame() != null ?
                " (" + (member.getGame().getUrl() == null ?
                        String.format(
                                "`%s`",
                                member.getGame().getName()) :
                        String.format(
                                "[`%s`](%s)",
                                member.getGame().getName(),
                                member.getGame().getUrl()
                        )
                ) + ")" : "");
    }

    public static String getStatus(Member member, Message msg){
        if(member.getOnlineStatus() == OnlineStatus.ONLINE){
            return (PermUtil.canUseCustomEmojis(msg) ? "<:online:426838620033253376> "
                    : "" ) + "`Online`" + getGameStatus(member);
        }else
        if(member.getOnlineStatus() == OnlineStatus.IDLE){
            return (PermUtil.canUseCustomEmojis(msg) ? "<:idle:426838620012281856> "
                    : "" ) + "`Idle`" + getGameStatus(member);
        }else
        if(member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB){
            return (PermUtil.canUseCustomEmojis(msg) ? "<:dnd:426838619714748439> "
                    : "" ) + "`Do not disturb`" + getGameStatus(member);
        }
        return (PermUtil.canUseCustomEmojis(msg) ? "<:offline:426840813729742859> "
                : "" ) + "`Offline`";
    }

    public static String getTag(User user){
        return user.getName() + "#" + user.getDiscriminator();
    }

    public static void sendWebhookEmbed(String webhookURL, Guild g, Color color, String title, String desc){

        MessageEmbed webhook = getEmbed()
                .setColor(color)
                .setThumbnail(g.getIconUrl())
                .setDescription(desc)
                .setFooter(String.format(
                        "%s",
                        Main.now()
                ), null).build();

        WebhookClient webc = Main.webhookClient(webhookURL);
        webc.send(new WebhookMessageBuilder().addEmbeds(webhook).
                setUsername(title).
                setAvatarUrl(g.getJDA().getSelfUser().getEffectiveAvatarUrl()).build());
        webc.close();

    }

    public static EmbedBuilder getEmbed(User user){
        return new EmbedBuilder().setFooter(String.format(
                "Requested by: %s | %s",
                getTag(user),
                Main.now()
        ), user.getEffectiveAvatarUrl());
    }

    public static EmbedBuilder getEmbed(){
        return new EmbedBuilder();
    }
}