// This class instantiates sentences.

import java.util.*;

public class Sentence {

    int N; // sentence # within corpus
    int numberWords; // # words in sentence
    int target; // target word #
    ArrayList<Word> word = new ArrayList<>(); // sentence = array of words

    // Add word to sentence:
    public void add(Word w) {
        word.add(w);
    }

    // Retreive word from sentence:
    public Word get(int N) {
        return word.get(N);
    }
}