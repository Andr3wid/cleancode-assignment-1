import assignment1.HtmlGrabber;
import assignment1.metrics.MetricProvider;
import assignment1.metrics.WordCountProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class WordCountTests {

    // TODO: refactor tests to be beautiful; test shallow (depth = 0) and recursive steps

    HtmlGrabber grabber;
    MetricProvider wordCountMetric;
    final String TEST_URL_ENGLISH_SIMPLE = "http://andref.xyz/";
    final String TEST_URL_GERMAN_SIMPLE = "http://andref.xyz/german.html";

    @BeforeEach
    void setupValidGrabber() {
        this.instantiateGrabberByUrl(TEST_URL_ENGLISH_SIMPLE);
    }

    private void instantiateGrabberByUrl(String url) {
        grabber = new HtmlGrabber(url);
        wordCountMetric = new MetricProvider(grabber, new WordCountProvider());
    }

    @Test
    void wordCountEnglishTextDepthZero() {
        grabber.setBrokenLinkDepth(0);
        final int wordsOnNginxDefaultPage = 43;
        Assertions.assertEquals(wordsOnNginxDefaultPage, wordCountMetric.getMetric());
    }

    @Test
    void wordCountEnglishTextDepthZero2() throws IOException {
        instantiateGrabberByUrl("http://nginx.org/");
        grabber.setBrokenLinkDepth(0);
        final int wordsOnNginxHomePage = 145;
        Assertions.assertEquals(wordsOnNginxHomePage, wordCountMetric.getMetric());
    }

    @Test
    void wordCountEnglishTextDepthZero3() throws IOException {
        instantiateGrabberByUrl("http://nginx.com/");
        grabber.setBrokenLinkDepth(0);
        final int wordsOnNginxHomePage = 981;
        Assertions.assertEquals(wordsOnNginxHomePage, wordCountMetric.getMetric());
    }

    @Test
    void wordCountGermanTextDepthZero() {
        instantiateGrabberByUrl(TEST_URL_GERMAN_SIMPLE);
        grabber.setBrokenLinkDepth(0);
        final int wordsOnPage = 34;
        Assertions.assertEquals(wordsOnPage, wordCountMetric.getMetric());
    }

    @Test
    void wordCountRecursive1() {
        grabber.setBrokenLinkDepth(1);
        int expectedWordCount = 145 + 981 + 43;
        Assertions.assertEquals(expectedWordCount, wordCountMetric.getMetric());
    }

}
