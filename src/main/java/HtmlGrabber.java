import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Grabs HTML from a given URL, parses it and recursively grabs linked resources.
 */
public class HtmlGrabber {
    public static final int BROKEN_LINK_DEPTH_DEFAULT = 2;

    private final Document grabbedHtml;
    private final String url;
    private int brokenLinkDepth;
    private final Elements links;

    public HtmlGrabber(String url) throws IOException {
        this.url = url;
        this.grabbedHtml = Jsoup.connect(this.url).get();
        this.brokenLinkDepth = BROKEN_LINK_DEPTH_DEFAULT;
        // TODO: check if "a" is a sufficient criterion. (could also just be an on-page anchor ...)
        this.links = this.grabbedHtml.select("a");
    }

    public HtmlGrabber(String url, int brokenLinkDepth) throws IOException {
        this(url);
        this.brokenLinkDepth = brokenLinkDepth;
    }

    // GETTER & SETTER section
    public static int getBrokenLinkDepthDefault() {
        return BROKEN_LINK_DEPTH_DEFAULT;
    }

    public String getUrl() {
        return url;
    }

    public int getBrokenLinkDepth() {
        return brokenLinkDepth;
    }

    public void setBrokenLinkDepth(int brokenLinkDepth) {
        this.brokenLinkDepth = brokenLinkDepth;
    }

    public Elements getLinks() {
        return this.links;
    }

    public int getLinkCount() {
        return this.links.toArray().length;
    }

    public Document getGrabbedHtml() {
        return grabbedHtml;
    }
}
