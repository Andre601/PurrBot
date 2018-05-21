package net.andre601.commands.info;

import com.jagrosh.jdautilities.menu.Paginator;
import net.andre601.commands.Command;
import net.andre601.util.messagehandling.EmbedUtil;
import net.andre601.util.messagehandling.MessageUtil;
import net.andre601.util.PermUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static net.andre601.commands.server.CmdPrefix.getPrefix;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import static net.andre601.core.PurrBotMain.waiter;

public class CmdHelp implements Command {

    private static Paginator.Builder pBuilder =
            new Paginator.Builder().setEventWaiter(waiter).setTimeout(1, TimeUnit.MINUTES);

    private static void usage(Message msg, String command, String use, String description, String args,
                             String permission){
        msg.getChannel().sendTyping().queue();
        EmbedBuilder uEmbed = EmbedUtil.getEmbed(msg.getAuthor())
                .setTitle(String.format(
                        "Command: %s",
                        command
                ))
                .setDescription(String.format(
                        "**Usage**: `%s%s`\n" +
                        "\n" +
                        "**Description**:\n" +
                        "%s\n" +
                        "\n" +
                        "**Arguments**:\n" +
                        "%s\n" +
                        "\n" +
                        "**Required Permission**:\n" +
                        "%s",
                        getPrefix(msg.getGuild()),
                        use,
                        description,
                        args,
                        permission
                ));

        msg.getChannel().sendMessage(uEmbed.build()).queue();
    }

    private static void usage(Message msg){
        msg.getChannel().sendTyping().queue();
        Paginator page = pBuilder
                .setItems(MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "```\n" +
                        "Pages:\n" +
                        "  Fun\n" +
                        "  Informative\n" +
                        "  NSFW\n" +
                        "  Server\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        getPrefix(msg.getGuild()),
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**Fun**:\n" +
                        "```\n" +
                        "Command:     Argument(s):\n" +
                        "\n" +
                        "Cuddle       <@user ...>\n" +
                        "Hug          <@user ...>\n" +
                        "Kiss         <@user ...>\n" +
                        "Neko         [-slide]\n" +
                        "Pat          <@user ...>\n" +
                        "Slap         <@user ...>\n" +
                        "Tickle       <@user ...>\n" +
                        "\n" +
                        "[optional] | <required>\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        getPrefix(msg.getGuild()),
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**Informative**:\n" +
                        "```\n" +
                        "Command:     Argument(s):\n" +
                        "\n" +
                        "Help\n" +
                        "Info         [-here]\n" +
                        "Img          <URL|neko:<image>.<png/jpg/...>>\n" +
                        "Invite       [-here]\n" +
                        "Quote        <messageID>\n" +
                        "Server\n" +
                        "Stats\n" +
                        "User         [@user]\n" +
                        "\n" +
                        "[optional] | <required>\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        getPrefix(msg.getGuild()),
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**NSFW**:\n" +
                        "```\n" +
                        "Command:     Argument(s):\n" +
                        "\n" +
                        "Lewd         [-slide]\n" +
                        "\n" +
                        "[optional]\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        getPrefix(msg.getGuild()),
                        MessageUtil.getFact()
                ), MessageFormat.format(
                        "**Command-Prefix**: {0}\n" +
                        "\n" +
                        "**Commands**:\n" +
                        "Use the reactions to switch through the pages.\n" +
                        "Type `{0}help [command]` to get infos about a command!\n" +
                        "\n" +
                        "**Server**:\n" +
                        "```\n" +
                        "Command:     Argument(s):\n" +
                        "\n" +
                        "Debug\n" +
                        "Prefix       [set <prefix>|reset]\n" +
                        "Welcome      [set <channelID>|reset|test]\n" +
                        "\n" +
                        "[optional] | <required>\n" +
                        "```\n" +
                        "\n" +
                        "**Random fact**:\n" +
                        "{1}",
                        getPrefix(msg.getGuild()),
                        MessageUtil.getFact()
                ))
                .setText("")
                .setItemsPerPage(1)
                .waitOnSinglePage(true)
                .setFinalAction(message -> {
                    message.delete().queue();
                    msg.getTextChannel().sendMessage("Help closed!").queue(del ->
                            del.delete().queueAfter(5, TimeUnit.SECONDS));
                })
                .build();

        page.display(msg.getChannel());
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        Message msg = e.getMessage();
        TextChannel tc = e.getTextChannel();

        if (!PermUtil.canWrite(msg))
            return;

        if(!PermUtil.canSendEmbed(e.getMessage())){
            e.getTextChannel().sendMessage("I need the permission, to embed Links in this Channel!").queue();
            if(PermUtil.canReact(e.getMessage()))
                e.getMessage().addReaction("🚫").queue();

            return;
        }

        if(PermUtil.canDeleteMsg(msg))
            msg.delete().queue();

        if(args.length == 0){
            usage(msg);
            return;
        }

