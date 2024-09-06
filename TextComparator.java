
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextComparator {

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;

    public TextComparator() throws IOException {
        try (FileInputStream sentenceModelStream = new FileInputStream("en-sent.bin");
             FileInputStream tokenModelStream = new FileInputStream("en-token.bin")) {
            SentenceModel sentenceModel = new SentenceModel(sentenceModelStream);
            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);

            sentenceDetector = new SentenceDetectorME(sentenceModel);
            tokenizer = new TokenizerME(tokenModel);
        }
    }

    public double compareText(String text1, String text2) {
        // Tokenize and compare sentences
        String[] sentences1 = sentenceDetector.sentDetect(text1);
        String[] sentences2 = sentenceDetector.sentDetect(text2);

        List<String> allTokens1 = new ArrayList<>();
        List<String> allTokens2 = new ArrayList<>();

        for (String sentence : sentences1) {
            Span[] tokens = tokenizer.tokenizePos(sentence);
            for (Span token : tokens) {
                allTokens1.add(token.toString());
            }
        }

        for (String sentence : sentences2) {
            Span[] tokens = tokenizer.tokenizePos(sentence);
            for (Span token : tokens) {
                allTokens2.add(token.toString());
            }
        }

        // Calculate similarity (simple Jaccard Index for demo purposes)
        List<String> commonTokens = new ArrayList<>(allTokens1);
        commonTokens.retainAll(allTokens2);

        double jaccardIndex = (double) commonTokens.size() /
                (allTokens1.size() + allTokens2.size() - commonTokens.size());

        return jaccardIndex;
    }
}
