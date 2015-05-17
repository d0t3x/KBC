package kclient.knuddels.tools.commands;

import java.util.HashMap;
import java.util.Map;
import kclient.knuddels.GroupChat;

/**
 *
 * @author SeBi
 */
public class CommandParser {
    private static final Map<String, Command> commands;
    
    static {
        commands = new HashMap<>();
        commands.put("toolbar", new ToolbarCommand());
        commands.put("kclient", new ClientInfoCommand());
        commands.put("effect", new EffectCommand());
        commands.put("mdl", new ModuleCommand());
        commands.put("logger", new LoggerCommand());
        commands.put("sendpublic", new SendPublicCommand());
        commands.put("sendrnd", new SendRandomCommand());
    }
    
    public static boolean parse(GroupChat groupChat, String message, String channel) {
        String command = message.substring(1).split(" ")[0];
        String cmd = command.toLowerCase();
        String arg = "";
        if (message.length() > cmd.length() + 1) {
            arg = message.substring(message.indexOf(' ') + 1);
        }
        if (cmd.equals("p")) { //Custom Command implementation in Private Chat
            if (arg.contains(":")) {
                String msg = arg.substring(arg.indexOf(':') + 1);
                if (msg.charAt(0) == '/')
                    return CommandParser.parse(groupChat, msg, channel);
            }
        }
        
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            if (entry.getKey().toLowerCase().equals(cmd)) {
                entry.getValue().parse(cmd, arg, groupChat, channel);
                return true;
            }
        }
        return false;
    }
}
