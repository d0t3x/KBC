package kclient.knuddels.tools.commands;

import kclient.knuddels.GroupChat;

/**
 *
 * @author SeBi
 */
public class ToolbarCommand implements Command {
    @Override
    public void parse(String cmd, String arg, GroupChat groupChat, String channel) {
        groupChat.toggleToolbar();
    }
}
