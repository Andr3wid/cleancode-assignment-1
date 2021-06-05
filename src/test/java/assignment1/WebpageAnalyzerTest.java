package assignment1;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WebpageAnalyzerTest {

    static MockedStatic<Jsoup> jsoupMockedStatic;

    String andrefURL = "http://andref.xyz";
    String andref404URL = "http://andref.xyz/invalidpage/";
    String nginxOrgURL = "http://nginx.org/";
    String nginxComURL = "http://nginx.com/";

    Document andrefDoc;
    Document andref404Doc;
    Document nginxOrgDoc;
    Document nginxComDoc;

    HttpConnection mockedAndref;
    HttpConnection mockNginxOrg;
    HttpConnection mockNginxCom;
    HttpConnection mockAndref404;

    @BeforeEach
    public void setup() throws IOException {

        String andrefHTML = Files.readString(Path.of("src/test/resources/andref.html"));
        String andref404HTML = Files.readString(Path.of("src/test/resources/andref404.html"));
        String nginxOrgHTML = Files.readString(Path.of("src/test/resources/nginxorg.html"));
        String nginxComHTML = Files.readString(Path.of("src/test/resources/nginxcom.html"));

        andrefDoc = Parser.parse(andrefHTML, andrefURL);
        andref404Doc = Parser.parse(andref404HTML, andrefURL);
        nginxOrgDoc = Parser.parse(nginxOrgHTML, nginxOrgURL);
        nginxComDoc = Parser.parse(nginxComHTML, nginxComURL);

        mockedAndref = mock(HttpConnection.class);
        mockNginxOrg = mock(HttpConnection.class);
        mockNginxCom = mock(HttpConnection.class);
        mockAndref404 = mock(HttpConnection.class);

        when(Jsoup.connect(andrefURL)).thenReturn(mockedAndref);
        when(Jsoup.connect(nginxOrgURL)).thenReturn(mockNginxOrg);
        when(Jsoup.connect(nginxComURL)).thenReturn(mockNginxCom);
        when(Jsoup.connect(andref404URL)).thenReturn(mockAndref404);

        when(mockNginxOrg.get()).thenReturn(nginxOrgDoc);
        when(mockNginxCom.get()).thenReturn(nginxComDoc);
        when(mockAndref404.get()).thenThrow(new HttpStatusException("", 404, andref404URL));
    }

    @BeforeAll
    private static void setupStaticMock() {
        jsoupMockedStatic = mockStatic(Jsoup.class);
    }

    @AfterAll
    private static void teardownStaticMock() {
        jsoupMockedStatic.close();
    }

    private void setupTestsuite() throws IOException {
        when(mockedAndref.get()).thenReturn(andrefDoc);
    }

    private void setupTestsuite404() throws IOException {
        when(mockedAndref.get()).thenReturn(andref404Doc);
    }

    @Test
    public void testSimple() throws IOException {
        setupTestsuite();
        WebpageAnalyzer webpageAnalyzer = new WebpageAnalyzer(andrefURL, 1);
        webpageAnalyzer.analyze();

        String expected = "[WebpageMetric{url=http://nginx.org/, brokenLinks=[], linkCount=57, imageCount=1, videoCount=0, wordCount=124}\n" +
                ", WebpageMetric{url=http://nginx.com/, brokenLinks=[], linkCount=228, imageCount=62, videoCount=0, wordCount=1278}\n" +
                ", WebpageMetric{url=http://andref.xyz, brokenLinks=[], linkCount=2, imageCount=0, videoCount=0, wordCount=43}\n]";
        assertEquals(expected, webpageAnalyzer.getWebpageMetrics().toString());
    }

    @Test
    public void testSimple404() throws IOException {
        setupTestsuite404();
        WebpageAnalyzer webpageAnalyzer = new WebpageAnalyzer(andrefURL, 1);
        webpageAnalyzer.analyze();

        String expected = "[WebpageMetric{url=http://nginx.org/, brokenLinks=[], linkCount=57, imageCount=1, videoCount=0, wordCount=124}\n" +
                ", WebpageMetric{url=http://nginx.com/, brokenLinks=[], linkCount=228, imageCount=62, videoCount=0, wordCount=1278}\n" +
                ", WebpageMetric{url=http://andref.xyz, brokenLinks=[http://andref.xyz/invalidpage/], linkCount=3, imageCount=0, videoCount=0, wordCount=43}\n]";
        assertEquals(expected, webpageAnalyzer.getWebpageMetrics().toString());
    }
}