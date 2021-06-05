package assignment1;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebpageAnalyzerRunner {
    /**
     * Represents the current state of CLI parsing; mainly used for testability
     */
    private static CliParsingState cliState = CliParsingState.NOT_INITIALIZED;
    private static final StringBuilder reportConcatenator = new StringBuilder();

    private static final int MAX_THREADS = 10;
    private static final int MAX_MIN_PER_PAGE = 5;

    /**
     * Root page analyzer provided by the URL entered as CLI argument
     */
    private static ArrayList<WebpageAnalyzer> pages = new ArrayList<>();

    public static void main(String[] args) {
        // parse CLI arguments
        try {
            pages = parseCliArguments(args);
            cliState = CliParsingState.PARSING_SUCCESS;
        } catch (Exception e) {
            printUsageTextOnError(e.getMessage());
        }

        ExecutorService pool = startConcurrentCrawling(pages, MAX_THREADS);
        mergeReports(pool);
    }

    public static ExecutorService startConcurrentCrawling(ArrayList<WebpageAnalyzer> pagesToAnalyze, int maxThreads) {
        ExecutorService pool = Executors.newFixedThreadPool(maxThreads);

        // analyze the given page(s)
        try {
            for(WebpageAnalyzer analyzer : pagesToAnalyze) {
                pool.execute(analyzer);
            }
        } catch (Exception e) {
            System.out.println("Provided url is not reachable : " + e.getMessage());
        }

        pool.shutdown();

        return pool;
    }

    public static void mergeReports(ExecutorService pool) {

        // write the collected content into report-file
        try {
            pool.awaitTermination((long) pages.size() * MAX_MIN_PER_PAGE, TimeUnit.MINUTES);
            for(WebpageAnalyzer analyzer : pages) {
                reportConcatenator.append("--- ").append(analyzer.getUrl().toString()).append("\n");
                reportConcatenator.append(analyzer.getReport().getReportContent()).append("\n");
            }

            WebpageAnalyzerReport finalReport = new WebpageAnalyzerReport();
            finalReport.addToReport(reportConcatenator.toString());
            finalReport.writeReport();

        } catch (Exception e) {
            System.out.println("Error while trying to write report-file.");
        }
    }

    public static ArrayList<WebpageAnalyzer> getRootPages() {
        return pages;
    }

    private static void printUsageTextOnError(String message) {
        System.out.println("Error while parsing CLI arguments: " + message);
        printUsageInfo();
    }

    private static void printUsageInfo() {
        System.out.println("Usage: java -jar crawler.jar <URL1> [<URL2> ... <URLn>] <RECURSION_DEPTH (optional; default=2)>");
        System.out.println("Examples:");
        System.out.println("\t * java -jar crawler.jar https://www.google.at");
        System.out.println("\t * java -jar crawler.jar https://www.google.at 3");
        System.out.println("\t * java -jar crawler.jar https://www.google.at https://duckduckgo.com 3");
    }


    /**
     * Checks if CLI arguments are well formed and given in the expected order.
     *
     * @param args The parameter array to check
     * @return constructed WebpageAnalyzer
     * @throws IllegalArgumentException thrown if malformed CLI arguments are found
     */
    private static ArrayList<WebpageAnalyzer> parseCliArguments(String[] args) throws IllegalArgumentException, MalformedURLException {
        int upperBound = args.length-1;
        int recursionDepth;
        ArrayList<WebpageAnalyzer> pages = new ArrayList<>();

        if (args.length < 1) {
            cliState = CliParsingState.ILLEGAL_ARGUMENT_COUNT;
            throw new IllegalArgumentException("Illegal argument count!");
        }

        try {
            recursionDepth = Integer.parseInt(args[args.length-1]);
            upperBound--;
            if(recursionDepth < 0) throw new IllegalArgumentException("Recursion depth must not be negative! using default value instead.");
        } catch (Exception e) {
            recursionDepth = WebpageAnalyzer.DEFAULT_RECURSION_DEPTH;
        }

        for(int i = 0; i <= upperBound; i++) {
            pages.add(new WebpageAnalyzer(args[i], recursionDepth));
        }
        return pages;
    }

    public static CliParsingState getCliState() {
        return cliState;
    }

    public static void setCliState(CliParsingState cliState) {
        WebpageAnalyzerRunner.cliState = cliState;
    }


}
