package kclient.knuddels.tools.commands;

import kclient.knuddels.GroupChat;
import kclient.knuddels.tools.PacketBuilder;

/**
 *
 * @author SeBi
 */
public class EffectCommand implements Command {
    @Override
    public void parse(String cmd, String arg, GroupChat groupChat, String channel) {
        if (arg.charAt(0) == '+' || arg.charAt(0) == '-') {
            String action = arg.charAt(0) == '+' ? "a" : "r";
            String effect = arg.substring(1).split(" ")[0];
            String nick = groupChat.getNickname();
            if (arg.length() > effect.length() + 1)
                nick = arg.substring(arg.indexOf(' ') + 1);

            PacketBuilder buffer = new PacketBuilder("4");
            buffer.writeNull(); buffer.writeString(action);
            buffer.writeNull(); buffer.writeString(nick);
            buffer.writeNull(); buffer.writeString(effect);
            groupChat.receive(buffer.toString());
        }
    }
    
}
