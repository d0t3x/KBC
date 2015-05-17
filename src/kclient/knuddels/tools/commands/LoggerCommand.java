package kclient.knuddels.tools.commands;

import kclient.knuddels.GroupChat;
import kclient.tools.Logger;

/**
 *
 * @author SeBi
 */
public class LoggerCommand implements Command {
    @Override
    public void parse(String cmd, String arg, GroupChat groupChat, String channel) {
        if (arg.isEmpty()) {
            groupChat.printBotMessage(channel, "LogLevel angeben! _Beispiel_: /logger [Error, Info, Debug, All, None]");
        } else {
            try {
                Logger.Level level = Logger.Level.valueOf(arg.toUpperCase());
                Logger.get().setLevel(level);
                groupChat.printBotMessage(channel, "LogLevel auf _" + level.name() + "_ gesetzt!");
            } catch (IllegalArgumentException e) {
                Logger.get().error(e);
                groupChat.printBotMessage(channel, "LogLevel angeben! _Beispiel_: /logger [Error, Info, Debug, All, None]");
            }
        }
    }
    
}
