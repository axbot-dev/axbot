package com.github.axiangcoding.axbot.app.bot.message;

public class QGContentMessage {
    public static String plain(String text) {
        return text;
    }

    public static String atUser(String userId) {
        return "<@%s>".formatted(userId);
    }

    public static String atEveryone() {
        return "@everyone";
    }

    public static String atChannelId(String channelId) {
        return "<#%s>".formatted(channelId);
    }

    public static String emoji(Integer id) {
        return "<emoji:%d>".formatted(id);
    }
}
