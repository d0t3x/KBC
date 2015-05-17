package kclient.knuddels.tools.commands;

import kclient.knuddels.GroupChat;
import kclient.module.Module;
import kclient.module.ModuleBase;

/**
 *
 * @author SeBi
 */
public class ModuleCommand implements Command {
    @Override
    public void parse(String cmd, String arg, GroupChat groupChat, String channel) {
        if (arg.charAt(0) == '+') {
            String mdlName = arg.substring(1);
            Module mdl = groupChat.getModule(mdlName);
            if (mdl == null) {
                groupChat.printBotMessage(channel, "Das Module _" + mdlName + "_ existiert nicht!");
            } else {
                if (((ModuleBase)mdl).getState()) {
                    groupChat.printBotMessage(channel, "Das Module _" + mdl.getName() + "_ ist bereits aktiv!");
                } else {
                    groupChat.printBotMessage(channel, "Das Module _" + mdl.getName() + "_ wurde aktiviert.");
                    ((ModuleBase)mdl).setState(true);
                }
            }
        } else if (arg.charAt(0) == '-') {
            String mdlName = arg.substring(1);
            Module mdl = groupChat.getModule(mdlName);
            if (mdl == null) {
                groupChat.printBotMessage(channel, "Das Module _" + mdlName + "_ existiert nicht!");
            } else {
                if (!((ModuleBase)mdl).getState()) {
                    groupChat.printBotMessage(channel, "Das Module _" + mdl.getName() + "_ ist nicht aktiv!");
                } else {
                    groupChat.printBotMessage(channel, "Das Module _" + mdl.getName() + "_ wurde deaktiviert.");
                    ((ModuleBase)mdl).setState(false);
                }
            }
        } else if (arg.charAt(0) == 'r') {
            String mdlName = arg.substring(1);
            Module mdl = groupChat.getModule(mdlName);
            if (mdl == null) {
                groupChat.printBotMessage(channel, "Das Module _" + mdlName + "_ existiert nicht!");
            } else {
                boolean cState = ((ModuleBase)mdl).getState();
                //mdl.save();
                ((ModuleBase)mdl).setState(false);
                mdl.load();
                ((ModuleBase)mdl).setState(cState);
                groupChat.printBotMessage(channel, "Das Module _" + mdl.getName() + "_ wurde neugeladen!");
            }
        } else if (arg.charAt(0) == '?') {
            String mdlName = arg.substring(1);
            Module mdl = groupChat.getModule(mdlName);
            if (mdl == null) {
                groupChat.printBotMessage(channel, "Das Module _" + mdlName + "_ existiert nicht!");
            } else {
                groupChat.printBotMessage(channel, "_Beschriebung (" + mdl.getName() + ")_:#" + mdl.getDescription());
            }
        } else {
            String arg2 = "";
            if (arg.split(" ").length > 1) {
                arg2 = arg.substring(arg.indexOf(' ') + 1);
                arg = arg.split(" ")[0].toLowerCase();
            } else {
                groupChat.printBotMessage(channel, "_Verwendung:_ /mdl MODULE__NAME COMMAND PARAMS");
                return;
            }

            boolean exists = false;
            for (Module mdl : groupChat.getModule()) {
                if (arg.toLowerCase().equals(mdl.getName().toLowerCase())) {
                    if (mdl.handleCommand(arg, arg2, channel)) {
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists)
                groupChat.printBotMessage(channel, "Das Module _" + arg + "_ existiert nicht!");
        }
        groupChat.refreshToolbar(channel);
    }    
}
