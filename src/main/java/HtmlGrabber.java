import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

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

    /**
     * Counts the number of words on a web page.
     * @return the number of words counted
     */
    public int countNumberOfWords() {
        Document htmlWithoutTitle = this.removeHtmlElementsBySelector(this.grabbedHtml, "title");
        String strippedText = this.preProcessText(htmlWithoutTitle.text());
        String[] words = strippedText.split(" ");

        return words.length;
    }

    /**
     * Pre-processes text for word-counting by
     * - replacing all non-alphanumeric characters with a space
     * - replace all whitespace-chars with a single space
     * @param text the text to be processed
     * @return the text with removed tokens and whitespace
     */
    private String preProcessText(String text) {
        text = text.replaceAll("[^a-zA-z]", " ");
        text = text.trim();
        text = text.replaceAll(" +", " ");

        return text;
    }

    /**
     * Removes all nodes that match the given selector and returns a copy of the document.
     * @param doc document to be removed from
     * @param selector css-selector to match nodes
     * @return a stripped copy of the document
     */
    private Document removeHtmlElementsBySelector(Document doc, String selector) {
        Document strippedDoc = doc.clone();
        Elements matchedElements = strippedDoc.select(selector);

        for(Element element : matchedElements) {
            element.remove();
        }

        return strippedDoc;
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
}
