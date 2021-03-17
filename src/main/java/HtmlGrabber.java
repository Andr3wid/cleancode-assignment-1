import java.util.ArrayList;

/**
 * Grabs HTML from a given URL, parses it and recursively grabs linked resources.
 */
public class HtmlGrabber {
    public static final int BROKEN_LINK_DEPTH_DEFAULT = 2;

    private final String url;
    private int brokenLinkDepth;
    private final ArrayList<HtmlGrabber> adjacentPages;

    public HtmlGrabber(String url) {
        this.adjacentPages = new ArrayList<>();
        this.url = url;
    }

    public HtmlGrabber(String url, int brokenLinkDepth) {
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

    public ArrayList<HtmlGrabber> getAdjacentPages() {
        return adjacentPages;
    }
}
