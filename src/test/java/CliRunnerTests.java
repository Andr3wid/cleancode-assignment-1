import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CliRunnerTests {
    private final String VALID_TEST_URL = "https://www.google.at";

    @Test
    void singleValidCliArgument() {
        String[] args = {VALID_TEST_URL};
        CliRunner.main(args);

        HtmlGrabber mainGrabber = CliRunner.getGrabber();

        Assertions.assertEquals(VALID_TEST_URL, mainGrabber.getUrl());
        Assertions.assertEquals(HtmlGrabber.BROKEN_LINK_DEPTH_DEFAULT, mainGrabber.getBrokenLinkDepth());
        Assertions.assertEquals(CliArgParsingState.ARG_PARSING_SUCCESS, CliRunner.getState());
    }

    @Test
    void twoValidCliArguments() {
        int depth = 5;
        String[] args = {VALID_TEST_URL, Integer.toString(depth)};
        CliRunner.main(args);

        HtmlGrabber mainGrabber = CliRunner.getGrabber();

        Assertions.assertEquals(VALID_TEST_URL, mainGrabber.getUrl());
        Assertions.assertEquals(depth, mainGrabber.getBrokenLinkDepth());
        Assertions.assertEquals(CliArgParsingState.ARG_PARSING_SUCCESS, CliRunner.getState());
    }

    @Test
    void noCliArguments() {
        String[] args = {};
        CliRunner.main(args);

        Assertions.assertNull(CliRunner.getGrabber());
        Assertions.assertEquals(CliArgParsingState.UNEXPECTED_NUMBER_OF_ARGS, CliRunner.getState());
    }

    @Test
    void tooManyCliArguments() {
        String[] args = {VALID_TEST_URL, "100", "1000"};
        CliRunner.main(args);

        Assertions.assertNull(CliRunner.getGrabber());
        Assertions.assertEquals(CliArgParsingState.UNEXPECTED_NUMBER_OF_ARGS, CliRunner.getState());
    }

    @Test
    void twoInvalidCliArguments() {
        String[] args = {VALID_TEST_URL, "i_am_not_a_number"};
        CliRunner.main(args);

        Assertions.assertNull(CliRunner.getGrabber());
        Assertions.assertEquals(CliArgParsingState.ARG_PARSING_ERROR, CliRunner.getState());
    }
}
