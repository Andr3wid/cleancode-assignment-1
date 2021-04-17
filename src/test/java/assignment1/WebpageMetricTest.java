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
        WebpageMetric webpageMetric = new WebpageMetric(andrefDoc, andrefURL);
        String expected = "WebpageMetric{brokenLinks=[], linkCount=0, imageCount=0, videoCount=0, wordCount=43, url='http://andref.xyz'}";
        assertEquals(expected, webpageMetric.toString());
    }

    @Test
    public void testNginxCom() {
        WebpageMetric webpageMetric = new WebpageMetric(nginxComDoc, nginxComURL);
        String expected = "WebpageMetric{brokenLinks=[], linkCount=0, imageCount=62, videoCount=0, wordCount=1278, url='http://nginx.com/'}";
        assertEquals(expected, webpageMetric.toString());
    }
}