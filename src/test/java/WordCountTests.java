import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class WordCountTests {

    // TODO: refactor tests to be beautiful; test shallow (depth = 0) and recursive steps

    HtmlGrabber grabber;
    WordCountProvider wcp;
    final String TEST_URL_ENGLISH_SIMPLE = "http://andref.xyz/";
    final String TEST_URL_GERMAN_SIMPLE = "http://andref.xyz/german.html";

    @BeforeEach
    void setupValidGrabber() {
        try {
            grabber = new HtmlGrabber(TEST_URL_ENGLISH_SIMPLE);
            wcp = new WordCountProvider(grabber);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWordCountEnglishText() {
        final int wordsOnNginxDefaultPage = 43;
        Assertions.assertEquals(wordsOnNginxDefaultPage, wcp.getMetric());
    }

    @Test
    void testWordCountGermanText() {
        // TODO: wordcount-method obviously treats german 'äöü' not as alphanumeric chars; fix that
        final int wordsOnPage = 34;
        try {
            grabber = new HtmlGrabber(TEST_URL_GERMAN_SIMPLE);
            wcp = new WordCountProvider(grabber);
            Assertions.assertEquals(wordsOnPage, wcp.getMetric());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
