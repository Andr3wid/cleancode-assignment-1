import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Grabs HTML from a given URL, parses it and recursively grabs linked resources.
 */
public class HtmlGrabber {
    public static final int BROKEN_LINK_DEPTH_DEFAULT = 2;

    private final Document grabbedHtml;
    private final String url;
    private int brokenLinkDepth;
    private final ArrayList<HtmlGrabber> adjacentPages;

    public HtmlGrabber(String url) throws IOException {
        this.adjacentPages = new ArrayList<>();
        this.url = url;
        this.grabbedHtml = Jsoup.connect(this.url).get();
        this.brokenLinkDepth = BROKEN_LINK_DEPTH_DEFAULT;
    }

    public HtmlGrabber(String url, int brokenLinkDepth) throws IOException {
        this(url);
        this.brokenLinkDepth = brokenLinkDepth;
    }





    public int countNumberOfLinks() {
        return -1;
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

    public ArrayList<HtmlGrabber> getAdjacentPages() {
        return adjacentPages;
    }

    public Document getGrabbedHtml() {
        return grabbedHtml;
    }
}
