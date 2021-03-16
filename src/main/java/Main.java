/**
* Main class handles command line argument parsing and initial call to the HtmlGrabber.
 */

public class Main {

    static HtmlGrabber grabbedHtml;

    public static void main(String[] args) {

        if (args.length == 1) {
            Main.grabbedHtml = new HtmlGrabber(args[0]);
        } else if (args.length == 2) {
            getGrabberByCliArguments(args);
        } else {
            printUsageInfoOnError("Unexpected number of arguments!");
            System.exit(1);
        }

    }

    private static void printUsageInfoOnError(String message) {
        System.out.println("Error while invoking jhg: " + message);
        System.out.println("Intended usage: java -jar jhg.jar <URL> <OPTIONAL: RECURSION_DEPTH; default value=1>");
        System.out.println("Examples: ");
        System.out.println("java -jar jhg.jar https://www.google.at 2");
        System.out.println("java -jar jhg.jar https://www.google.at\n");
    }

    private static void getGrabberByCliArguments(String[] args) {
        int brokenLinkDepth;
        String url;

        try {
            url = args[0];
            brokenLinkDepth = Integer.parseInt(args[1]);
            Main.grabbedHtml = new HtmlGrabber(url, brokenLinkDepth);
        } catch(Exception e) {
            printUsageInfoOnError("Second argument must be a number!");
        }
    }

}
