package assignment1;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebpageAnalyzer {

    /**
     * Default recursion depth if not stated otherwise (via CLI args)
     */
    public static final int DEFAULT_RECURSION_DEPTH = 2;

    /**
     * URL to be analyzed
     */
    private final URL url;

    /**
     * Specifies the level of recursion to follow links on the {@link this.url}
     */
    private final int maxLinkDepth;

    /**
     * List for the Metrics of all analyzed webpages
     */
    private final List<WebpageMetric> webpageMetrics;

    /**
     * Root page analyzer provided by the URL entered as CLI argument
     */
    private static WebpageAnalyzer rootPage;

    /**
     * Represents the current state of CLI parsing; mainly used for testability
     */
    private static CliParsingState cliState = CliParsingState.NOT_INITIALIZED;


    public WebpageAnalyzer(String url, int linkDepth) throws MalformedURLException {
        this.maxLinkDepth = linkDepth;
        webpageMetrics = new ArrayList<>();
        this.url = new URL(url);
    }

    public WebpageAnalyzer(String url) throws MalformedURLException {
        this.maxLinkDepth = 2;
        webpageMetrics = new ArrayList<>();
        this.url = new URL(url);
    }

    public static void main(String[] args) {
        // parse CLI arguments
        try {
            rootPage = parseCliArguments(args);
            cliState = CliParsingState.PARSING_SUCCESS;
        } catch (Exception e) {
            printUsageTextOnError(e.getMessage());
        }

        try {
            if (rootPage != null) {
                rootPage.analyze();
            }
        } catch (IOException e) {
            System.out.println("Provided url is not reachable : " + e.getMessage());
        }
    }

    /**
     * Starts the recursive analyze process of the webpage
     *
     * @throws IOException see {@link Connection#get()}
     */
    public void analyze() throws IOException {
        Document document = Jsoup.connect(url.toString()).get();
        analyze(url.toString(), document, maxLinkDepth);
    }

    private void analyze(String documentUrl, Document document, int linkDepth) {
        WebpageMetric webpageMetric = new WebpageMetric(document, documentUrl);

        Elements links = document.select("a");
        webpageMetric.setLinkCount(links.size());

        if (linkDepth != 0) {
            System.out.println("links to visit: " + links.size());
            for (int i = 0; i < links.size(); i++) {
                Element link = links.get(i);
                try {
                    URL urlsn = new URL(link.attr("href"));
                    System.out.println(linkDepth + " : " + (i + 1) + "/" + links.size() + " : " + urlsn);
                    try {
                        Document linkDocument = Jsoup.connect(urlsn.toString()).get();
                        analyze(urlsn.toString(), linkDocument, linkDepth - 1);
                    } catch (HttpStatusException e) {
                        if (e.getStatusCode() == 404) {
                            webpageMetric.addBrokenLink(urlsn.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException ignored) {
                    // ignore non conform URLs
                }
            }
        }
        webpageMetrics.add(webpageMetric);
    }

    /**
     * Checks if CLI arguments are well formed and given in the expected order.
     *
     * @param args The parameter array to check
     * @return constructed WebpageAnalyzer
     * @throws IllegalArgumentException thrown if malformed CLI arguments are found
     */
    private static WebpageAnalyzer parseCliArguments(String[] args) throws IllegalArgumentException, MalformedURLException {
        String url;
        int recursionDepth;

        if (args.length < 1 || args.length > 2) {
            WebpageAnalyzer.cliState = CliParsingState.ILLEGAL_ARGUMENT_COUNT;
            throw new IllegalArgumentException("Illegal argument count!");
        }

        url = args[0];

        if (args.length == 1) {
            return new WebpageAnalyzer(url);
        } else {
            try {
                recursionDepth = Integer.parseInt(args[1]);
                return new WebpageAnalyzer(url, recursionDepth);
            } catch (NumberFormatException nfe) {
                WebpageAnalyzer.cliState = CliParsingState.ARGUMENT_PARSING_ERROR;
                throw new IllegalArgumentException("Recursion depth must be a number!");
            }
        }
    }

    private static void printUsageTextOnError(String message) {
        System.out.println("Error while parsing CLI arguments: " + message);
        printUsageInfo();
    }

    private static void printUsageInfo() {
        System.out.println("Usage: java -jar crawler.jar <URL> <RECURSION_DEPTH (optional; default=2)>");
        System.out.println("Examples:");
        System.out.println("\t * java -jar crawler.jar https://www.google.at");
        System.out.println("\t * java -jar crawler.jar https://www.google.at 3");
    }

    public List<WebpageMetric> getWebpageMetrics() {
        return webpageMetrics;
    }

    public int getMaxLinkDepth() {
        return this.maxLinkDepth;
    }

    public static CliParsingState getCliState() {
        return cliState;
    }

    public static void setCliState(CliParsingState cliState) {
        WebpageAnalyzer.cliState = cliState;
    }

    public static WebpageAnalyzer getRootPageAnalyzer() {
        return rootPage;
    }
}
