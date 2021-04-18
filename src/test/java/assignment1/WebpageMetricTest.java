package assignment1;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebpageMetricTest {
    static String andrefURL = "http://andref.xyz";
    static String nginxComURL = "http://nginx.com/";

    Document andrefDoc;
    Document nginxComDoc;

    public WebpageMetricTest() throws IOException {

        String andrefHTML = Files.readString(Path.of("src/test/resources/andref.html"));
        String nginxComHTML = Files.readString(Path.of("src/test/resources/nginxcom.html"));

        andrefDoc = Parser.parse(andrefHTML, andrefURL);
        nginxComDoc = Parser.parse(nginxComHTML, nginxComURL);
    }

    @Test
    public void testAndref() {
        WebpageMetric webpageMetric = new WebpageMetric(andrefDoc);
        String expected = "WebpageMetric{url=http://andref.xyz, brokenLinks=[], linkCount=0, imageCount=0, videoCount=0, wordCount=43}\n";
        assertEquals(expected, webpageMetric.toString());
    }

    @Test
    public void testNginxCom() {
        WebpageMetric webpageMetric = new WebpageMetric(nginxComDoc);
        String expected = "WebpageMetric{url=http://nginx.com/, brokenLinks=[], linkCount=0, imageCount=62, videoCount=0, wordCount=1278}\n";
        assertEquals(expected, webpageMetric.toString());
    }

    @Test
    public void testRemoveTitleFormDoc() {
        WebpageMetric webpageMetric = new WebpageMetric(andrefDoc);
        Document strippedDoc = webpageMetric.removeHtmlElementsBySelector(andrefDoc, "title");
        assertEquals(0, strippedDoc.select("title").size());
    }

    @Test
    public void testPreprocessTextEN() {
        WebpageMetric webpageMetric = new WebpageMetric(andrefDoc);
        String preprocessedString = webpageMetric.preProcessText("   This    is    a     test     string.   ");
        assertEquals("This is a test string", preprocessedString);
    }

    @Test
    public void testPreprocessTextNonEN() {
        WebpageMetric webpageMetric = new WebpageMetric(andrefDoc);
        String preprocessedString = webpageMetric.preProcessText("   This is a     test string with ǯ Ì¾ » ì    non EN characters");
        assertEquals("This is a test string with non EN characters", preprocessedString);
    }
}