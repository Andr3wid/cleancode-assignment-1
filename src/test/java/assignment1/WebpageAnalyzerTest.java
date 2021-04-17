package assignment1;

import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebpageAnalyzerTest {
    String andrefURL = "http://andref.xyz";
    String nginxOrgURL = "http://nginx.org/";
    String nginxComURL = "http://nginx.com/";
    String andrefHTML;
    String nginxOrgHTML;
    String nginxComHTML;

    public WebpageAnalyzerTest() throws IOException {
        andrefHTML = Files.readString(Path.of("src/test/resources/andref.html"));
        nginxOrgHTML = Files.readString(Path.of("src/test/resources/nginxorg.html"));
        nginxComHTML = Files.readString(Path.of("src/test/resources/nginxcom.html"));
    }

    @Test
    public void testMe() throws IOException {
        HttpConnection mockedCon = mock(HttpConnection.class);

        when(mockedCon.url(Mockito.anyString())).thenCallRealMethod();

        when(mockedCon.get()).thenReturn(Parser.parse(andrefHTML, andrefURL));
        when(mockedCon.get()).thenReturn(Parser.parse(nginxOrgHTML, nginxOrgURL));
        when(mockedCon.get()).thenReturn(Parser.parse(andrefHTML, nginxComURL));

        WebpageAnalyzer webpageAnalyzer = new WebpageAnalyzer(andrefURL, 1);
        webpageAnalyzer.analyze();

        String expected = "[WebpageMetric{brokenLinks=[], linkCount=57, imageCount=1, videoCount=0, wordCount=124, url='http://nginx.org/'}, WebpageMetric{brokenLinks=[], linkCount=228, imageCount=62, videoCount=0, wordCount=1278, url='http://nginx.com/'}, WebpageMetric{brokenLinks=[], linkCount=2, imageCount=0, videoCount=0, wordCount=43, url='http://andref.xyz'}]";
        assertEquals(expected, webpageAnalyzer.getWebpageMetrics().toString());
    }
}