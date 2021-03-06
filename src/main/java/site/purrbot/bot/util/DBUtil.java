/*
 *  Copyright 2018 - 2021 Andre601
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *  
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 *  OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package site.purrbot.bot.util;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.utils.Types;
import site.purrbot.bot.PurrBot;

import java.util.Map;

public class DBUtil {

    private final PurrBot bot;

    private final RethinkDB r;
    private final Connection connection;

    private final String guildTable;

    public DBUtil(PurrBot bot){
        r = RethinkDB.r;
        connection = r.connection()
                .hostname(bot.getFileManager().getString("config", "database.ip"))
                .port(28015)
                .db(bot.getFileManager().getString("config", "database.name"))
                .connect();

        guildTable  = bot.getFileManager().getString("config", "database.guildTable");
        this.bot = bot;
    }

    /*
     *  Guild Stuff
     */
    private void checkValue(String id, String key, String def){
        Map<?,?> map = getGuild(id);

        if(map == null){
            addGuild(id);
            return;
        }

        if(map.get(key) == null)
            r.table(guildTable).get(id).update(r.hashMap(key, def)).run(connection);
    }

    public void addGuild(String id){
        r.table(guildTable).insert(
                r.array(
                        r.hashMap("id", id)
                         .with("language", "en")
                         .with("prefix", bot.isBeta() ? "p.." : "p.")
                         .with("welcome_background", "color_white")
                         .with("welcome_channel", "none")
                         .with("welcome_color", "hex:000000")
                         .with("welcome_icon", "purr")
                         .with("welcome_message", "Welcome {mention}!")
                )
        ).optArg("conflict", "update").run(connection);
    }

    public void delGuild(String id){
        Map<?,?> guild = getGuild(id);

        if(guild == null) 
            return;
        
        r.table(guildTable).get(id).delete().run(connection);
    }

    private Map<String,String> getGuild(String id){
        return r.table(guildTable)
                .get(id)
                .run(connection, Types.mapOf(String.class, String.class))
                .single();
    }
    
    /*
     * Language stuff
     */
    public String getLanguage(String id){
        checkValue(id, "language", "en");
        Map<String,String> guild = getGuild(id);
        
        return guild.get("language");
    }
    
    public void setLanguage(String id, String language){
        checkValue(id, "language", "en");
        
        r.table(guildTable).get(id).update(r.hashMap("language", language)).run(connection);
    }
    
    /*
     *  Prefix Stuff
     */
    public String getPrefix(String id){
        checkValue(id, "prefix", "p.");
        Map<String,String> guild = getGuild(id);

        return guild.get("prefix");
    }

    public void setPrefix(String id, String prefix){
        checkValue(id, "prefix", prefix);

        r.table(guildTable).get(id).update(r.hashMap("prefix", prefix)).run(connection);
    }

    /*
     *  Welcome Stuff
     */
    public String getWelcomeBg(String id){
        checkValue(id, "welcome_background", "color_white");
        Map<String,String> guild = getGuild(id);

        return guild.get("welcome_background");
    }

    public void setWelcomeBg(String id, String background){
        checkValue(id, "welcome_background", background);

        r.table(guildTable).get(id).update(r.hashMap("welcome_background", background)).run(connection);
    }

    public String getWelcomeChannel(String id){
        checkValue(id, "welcome_channel", "none");
        Map<String,String> guild = getGuild(id);

        return guild.get("welcome_channel");
    }

    public void setWelcomeChannel(String id, String channelID){
        checkValue(id, "welcome_channel", channelID);

        r.table(guildTable).get(id).update(r.hashMap("welcome_channel", channelID)).run(connection);
    }

    public String getWelcomeColor(String id){
        checkValue(id, "welcome_color", "hex:000000");
        Map<String,String> guild = getGuild(id);

        return guild.get("welcome_color");
    }

    public void setWelcomeColor(String id, String color){
        checkValue(id, "welcome_color", color);

        r.table(guildTable).get(id).update(r.hashMap("welcome_color", color)).run(connection);
    }

    public String getWelcomeIcon(String id){
        checkValue(id, "welcome_icon", "purr");
        Map<String,String> guild = getGuild(id);

        return guild.get("welcome_icon");
    }

    public void setWelcomeIcon(String id, String icon){
        checkValue(id, "welcome_icon", icon);

        r.table(guildTable).get(id).update(r.hashMap("welcome_icon", icon)).run(connection);
    }

    public String getWelcomeMsg(String id){
        checkValue(id, "welcome_message", "Welcome {mention}!");
        Map<String,String> guild = getGuild(id);

        return guild.get("welcome_message");
    }

    public void setWelcomeMsg(String id, String message){
        checkValue(id, "welcome_message", message);

        r.table(guildTable).get(id).update(r.hashMap("welcome_message", message)).run(connection);
    }
}
