/**
* Main class handles command line argument parsing and initial call to the HtmlGrabber.
 */

public class CliRunner {

    static HtmlGrabber topLevelGrabber;
    static CliArgParsingState state;

    public static void main(String[] args) {

        if (args.length == 1 || args.length == 2) {
            CliRunner.topLevelGrabber = getGrabberByCliArguments(args);
        } else {
            handleUnexpectedParameters("Unexpected number of arguments!", CliArgParsingState.UNEXPECTED_NUMBER_OF_ARGS);
        }

    }

    /**
     * Print specified message along with usage information and exit the program.
     * @param message The message to be displayed.
     * @param state The state that should be set.
     */
    private static void handleUnexpectedParameters(String message, CliArgParsingState state) {
        CliRunner.state = state;

        System.out.println("Error while invoking jhg: " + message);
        System.out.println("Intended usage: java -jar jhg.jar <URL> <OPTIONAL: RECURSION_DEPTH; default value=2>");
        System.out.println("Examples: ");
        System.out.println("java -jar jhg.jar https://www.google.at 2");
        System.out.println("java -jar jhg.jar https://www.google.at\n");
    }

    /**
     * Instantiate the top-level HtmlGrabber by trying to parse CLI arguments.
     * @param args The argument-array.
     */
    private static HtmlGrabber getGrabberByCliArguments(String[] args) {
        int brokenLinkDepth;
        String url;
        HtmlGrabber instantiatedGrabber;

        try {
            url = args[0];
            brokenLinkDepth = args.length == 2 ? Integer.parseInt(args[1]) : HtmlGrabber.BROKEN_LINK_DEPTH_DEFAULT;
            instantiatedGrabber = new HtmlGrabber(url, brokenLinkDepth);
            CliRunner.state = CliArgParsingState.ARG_PARSING_SUCCESS;
        } catch(Exception e) {
            instantiatedGrabber = null;
            handleUnexpectedParameters("Second argument must be a number!", CliArgParsingState.ARG_PARSING_ERROR);
        }

        return instantiatedGrabber;
    }

    public static CliArgParsingState getState() {
        return CliRunner.state;
    }

    public static HtmlGrabber getGrabber() {
        return CliRunner.topLevelGrabber;
    }
}
