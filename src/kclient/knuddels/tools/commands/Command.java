package kclient.knuddels.tools.commands;

import kclient.knuddels.GroupChat;

/**
 *
 * @author SeBi
 */
public interface Command {
    void parse(String cmd, String arg, GroupChat groupChat, String channel);
}