        switch (args[0].toLowerCase()){

            case "help":
                usage(msg, "Help", "help [command]",
                        "Shows you all available commands.",
                        "`[command]` Gives info about a command.",
                        "`none`"
                );
                break;

            case "info":
                usage(msg, "info", "info [-here]",
                        "Sends you basic info about the bot (A small description, version, used " +
                        "Library, ect)",
                        "`-here` Sends the message in the channel in which you've run the command.",
                        "`none`"
                );
                break;

            case "invite":
                usage(msg, "Invite", "invite [-here]",
                        "Sends you the invite-links for the bot and for the official Discord.",
                        "`-here` Sends the message in the channel in which you've run the command.",
                        "none"
                );
                break;

            case "server":
                usage(msg, "Server",  "server",
                        "Gives you basic server-infos like users online, verification level, ect.",
                        "`none`",
                        "`none`"
                );
                break;

            case "user":
                usage(msg, "User", "user [@user]",
                        "Gives basic information about the mentioned user.",
                        "`<@user>` The user to get infos about (as mention).",
                        "`none`"
                );
                break;

            case "neko":
                usage(msg, "Neko", "neko [-slide]",
                        "Sends a cute neko. 'nuf said.",
                        "`[-slide]` Creates a slideshow with 30 images",
                        "`none`"
                );
                break;

            case "hug":
                usage(msg, "Hug", "hug <@user ...>",
                        "Gives the mentioned user a hug.",
                        "`<@user ...>` The user to hug (as mention). You can mention multiple users!",
                        "`none`"
                );
                break;

            case "pat":
                usage(msg, "Pat", "pat <@user ...>",
                        "Gives the mentioned user a pat.",
                        "`<@user ...>` The user to pat (as mention). You can mention multiple users!",
                        "`none`"
                );
                break;

            case "slap":
                usage(msg, "Slap", "slap <@user>",
                        "Slaps the mentioned user.",
                        "`<@user ...>` The user to slap (as mention). You can mention multiple users!",
                        "`none`"
                );
                break;

            case "lewd":
                usage(msg, "Lewd", "lewd [-slide]",
                        "Sends a lewd neko.\n" +
                        "Can only be used in NSFW-Channels.",
                        "`[-slide]` Creates a slideshow with 30 images",
                        "`none`"
                );
                break;

            case "prefix":
                usage(msg, "Prefix", "prefix [set <prefix>|reset]",
                        "Shows the currently used prefix in this Discord, if no argument is given.",
                        "`set <prefix>` Sets the prefix to the provided one.\n" +
                        "`reset` Resets the prefix to the default one.",
                        "`MANAGE_SERVER` for setting or resetting the prefix.");
                break;

            case "cuddle":
                usage(msg, "Cuddle", "cuddle <@user ...>",
                        "Cuddles the mentioned user.",
                        "`<@user ...>` The user to cuddle (as mention). You can mention multiple users!",
                        "`none`"
                );
                break;

            case "tickle":
                usage(e.getMessage(), "Tickle", "tickle <@user ...>",
                        "Tickles the mentioned user.",
                        "`<@user ...>` The user to tickle (as mention). You can mention multiple users!",
                        "`none`"
                );
                break;

            case "welcome":
                usage(e.getMessage(), "Welcome", "welcome [set <ChannelID>|reset|test]",
                        "Shows, sets or resets the Welcome-channel.",
                        "`set <ChannelID>` Sets a Channel as Welcome-Channel.\n" +
                        "`reset` Resets (removes) the welcome-channel.\n" +
                        "`test` Creates a welcome-image in the channel you currently are.",
                        "`MANAGE_SERVER`"
                );
                break;

            case "stats":
            case "stat":
                usage(e.getMessage(), "Stats", "stats",
                        "Shows some statistics of \\*Purr*",
                        "`none`",
                        "`none`"
                );
                break;

            case "kiss":
                usage(e.getMessage(), "Kiss", "kiss <@user ...>",
                        "Lets you kiss someone.",
                        "`<@user ...>` The user you want to kiss (as mention). You can mention multiple users!",
                        "`none`"
                );
                break;

            case "quote":
                usage(e.getMessage(), "Quote", "quote <messageID>",
                        "Lets you quote a message.",
                        "`<messageID>` The ID of the message to quote.",
                        "`none`"
                );
                break;

            case "img":
            case "image":
                usage(msg, "Img", "img <URL|neko:<name>.<ending>>",
                        "Will upload a image in the textchannel.",
                        "`<URL>` The URL of the image to post.\n" +
                        "`<neko:<name>.<ending>>` Get an image from the nekos.life-Database. Ending is .png/.jpg/...",
                        "`none`");
                break;

            case "debug":
                usage(msg, "Debug", "debug",
                        "Will create debug-files and post them on [debug.scarsz.me](https://debug.scarsz.me",
                        "`none`",
                        "`MANAGE_SERVER`");
                break;

            default:
                usage(msg);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {

    }

    @Override
    public String help() {
        return null;
    }
}
