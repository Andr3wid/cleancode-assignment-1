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
        this.instantiateGrabberByUrl(TEST_URL_ENGLISH_SIMPLE);
    }

    private void instantiateGrabberByUrl(String url) {
        grabber = new HtmlGrabber(url);
        wcp = new WordCountProvider(grabber);
    }

    @Test
    void wordCountEnglishTextDepthZero() {
        grabber.setBrokenLinkDepth(0);
        final int wordsOnNginxDefaultPage = 43;
        Assertions.assertEquals(wordsOnNginxDefaultPage, wcp.getMetric());
    }

    @Test
    void wordCountEnglishTextDepthZero2() throws IOException {
        grabber = new HtmlGrabber("http://nginx.org/");
        grabber.setBrokenLinkDepth(0);
        wcp = new WordCountProvider(grabber);
        final int wordsOnNginxHomePage = 145;
        Assertions.assertEquals(wordsOnNginxHomePage, wcp.getMetric());
    }

    @Test
    void wordCountEnglishTextDepthZero3() throws IOException {
        grabber = new HtmlGrabber("http://nginx.com/");
        grabber.setBrokenLinkDepth(0);
        wcp = new WordCountProvider(grabber);
        final int wordsOnNginxHomePage = 981;
        Assertions.assertEquals(wordsOnNginxHomePage, wcp.getMetric());
    }

    @Test
    void wordCountGermanTextDepthZero() {
        grabber.setBrokenLinkDepth(0);
        final int wordsOnPage = 34;
        grabber = new HtmlGrabber(TEST_URL_GERMAN_SIMPLE);
        wcp = new WordCountProvider(grabber);
        Assertions.assertEquals(wordsOnPage, wcp.getMetric());
    }

    @Test
    void wordCountRecursive1() {
        grabber.setBrokenLinkDepth(1);
        int expectedWordCount = 145 + 981 + 43;
        Assertions.assertEquals(expectedWordCount, wcp.getMetric());
    }

}
