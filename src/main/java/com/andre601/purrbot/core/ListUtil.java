package com.andre601.purrbot.core;

import com.andre601.purrbot.util.HttpUtil;

import java.util.Collections;

public class ListUtil {

    public static void refreshBlackList(){
        Collections.addAll(PurrBot.getGuildBlacklist(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/BlacklistedGuilds"
        ).split("\n"));
    }

    public static void refreshRandomImages(){
        Collections.addAll(PurrBot.getRandomKissImg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomKissImage"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomShutdownImg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomShutdownImage"
        ).split("\n"));
        Collections.addAll(PurrBot.getImages(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/Andre601/PurrBot-files/master/files/welcome-images"
        ).split("\n"));
    }

    public static void refreshRandomMessages(){
        Collections.addAll(PurrBot.getRandomStartupMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/Andre601/PurrBot-files/master/files/RandomStartupMsg"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomShutdownMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomShutdownText"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomFacts(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomFact"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomNoNSFWMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomNoNSFWMsg"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomDebugMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomDebugMsg"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomAPIPingMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomAPIPingMsg"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomPingMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomPingMsg"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomAcceptFuckMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomAcceptFuckMsg"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomDenyFuckMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomDenyFuckMsg"
        ).split("\n"));
        Collections.addAll(PurrBot.getRandomAcceptFuckMsg(), HttpUtil.requestHttp(
                "https://raw.githubusercontent.com/andre601/purrbot-files/master/files/RandomAcceptFuckMsg"
        ).split("\n"));
    }

    public static void clear(){
        PurrBot.getRandomStartupMsg().clear();
        PurrBot.getRandomShutdownImg().clear();
        PurrBot.getRandomShutdownMsg().clear();
        PurrBot.getRandomFacts().clear();
        PurrBot.getRandomNoNSFWMsg().clear();
        PurrBot.getRandomDebugMsg().clear();
        PurrBot.getRandomAPIPingMsg().clear();
        PurrBot.getRandomPingMsg().clear();
        PurrBot.getRandomKissImg().clear();
        PurrBot.getRandomAcceptFuckMsg().clear();
        PurrBot.getRandomDenyFuckMsg().clear();
        PurrBot.getImages().clear();
    }
}