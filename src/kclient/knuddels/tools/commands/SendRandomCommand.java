package kclient.knuddels.tools.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import kclient.knuddels.GroupChat;
import kclient.tools.Logger;

/**
 *
 * @author SeBi
 */
public class SendRandomCommand implements Command {

    @Override
    public void parse(String cmd, String arg, GroupChat groupChat, String channel) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("data" + File.separator + "rndText.txt"), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");
        } catch (IOException e) {
            Logger.get().error(e);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                }
        }
        String[] args = arg.split(" ");
        if (args.length == 1) {
            try {
                int length = Integer.parseInt(args[0]);
                if (buffer.length() < length) {
                    return;
                }
                String rndText = buffer.substring(0, length);
                groupChat.sendPublic(channel, rndText);
            } catch (Exception e) {
            }
        } else if (args.length == 2) {
            try {
                int length = Integer.parseInt(args[0]);
                int wdh = Integer.parseInt(args[1]);
                if (buffer.length() < length) {
                    return;
                }
                String rndText = buffer.substring(0, length);
                for (int i = 0; i < wdh; i++) {
                    groupChat.sendPublicDelay(channel, rndText, i * 5000);
                }
            } catch (NumberFormatException e) {
            }
        } else if (args.length == 3) {
            try {
                int length = Integer.parseInt(args[0]);
                int wdh = Integer.parseInt(args[1]);
                int sleep = Integer.parseInt(args[2]);
                if (buffer.length() < length) {
                    return;
                }
                String rndText = buffer.substring(0, length);
                for (int i = 0; i < wdh; i++) {
                    groupChat.sendPublicDelay(channel, rndText, i * sleep);
                }
            } catch (NumberFormatException e) {
            }
        } else if (args.length == 5) {
            try {
                int length = Integer.parseInt(args[0]);
                int wdh = Integer.parseInt(args[1]);
                int sleep = Integer.parseInt(args[2]);
                String text = arg.substring(args[0].length() + args[1].length() + args[2].length() + 2);
                if (buffer.length() < length) {
                    return;
                }
                String rndText = buffer.substring(0, length);
                for (int i = 0; i < wdh; i++) {
                    groupChat.sendPublicDelay(channel, text + rndText, i * sleep);
                }
            } catch (NumberFormatException e) {
            }
        } else {
            groupChat.printBotMessage(channel, "_Verwendung:_#/sendrnd LENGTH#/sendrnd LENGTH WDH#/sendrnd LENGTH WDH SLEEP#/sendrnd LENGTH WDH SLEEP TEXT");
        }
    }
    
}
