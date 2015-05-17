package kclient.module.wordmix.engines;

import java.util.List;

/**
 *
 * @author SeBi
 */
public interface WordMixEngine {
    void getAnswer(String mix, List<String> words);
    String getName();
}
