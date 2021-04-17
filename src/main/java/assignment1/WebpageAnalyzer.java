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

    public static void main(String[] args) throws MalformedURLException {
//        WebpageAnalyzer wpa = new WebpageAnalyzer("http://duck.com/");
        WebpageAnalyzer wpa = new WebpageAnalyzer("https://google.com/");
//        WebpageAnalyzer wpa = new WebpageAnalyzer("http://andref.xyz/");
        try {
            wpa.analyze();
            System.out.println("finshed");
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
        analyze(document, maxLinkDepth, url.toString());
    }

    private void analyze(Document document, int linkDepth, String documentUrl) {

        WebpageMetric webpageMetric = new WebpageMetric(document, documentUrl);

        Elements links = document.select("a");
        webpageMetric.setLinkCount(links.size());

        if (linkDepth != 0) {
            System.out.println("links to visit: " + links.size());
            for (int i = 0; i < links.size(); i++) {
                Element link = links.get(i);
                try {
                    URL urlsn = new URL(link.attr("href"));
//                    String linkUrl = urlsn.getProtocol() + "://" + urlsn.getHost();

                    System.out.println(linkDepth + " : " + i + "/" + links.size() + " : " + urlsn);
                    // only pages that lead to another page
//                    if (!pageUrl.startsWith(linkUrl)) {
                    try {
                        Document linkDocument = Jsoup.connect(urlsn.toString()).get();
                        analyze(linkDocument, linkDepth - 1, urlsn.toString());
                    } catch (HttpStatusException e) {
                        if (e.getStatusCode() == 404) {
                            webpageMetric.addBrokenLink(urlsn.toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
//                        }
                    }
                } catch (MalformedURLException ignored) {

                }
            }
        }
        webpageMetrics.add(webpageMetric);
    }

    public List<WebpageMetric> getWebpageMetrics() {
        return webpageMetrics;
    }
}
