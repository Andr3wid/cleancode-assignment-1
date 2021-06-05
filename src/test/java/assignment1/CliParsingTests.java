package assignment1;

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

public class CliParsingTests {

    static MockedStatic<Jsoup> jsoupMockedStatic;

    String simpleUrl = "http://andref.xyz/nolinks.html";

    Document andrefDoc;

    HttpConnection mockedCon;

    @BeforeEach
    public void setup() throws IOException {

        String andrefNoLinksHTML = Files.readString(Path.of("src/test/resources/andrefNoLinks.html"));

        andrefDoc = Parser.parse(andrefNoLinksHTML, simpleUrl);

        mockedCon = mock(HttpConnection.class);

        when(Jsoup.connect(simpleUrl)).thenReturn(mockedCon);

        when(mockedCon.get()).thenReturn(andrefDoc);
    }

    @BeforeAll
    private static void setupStaticMock() {
        jsoupMockedStatic = mockStatic(Jsoup.class);

    }

    @AfterAll
    private static void teardownStaticMock() {
        jsoupMockedStatic.close();
    }

    @BeforeEach
    public void resetRootPageAnalyzer() {
        WebpageAnalyzerRunner.setCliState(CliParsingState.NOT_INITIALIZED);
    }

    @Test
    public void noCliArguments() {
        String[] args = {};
        WebpageAnalyzerRunner.main(args);
        assertEquals(CliParsingState.ILLEGAL_ARGUMENT_COUNT, WebpageAnalyzerRunner.getCliState());
    }

    @Test
    public void defaultRecursionDepth() {
        String[] args = {simpleUrl};
        WebpageAnalyzerRunner.main(args);
        assertEquals(CliParsingState.PARSING_SUCCESS, WebpageAnalyzerRunner.getCliState());
        assertEquals(WebpageAnalyzer.DEFAULT_RECURSION_DEPTH, WebpageAnalyzerRunner.getRootPages().get(0).getMaxLinkDepth());
    }

    @Test
    public void customRecursionDepth() {
        int maxDepth = 4;

        String[] args = {simpleUrl, String.valueOf(maxDepth)};
        WebpageAnalyzerRunner.main(args);

        assertEquals(CliParsingState.PARSING_SUCCESS, WebpageAnalyzerRunner.getCliState());
        assertEquals(maxDepth, WebpageAnalyzerRunner.getRootPages().get(0).getMaxLinkDepth());
    }

    @Test
    public void negativeRecursionDepth() {

        String[] args = {simpleUrl, "-1"};
        WebpageAnalyzerRunner.main(args);

        assertEquals(WebpageAnalyzer.DEFAULT_RECURSION_DEPTH ,WebpageAnalyzerRunner.getRootPages().get(0).getMaxLinkDepth());
    }

    @Test
    public void multipleUrlsNoDepth() {
        String[] args = {simpleUrl, simpleUrl};

        WebpageAnalyzerRunner.main(args);

        assertEquals(args.length, WebpageAnalyzerRunner.getRootPages().size());
    }

    @Test
    public void multipleUrlsCustomDepth() {
        int customDepth = 4;
        String[] args = {simpleUrl, simpleUrl, String.valueOf(customDepth)};

        WebpageAnalyzerRunner.main(args);

        assertEquals(args.length-1, WebpageAnalyzerRunner.getRootPages().size());

        for(WebpageAnalyzer wa : WebpageAnalyzerRunner.getRootPages()) {
            assertEquals(customDepth, wa.getMaxLinkDepth());
        }
    }
}
