package assignment1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Grabs HTML from a given URL, parses it and recursively grabs linked resources.
 */
public class HtmlGrabber {
    public static final int BROKEN_LINK_DEPTH_DEFAULT = 2;

    private Document grabbedHtml;
    private final String url;
    private int brokenLinkDepth;
    private Elements links;
    private boolean isBroken;

    public HtmlGrabber(String url) {
        this.url = url;
        this.brokenLinkDepth = BROKEN_LINK_DEPTH_DEFAULT;
        this.instantiateGrabberByUrl(url);
    }

    public HtmlGrabber(String url, int brokenLinkDepth) {
        this(url);
        this.brokenLinkDepth = brokenLinkDepth;
    }

    private void instantiateGrabberByUrl(String url) {
        try {
            this.grabbedHtml = Jsoup.connect(this.url).get();
            // TODO: check if "a" is a sufficient criterion. (could also just be an on-page anchor ...)
            this.links = this.grabbedHtml.select("a");
            this.isBroken = false;
        } catch (Exception e) {
            this.grabbedHtml = null;
            this.links = null;
            this.isBroken = true;
        }
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
        if (this.links != null) {
            return this.links.toArray().length;
        } else {
            return 0;
        }
    }

    public Document getGrabbedHtml() {
        return grabbedHtml;
    }

    public boolean isBroken() {
        return this.isBroken;
    }
}
