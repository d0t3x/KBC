package kclient.module.wordmix.engines;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import kclient.module.wordmix.WordMixBot;
import kclient.module.wordmix.WordMixProcess;
import kclient.module.wordmix.WordMixRequest;
import kclient.tools.Logger;
/**
 *
 * @author SeBi
 */
public class GoogleEngine implements WordMixEngine {
    private final WordMixProcess process;
    
    public GoogleEngine(WordMixProcess p) {
        this.process = p;
    }
    
    @Override
    public void getAnswer(String mix, List<String> words) {
        String input = makeRequest(mix).replace("> ", ">").replace(" <", "<");
        
        boolean found = false;
        String[] tmpBold = input.split("<b>");
        for (String xBold : tmpBold) {
            if (found)
                break;
            String[] arrData = xBold.split("</b>");
            for (String arr : arrData) {
                arr = arr.replace("> ", ">").replaceAll("<[^>]+>", "");
                if (arr.contains(mix))
                    continue;
                if (WordMixBot.containsWords(arr, words)) {
                    this.process.setAnswer(arr, this);
                    found = true;
                    break;
                }
            }
        }
        if (!found)
            this.process.setAnswer(null, this);
    }
    @Override
    public String getName() {
        return "Google";
    }
    
    private String makeRequest(String mix) {
        try {
            WordMixRequest r = new WordMixRequest(this, this.process, "http://www.google.de/search", 
                    "hl=&num=100&q=" + URLEncoder.encode(mix, "UTF-8") + "&btnG=Google-Suche&meta=");
            
            return r.make();
        } catch (UnsupportedEncodingException ex) {
            Logger.get().error(ex);
        }
        return null;
    }
}
