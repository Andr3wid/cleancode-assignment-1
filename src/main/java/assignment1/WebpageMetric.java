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

    public WebpageMetric(Document document) {
        brokenLinks = new ArrayList<>();
        imageCount = 0;
        getMetrics(document);
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

    private void getMetrics(Document document) {
        imageCount = document.body().select("img").size();
        videoCount = document.body().select("video").size();
        Document htmlWithoutTitle = this.removeHtmlElementsBySelector(document, "title");
        String strippedText = this.preProcessText(htmlWithoutTitle.text());
        wordCount = strippedText.split(" ").length;
    }

    /**
     * Removes all nodes that match the given selector and returns a copy of the document.
     *
     * @param doc      document to be removed from
     * @param selector
     * @return a stripped copy of the document
     */
    private Document removeHtmlElementsBySelector(Document doc, String selector) {
        Document strippedDoc = doc.clone();
        Elements matchedElements = strippedDoc.select("title");

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
    private String preProcessText(String text) {
        // TODO: find regex that works for all non-english letter (e.g. scandinavian or balkan-languages, japanese ...)
        text = text.replaceAll("[^a-zA-zöüäÖÜÄß]", " ");
        // TODO: Check if trim can be removed.
        text = text.trim();
        text = text.replaceAll(" +", " ");

        return text;
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
}
