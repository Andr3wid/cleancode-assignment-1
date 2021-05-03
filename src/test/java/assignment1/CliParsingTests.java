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
        WebpageAnalyzer.setCliState(CliParsingState.NOT_INITIALIZED);
    }

    @Test
    public void noCliArguments() {
        String[] args = {};
        WebpageAnalyzer.main(args);
        assertEquals(CliParsingState.ILLEGAL_ARGUMENT_COUNT, WebpageAnalyzer.getCliState());
    }

    @Test
    public void tooManyCliArguments() {
        String[] args = {simpleUrl, "4", "pizza"};
        WebpageAnalyzer.main(args);
        assertEquals(CliParsingState.ILLEGAL_ARGUMENT_COUNT, WebpageAnalyzer.getCliState());
    }

    @Test
    public void defaultRecursionDepth() {
        String[] args = {simpleUrl};
        WebpageAnalyzer.main(args);
        assertEquals(CliParsingState.PARSING_SUCCESS, WebpageAnalyzer.getCliState());
        assertEquals(WebpageAnalyzer.DEFAULT_RECURSION_DEPTH, WebpageAnalyzer.getRootPageAnalyzer().getMaxLinkDepth());
    }

    @Test
    public void customRecursionDepth() {
        int maxDepth = 4;

        String[] args = {simpleUrl, String.valueOf(maxDepth)};
        WebpageAnalyzer.main(args);

        assertEquals(CliParsingState.PARSING_SUCCESS, WebpageAnalyzer.getCliState());
        assertEquals(maxDepth, WebpageAnalyzer.getRootPageAnalyzer().getMaxLinkDepth());
    }

    @Test
    public void invalidDepth() {

        String[] args = {simpleUrl, "a"};
        WebpageAnalyzer.main(args);

        assertEquals(CliParsingState.ARGUMENT_PARSING_ERROR, WebpageAnalyzer.getCliState());
    }
}
