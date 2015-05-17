package kclient.module.wordmix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kclient.Start;
import kclient.knuddels.GroupChat;
import kclient.knuddels.network.GameConnection;
import kclient.knuddels.network.generic.GenericProtocol;
import kclient.knuddels.tools.toolbar.Button;
import kclient.module.Module;
import kclient.module.ModuleBase;
import kclient.tools.Util;


/**
 *
 * @author SeBi
 */
public class WordMixBot extends ModuleBase implements Module {
    private final Map<String, WordMixProcess> processes;
    public int rounds, found, not_found, wins;
    public boolean autoPlay;

    public WordMixBot(GroupChat groupChat) {
        super(groupChat);
        super.state = true;
        this.processes = new HashMap<>();
    }
    
    private List<String> parseMix(String s) {
        List<String> mixxed = new ArrayList<>();
        String[] parts = s.split("\\(°B°");
        for (String string : parts) {
            if(string.contains("°°) "))
                string = string.substring(5);
            String w = WordMixBot.replace(string);
            if (w.length() > 0)
                mixxed.add(w);
        }
        return mixxed;
    }
    public static boolean containsWords(String str, List<String> words) {
        for (String word : words)
            if (!str.toLowerCase().contains(word.toLowerCase()))
                return false;
        return true;
    }
    public static String buildAnswer(String code, WordMixProcess p) {
        if (code == null)
            return null;
        Map<String, Integer> table = new HashMap();
        String[] awords = code.split(" ");
        String[] mwords = p.getMix().split(" ");
        for (String word : mwords) {
            table.put(word, table.size() + (p.containsNull() ? 0 : 1));
        }
        StringBuilder buffer = new StringBuilder();
        List<Integer> used = new ArrayList<>();
        for (String word : awords) {
            if (!table.containsKey(word))
                return null;
            int num = table.get(word);
            if (used.contains(num))
                continue;
            used.add(num);
            buffer.append(num);
        }
        if (buffer.toString().length() == mwords.length)
            return buffer.toString();
        return null;
    }
    public static String replace(String mix) {
        return mix.replace(".", "").replace(",", "")
                .replace("?", "").replace("!", "")
                .replace("\"", "").replace(":", "")
                .replace("-", "").replace("(", "")
                .replace(")", "").replace("  ", "");
    }

    @Override
    public String getAuthor() {
        return "°>_hSeBi|https://u-labs.de/members/sebi-2841/<°";
    }
    @Override
    public String getVersion() {
        return "1.0." + Start.REVISION;
    }
    @Override
    public String getDescription() {
        return "Das WordMix Module sucht für dich auf Google nach der richtigen Reihenfolge des Mix##"
                + "_Anleitung:_#In einen beliebigen WordMix Channel gehen und _" + this.groupChat.getButlerName() + " mix_ eingeben"
                + " alternativ auf den Button \"Neue Runde starten\" klicken.##Sobald " + this.groupChat.getButlerName() + " den Mix gesendet hat "
                + "sucht der Bot für dich nach der richtigen Reihenfolge, wenn er dies geschafft hat wird die Antwrot als ein Button in der Toolbar "
                + "angezeigt anderfalls wird \"Keine Antwort gefunden\" in der Toolbar angezeigt.";
    }
    @Override
    public String getName() {
        return "WordMix";
    }

    @Override
    public List<Button> getButtons(String channel) {
        if (!channel.toLowerCase().contains("wordmix"))
            return null;
        return Arrays.asList(new Button[] {
            new Button("WordMix", "py_" + (this.state ? "g" : "r") + ".gif", "/mdl " + (this.state ? "-" : "+") + getName(), false),
            new Button("Autoplay", "py_" + (this.autoPlay ? "g" : "r") + ".gif", "/mdl " + getName() + " autoplay:" + (this.autoPlay ? "false": "true"), false)
        });
    }

    @Override
    public String handleInput(String packet, String[] tokens) {
        if (tokens[0].equals("e")) {
            if (tokens[1].equals(this.groupChat.getButlerName()) &&
                    tokens[2].toLowerCase().contains("wordmix") &&
                    tokens[3].contains("(°B°1°°)"))
            {
                this.rounds++;
                if (this.processes.containsKey(tokens[2])) {
                    this.processes.get(tokens[2]).stop();
                    this.processes.remove(tokens[2]);
                }
                String mix = tokens[3].split("#_")[1].split("_")[0];
                List<String> words = this.parseMix(mix);
                boolean containsNull = mix.contains("°°0");
                StringBuilder buffer = new StringBuilder();
                for (String m : words)
                    buffer.append(m).append(" ");
                mix = WordMixBot.replace(buffer.toString());
                buffer = new StringBuilder();
                for (String m : mix.split(" ")) {
                    buffer.append("\"").append(m).append("\" ");
                }
                this.processes.put(tokens[2], WordMixProcess.start(this, this.groupChat, tokens[2], mix, words, buffer.toString(), containsNull));
            } else if (tokens[1].equals(this.groupChat.getButlerName())) {
                if (tokens[2].toLowerCase().contains("wordmix")) {
                    if (tokens[3].contains("°B°_") || (tokens[3].contains("Lösung") && tokens[3].contains("_"))) 
                    {
                        if (this.autoPlay)
                            this.groupChat.sendPublicDelay(tokens[2], "james mix", Util.rnd(1000, 2000));
                        
                        this.groupChat.refreshToolbar(tokens[2], new Button("Neue Runde starten", "/sendpublic " + tokens[2] + ":mix " + this.groupChat.getButlerName().toLowerCase(), true));
                    }
                }
            }
        }
        return packet;
    }

    @Override
    public String handleOutput(String packet, String[] tokens) {
        
        return packet;
    }

    @Override
    public GenericProtocol handleExtendInput(GameConnection connection, GenericProtocol protocol) {
        return protocol;
    }

    @Override
    public GenericProtocol handleExtendOutput(GameConnection connection, GenericProtocol protocol) {
        return protocol;
    }

    @Override
    public boolean handleCommand(String cmd, String arg, String channel) {
        String[] args = arg.split(":");
        if (args[0].equalsIgnoreCase("autoplay")) {
            this.autoPlay = Boolean.parseBoolean(args[1]);
            this.groupChat.printBotMessage(channel, String.format("WordMix AutoPlay gesetzt (%s)", this.autoPlay));
        }
        return true;
    }

    @Override
    public void save() {
    }

    @Override
    public void load() {
    }
}
