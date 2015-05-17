package kclient.knuddels.tools.commands;

import kclient.knuddels.GroupChat;

/**
 *
 * @author SeBi
 */
public class SendPublicCommand implements Command {
    @Override
    public void parse(String cmd, String arg, GroupChat groupChat, String channel) {
        if (arg.contains(":")) {
            String toChannel = arg.substring(0, arg.indexOf(':'));
            String sendMessage = arg.substring(toChannel.length() + 1);
            groupChat.sendPublic(toChannel, sendMessage);
        }
        groupChat.refreshToolbar(channel);
    }
}
