package assignment1;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class WebpageMetric {

    private final List<String> brokenLinks;

    private int linkCount;
    private int imageCount;
    private int videoCount;
    private int wordCount;
    private final String url;

    public WebpageMetric(Document document) {
        this.url = document.location();
        brokenLinks = new ArrayList<>();
        imageCount = 0;
        getMetrics(document);
    }


    private void getMetrics(Document document) {
        imageCount = document.body().select("img").size();
        videoCount = document.body().select("video").size();
        Document htmlWithoutTitle = removeHtmlElementsBySelector(document, "title");
        String strippedText = preProcessText(htmlWithoutTitle.text());
        wordCount = strippedText.split(" ").length;
    }

    /**
     * Removes all nodes that match the given selector and returns a copy of the document.
     *
     * @param doc document to be removed from
     * @return a stripped copy of the document
     */
    public Document removeHtmlElementsBySelector(Document doc, String selector) {
        Document strippedDoc = doc.clone();
        Elements matchedElements = strippedDoc.select(selector);

        for (Element element : matchedElements) {
            element.remove();
        }

        return strippedDoc;
    }

    /**
     * Pre-processes text for word-counting by
     * - replacing all non-alphanumeric characters with a space
     * - replace all whitespace-chars with a single space
     *
     * @param text the text to be processed
     * @return the text with removed tokens and whitespace
     */
    public String preProcessText(String text) {
        // NOTE: regex works only for english letters  not for (scandinavian or balkan-languages, japanese ...)
        text = text.replaceAll("[^a-zA-zöüäÖÜÄß]", " ");
        text = text.trim();
        text = text.replaceAll(" +", " ");

        return text;
    }

    public void addBrokenLink(String url) {
        brokenLinks.add(url);
    }

    public List<String> getBrokenLinks() {
        return brokenLinks;
    }

    public void setLinkCount(int linkCount) {
        this.linkCount = linkCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    public int getLinkCount() {
        return linkCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    @Override
    public String toString() {
        return "WebpageMetric{" +
                "url=" + url +
                ", brokenLinks=" + brokenLinks +
                ", linkCount=" + linkCount +
                ", imageCount=" + imageCount +
                ", videoCount=" + videoCount +
                ", wordCount=" + wordCount +
                "}\n";
    }
}
