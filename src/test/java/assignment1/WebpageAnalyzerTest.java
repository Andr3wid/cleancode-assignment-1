package assignment1;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WebpageAnalyzerTest {

    static String andrefURL = "http://andref.xyz";
    static String andref404URL = "http://andref.xyz/invalidpage/";
    static String nginxOrgURL = "http://nginx.org/";
    static String nginxComURL = "http://nginx.com/";

    Document andrefDoc;
    Document andref404Doc;
    Document nginxOrgDoc;
    Document nginxComDoc;

    HttpConnection mockedCon;
    HttpConnection mockedCon2;
    HttpConnection mockedCon3;
    HttpConnection mockedCon4;

    public WebpageAnalyzerTest() throws IOException {

        String andrefHTML = Files.readString(Path.of("src/test/resources/andref.html"));
        String andref404HTML = Files.readString(Path.of("src/test/resources/andref404.html"));
        String nginxOrgHTML = Files.readString(Path.of("src/test/resources/nginxorg.html"));
        String nginxComHTML = Files.readString(Path.of("src/test/resources/nginxcom.html"));

        andrefDoc = Parser.parse(andrefHTML, andrefURL);
        andref404Doc = Parser.parse(andref404HTML, andref404URL);
        nginxOrgDoc = Parser.parse(nginxOrgHTML, nginxOrgURL);
        nginxComDoc = Parser.parse(nginxComHTML, nginxComURL);

        mockedCon = mock(HttpConnection.class);
        mockedCon2 = mock(HttpConnection.class);
        mockedCon3 = mock(HttpConnection.class);
        mockedCon4 = mock(HttpConnection.class);

        when(Jsoup.connect(andrefURL)).thenReturn(mockedCon);
        when(Jsoup.connect(nginxOrgURL)).thenReturn(mockedCon2);
        when(Jsoup.connect(nginxComURL)).thenReturn(mockedCon3);
        when(Jsoup.connect(andref404URL)).thenReturn(mockedCon4);

        when(mockedCon2.get()).thenReturn(nginxOrgDoc);
        when(mockedCon3.get()).thenReturn(nginxComDoc);
        when(mockedCon4.get()).thenThrow(new HttpStatusException("", 404, andref404URL));
    }

    @BeforeAll
    private static void setupStaticMock() {
        MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class);
    }

    private void setupTestsuite() throws IOException {
        when(mockedCon.get()).thenReturn(andrefDoc);
    }

    private void setupTestsuite404() throws IOException {
        when(mockedCon.get()).thenReturn(andref404Doc);
    }

    @Test
    public void testSimple() throws IOException {
        setupTestsuite();
        WebpageAnalyzer webpageAnalyzer = new WebpageAnalyzer(andrefURL, 1);
        webpageAnalyzer.analyze();

        String expected = "[WebpageMetric{brokenLinks=[], linkCount=57, imageCount=1, videoCount=0, wordCount=124, url='http://nginx.org/'}, WebpageMetric{brokenLinks=[], linkCount=228, imageCount=62, videoCount=0, wordCount=1278, url='http://nginx.com/'}, WebpageMetric{brokenLinks=[], linkCount=2, imageCount=0, videoCount=0, wordCount=43, url='http://andref.xyz'}]";
        assertEquals(expected, webpageAnalyzer.getWebpageMetrics().toString());
    }

    @Test
    public void testSimple404() throws IOException {
        setupTestsuite404();
        WebpageAnalyzer webpageAnalyzer = new WebpageAnalyzer(andrefURL, 1);
        webpageAnalyzer.analyze();

        String expected = "[WebpageMetric{brokenLinks=[], linkCount=57, imageCount=1, videoCount=0, wordCount=124, url='http://nginx.org/'}, WebpageMetric{brokenLinks=[], linkCount=228, imageCount=62, videoCount=0, wordCount=1278, url='http://nginx.com/'}, WebpageMetric{brokenLinks=[http://andref.xyz/invalidpage/], linkCount=3, imageCount=0, videoCount=0, wordCount=43, url='http://andref.xyz'}]";
        assertEquals(expected, webpageAnalyzer.getWebpageMetrics().toString());
    }
}