package assignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CliParsingTests {

    String simpleUrl = "http://andref.xyz/nolinks.html";

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
        assertEquals(WebpageAnalyzer.DEFAULT_RECURSION_DEPTH ,WebpageAnalyzer.getRootPageAnalyzer().getMaxLinkDepth());
    }

    @Test
    public void customRecursionDepth() {
        int maxDepth = 4;

        String[] args = {simpleUrl, String.valueOf(maxDepth)};
        WebpageAnalyzer.main(args);

        assertEquals(CliParsingState.PARSING_SUCCESS, WebpageAnalyzer.getCliState());
        assertEquals(maxDepth, WebpageAnalyzer.getRootPageAnalyzer().getMaxLinkDepth());
    }
}
