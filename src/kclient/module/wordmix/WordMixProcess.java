package kclient.module.wordmix;

import java.util.Arrays;
import java.util.List;
import kclient.knuddels.GroupChat;
import kclient.knuddels.tools.toolbar.Button;
import kclient.module.wordmix.engines.BingEngine;
import kclient.module.wordmix.engines.GoogleEngine;
import kclient.module.wordmix.engines.WikipediaEngine;
import kclient.module.wordmix.engines.WordMixEngine;
import kclient.tools.Util;

/**
 *
 * @author SeBi
 */
public class WordMixProcess {
    private final List<WordMixEngine> engines;
    private final WordMixBot bot;
    private final GroupChat groupChat;
    private final String channel, kmix;
    private boolean running, containsNull;
    private int failCount;
    
    public WordMixProcess(WordMixBot bot, GroupChat groupChat, String channel, String kmix) {
        this.bot = bot;
        this.running = true;
        this.groupChat = groupChat;
        this.channel = channel;
        this.kmix = kmix;
        this.engines = Arrays.asList(new WordMixEngine[] {
            new GoogleEngine(this),
            new WikipediaEngine(this),
            new BingEngine(this)
        });
    }
    
    public static WordMixProcess start(WordMixBot bot, GroupChat groupChat, String channel, String kmix, List<String> words, final String mix, boolean containsNull) {
        final WordMixProcess req = new WordMixProcess(bot, groupChat, channel, kmix);
        req.running = true;
        req.containsNull = containsNull;
        groupChat.refreshToolbar(channel, new Button("Suche..."));
        for (final WordMixEngine eng : req.engines) {
            new Thread("WordMixRequest [" + eng.getName() + "]") {
                @Override
                public void run() {
                    eng.getAnswer(mix, words);
                }
            }.start();
        }
        return req;
    }
    
    public void setAnswer(String answer, WordMixEngine engine) {
        if (!this.running)
            return;
        answer = WordMixBot.buildAnswer(answer, this);
        System.out.println(engine.getName() + " - " + answer);
        if (!this.running)
            return;
        if (answer == null) {
            this.failCount++;
            if (this.failCount >= this.engines.size()) {
                this.bot.not_found++;
                this.groupChat.refreshToolbar(this.channel, new Button("Keine Antwort gefunden"));
                this.stop();
            }
            return;
        }
        if (this.running) {
            this.bot.found++;
            //this.groupChat.print(this.channel, String.format("°>{button} %s||call|/a %s|width|100|height|20<°", answer, answer));
            this.groupChat.refreshToolbar(this.channel, new Button(answer, String.format("/sendpublic %s:%s", channel, answer), true));
            if (this.bot.autoPlay) {
                this.groupChat.sendPublicDelay(this.channel, answer, Util.rnd(2000, 3000));
            }
            this.stop();
        }
    }
    
    public boolean containsNull() {
        return this.containsNull;
    }
    public String getMix() {
        return this.kmix;
    }
    public String getChannel() {
        return this.channel;
    }
    public GroupChat getGroupChat() {
        return this.groupChat;
    }
    
    public void stop() {
        this.running = false;
    }
    public boolean isRunning() {
        return this.running;
    }
}
