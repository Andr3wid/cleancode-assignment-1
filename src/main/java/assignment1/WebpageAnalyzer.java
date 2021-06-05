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

public class WebpageAnalyzer implements Runnable {

    /**
     * Default recursion depth if not stated otherwise (via CLI args)
     */
    public static int DEFAULT_RECURSION_DEPTH = 2;

    /**
     * URL to be analyzed
     */
    private final URL url;

    /**
     * Report-object that is used to store content and generate a file-based report.
     */
    private final WebpageAnalyzerReport report = new WebpageAnalyzerReport();

    /**
     * Specifies the level of recursion to follow links on the {@link WebpageAnalyzer#url}
     * Defaults to {@link WebpageAnalyzer#DEFAULT_RECURSION_DEPTH}
     */
    private int maxLinkDepth;

    /**
     * List for the Metrics of all analyzed webpages
     */
    private final List<WebpageMetric> webpageMetrics;



    public WebpageAnalyzer(String url, int linkDepth) throws MalformedURLException {
        this(url);
        this.maxLinkDepth = linkDepth;
    }

    public WebpageAnalyzer(String url) throws MalformedURLException {
        this.maxLinkDepth = DEFAULT_RECURSION_DEPTH;
        webpageMetrics = new ArrayList<>();
        this.url = new URL(url);
    }


    /**
     * Starts the recursive analyze process of the webpage
     *
     */
    @Override
    public void run() {
        try {
            Document rootDocument = Jsoup.connect(url.toString()).get();
            analyze(rootDocument, maxLinkDepth);
        } catch(IOException ioe) {
            // TODO: Make visible that URL was invalid
        }
    }

    private void analyze(Document document, int linkDepth) {
        WebpageMetric webpageMetric = new WebpageMetric(document);

        Elements links = document.select("a");
        webpageMetric.setLinkCount(links.size());

        if (linkDepth != 0) {
            System.out.println("links to visit: " + links.size() + " for " + document.location());
            int i = 1;
            for (Element link : links) {
                URL linkURL = null;
                try {
                    linkURL = new URL(link.attr("href"));
                    System.out.println(linkDepth + " : " + (i++) + "/" + links.size() + " : " + linkURL);

                    Document linkDocument = Jsoup.connect(linkURL.toString()).get();
                    analyze(linkDocument, linkDepth - 1);
                } catch (HttpStatusException e) {
                    if (e.getStatusCode() == 404) {
                        webpageMetric.addBrokenLink(linkURL.toString());
                    }
                } catch (IOException e) {
                    // ignore non conform URLs and non html types of response
                }
            }
        }
        webpageMetrics.add(webpageMetric);
        report.addToReport(webpageMetric.toString());
    }

    public List<WebpageMetric> getWebpageMetrics() {
        return webpageMetrics;
    }

    public int getMaxLinkDepth() {
        return this.maxLinkDepth;
    }

    public WebpageAnalyzerReport getReport() {
        return this.report;
    }

    public URL getUrl() {
        return this.url;
    }

}
